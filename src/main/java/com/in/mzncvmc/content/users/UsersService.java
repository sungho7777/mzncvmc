package com.in.mzncvmc.content.users;

import com.in.mzncvmc.common.auth.oAuth.OAuthUserInfo;
import com.in.mzncvmc.common.system.mail.MailService;
import com.in.mzncvmc.content.company.Company;
import com.in.mzncvmc.content.company.CompanyRepository;
import com.in.mzncvmc.content.userMfa.UserMfa;
import com.in.mzncvmc.content.userMfa.UserMfaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService{

    @Value("${user.first.password}")
    private String userFirstPassword;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final UserMfaRepository userMfaRepository;
    @Autowired
    private final CompanyRepository companyRepository;
    @Autowired
    private final MailService mailService;

    @Transactional
    public Users processOAuthUser(OAuthUserInfo userInfo) {

        String provider = userInfo.getProvider();       // "google"
        String providerId = userInfo.getProviderId();   // google user id
        Users.Provider providerEnum =
                Users.Provider.valueOf(provider.toUpperCase());

        Optional<Users> optionalUser =
                usersRepository.findByProviderAndProviderId(providerEnum, providerId);

        // 1. 기존 사용자 조회
        //Optional<Users> optionalUser = usersRepository.findByProviderAndProviderId(provider, providerId);

        if(optionalUser.isPresent()) {
            // 기존 회원 → 바로 반환
            return optionalUser.get();
        }

        Company company = companyRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        // 2. 신규 회원 생성
        Users newUser = Users.builder()
                .companyId(company)
                .username(userInfo.getEmail())
                .password("1")
                .fullName(userInfo.getName())
                .email(userInfo.getEmail())
                //.profileImage(userInfo.getProfileImage()) // TODO 컬럼 추가
                .provider(Users.Provider.valueOf(provider.toUpperCase()))
                .providerId(providerId)
                .role(Users.Role.valueOf("USER"))

                .status(Users.Status.valueOf("ACTIVE".toUpperCase()))
                .pwNotifyDuration("999")
                .connected(Users.Connected.valueOf("N".toUpperCase()))

                .build();

        mailService.sendCreateOAuthUser(newUser.getEmail());

        return usersRepository.save(newUser);
    }

    /**
     * R.데이터 단일조회
     *
     * @param username
     * @return Optional Entity
     */
    public Optional<Users> findByUsername(String username) {

        return usersRepository.findByUsername(username);
    }

    /**
     * C.데이터 생성
     *
     * @param dto 해당 정보 DTO
     * @return 생성된 데이터 ID
     * @throws IllegalArgumentException 입력 값 검증 실패 시
     */
    @Transactional
    public Long insert(UsersDto dto) {
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        Users user = new Users();
        user.setUsername(dto.getUsername());
        user.setCompanyId(company);
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setProvider(Users.Provider.valueOf(dto.getProviderId().toUpperCase())); // default 'LOCAL'
        user.setProviderId(null);
        user.setPhone(dto.getPhone());
        user.setRole(Users.Role.valueOf(dto.getRole().toUpperCase()));
        user.setPwNotifyDuration("999");
        user.setStatus(Users.Status.valueOf(dto.getStatus().toUpperCase()));
        //user.setConnected(Users.Connected.valueOf("N"));

        // 비밀번호 기본값 설정 (암호화 적용)
        user.setPassword(passwordEncoder.encode(userFirstPassword));

        Users saved = usersRepository.save(user);

        mailService.sendCreateLocalUser(saved.getEmail());

        return saved.getUserId();
    }

    /**
     * R.데이터 목록조회
     *
     * @param search 조회할 목록 데이터 search
     * @return DataList 조회된 목록 데이터
     *
     */
    @Transactional(readOnly = true)
    public Page<UsersDto> getPagedLists(int page, int size, String search, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "userId"));

        // status 처리
        Users.Status statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = Users.Status.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // 유효하지 않은 status는 null로 처리
                statusEnum = null;
            }
        }

        Page<Users> DataPage;

        if ((search == null || search.isBlank()) && statusEnum == null) {
            DataPage = usersRepository.findAll(pageable);
        } else {
            // 검색어 존재
            DataPage = usersRepository.searchAll(search.trim(), statusEnum, pageable);
        }

        return DataPage.map(this::entityToDto);
    }


    /**
     * R.데이터 단일조회
     *
     * @param id 조회할 단일 데이터 ID
     * @return DataDto 조회된 단일 데이터
     * @throws IllegalArgumentException 데이터 미존재
     */
    @Transactional(readOnly = true)
    public UsersDto findById(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Data not found"));

        return entityToDto(user); // 엔티티 → DTO 변환 메서드
    }
    @Transactional(readOnly = true)
    public UsersDto findByEmail(String email) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Data not found"));

        return entityToDto(user); // 엔티티 → DTO 변환 메서드
    }

    /**
     * U.데이터 수정
     *
     * @param !Data 수정할 데이터 엔티티 (password 제외)
     * @throws IllegalArgumentException 데이터 미존재
     */
    @Transactional
    public void update(UsersDto dto) {
        Users existing = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        existing.setUsername(dto.getUsername());
        existing.setCompanyId(company);
        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setProvider(Users.Provider.valueOf(dto.getProviderId().toUpperCase())); // default 'LOCAL'
        existing.setProviderId(null);
        existing.setPhone(dto.getPhone());
        existing.setRole(Users.Role.valueOf(dto.getRole().toUpperCase()));
        existing.setStatus(Users.Status.valueOf(dto.getStatus().toUpperCase()));

        usersRepository.save(existing);
    }

    /**
     * D.데이터 삭제
     *
     * @param id 삭제할 데이터 ID
     * @throws IllegalArgumentException 데이터 미존재
     */
    @Transactional
    public void delete(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user Data not found"));

        // 사용자 mfa 정보도 완전 삭제한다.
        userMfaRepository.deleteByUserId(user.getUserId());

        usersRepository.delete(user);
    }

    /**
     * ETC.엔티티 → DTO 변환용 private 메서드
     *
     * @param !Entity 데이터
     */
    private UsersDto entityToDto(Users entity) {
        UsersDto dto = UsersDto.builder()
            .userId(entity.getUserId())
            .username(entity.getUsername())
            .fullName(entity.getFullName())
            .email(entity.getEmail())
            .provider(entity.getProviderId())
            .providerId(entity.getProviderId())

            .phone(entity.getPhone())
            .role(entity.getRole().name())
            .status(entity.getStatus().name())
            .pwNotifyDuration((entity.getPwNotifyDuration()))
            .connected(entity.getConnected().name())

            .companyId(entity.getCompanyId() != null ? entity.getCompanyId().getCompanyId() : null)
            .companyName(entity.getCompanyId() != null ? entity.getCompanyId().getCompanyName() : null)
            .companyType(entity.getCompanyId() != null ? entity.getCompanyId().getCompanyType() : null)

            .build();

        //log.debug("UsersDto : " + dto);
        return dto;
    }

    /**
     * ETC.사용자 접속여부를 업데이트 한다.
     *
     * @param !ID, connected
     */
    @Transactional
    public int updateUsersConnected(String username, String connected){

        return usersRepository.updateUsersConnected(username, Users.Connected.valueOf(connected.toUpperCase()));
    }

    /**
     * ETC.모든 사용자 접속여부를 업데이트 한다.
     *
     * @param connected
     */
    @Transactional
    public int updateAllUsersConnected(String connected){

        return usersRepository.updateAllUsersConnected(Users.Connected.valueOf(connected.toUpperCase()));
    }
}
