package com.in.mzncvmc.content.users;


import com.in.mzncvmc.content.company.Company;
import com.in.mzncvmc.content.company.CompanyRepository;
import com.in.mzncvmc.content.company.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;        // Page<T>
import org.springframework.data.domain.Pageable;   // Pageable
import org.springframework.data.domain.PageRequest; // PageRequest.of()
import org.springframework.data.domain.Sort;        // 정렬 옵션

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UsersService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private PasswordEncoder passwordEncoder;
    private UsersRepository usersRepository;
    private CompanyRepository companyRepository;

    @Autowired
    public UsersService(PasswordEncoder passwordEncoder, UsersRepository usersRepository, CompanyRepository companyRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.companyRepository = companyRepository;
    }

    public Users createUser(String username, String email, String password) {
        if (usersRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        if (usersRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        return usersRepository.save(user);
    }

    public Optional<Users> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return usersRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return usersRepository.existsByEmail(email);
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
        user.setPhone(dto.getPhone());
        user.setRole(Users.Role.valueOf(dto.getRole().toUpperCase()));
        user.setStatus(Users.Status.valueOf(dto.getStatus().toUpperCase()));

        // 비밀번호 기본값 설정 (암호화 적용)
        user.setPassword("1");

        Users saved = usersRepository.save(user);

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
    public Page<UsersDto> getPagedDatas(int page, int size, String search, String status) {
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

        return DataPage.map(this::toDto);
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

        return toDto(user); // 엔티티 → DTO 변환 메서드
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
                .orElseThrow(() -> new IllegalArgumentException("Data not found"));

        usersRepository.delete(user);
    }

    /**
     * ETC.엔티티 → DTO 변환용 private 메서드
     *
     * @param !Entity 데이터
     */
    private UsersDto toDto(Users entity) {
        UsersDto dto = UsersDto.builder()
            .userId(entity.getUserId())
            .username(entity.getUsername())
            .fullName(entity.getFullName())
            .email(entity.getEmail())
            .phone(entity.getPhone())
            .role(entity.getRole().name())
            .status(entity.getStatus().name())

            .companyId(entity.getCompanyId() != null ? entity.getCompanyId().getCompanyId() : null)
            .companyName(entity.getCompanyId() != null ? entity.getCompanyId().getCompanyName() : null)
            .companyType(entity.getCompanyId() != null ? entity.getCompanyId().getCompanyType() : null)

            .build();

        //logger.debug("UsersDto : " + dto);
        return dto;
    }
}
