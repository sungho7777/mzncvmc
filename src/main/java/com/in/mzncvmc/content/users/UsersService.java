package com.in.mzncvmc.content.users;


import com.in.mzncvmc.content.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;        // Page<T>
import org.springframework.data.domain.Pageable;   // Pageable
import org.springframework.data.domain.PageRequest; // PageRequest.of()
import org.springframework.data.domain.Sort;        // 정렬 옵션

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UsersService {
    private UsersRepository usersRepository;
    private CompanyService companyService;

    @Autowired
    public UsersService(UsersRepository usersRepository, CompanyService companyService) {
        this.usersRepository = usersRepository;
        this.companyService = companyService;
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
        Users user = new Users();
        user.setUsername(dto.getUsername());
        user.setCompanyId(companyService.findByCompanyId(dto.getCompanyId()));
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
    public Page<UsersDto> getPagedUsers(int page, int size, String search, String status) {
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

        Page<Users> usersPage;

        if ((search == null || search.isBlank()) && statusEnum == null) {
            usersPage = usersRepository.findAll(pageable);
        } else {
            // 검색어 존재
            usersPage = usersRepository.searchAll(search.trim(), statusEnum, pageable);
        }

        return usersPage.map(this::toDto);
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
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return toDto(user); // 엔티티 → DTO 변환 메서드
    }

    /**
     * U.데이터 수정
     *
     * @param Data 수정할 데이터 엔티티 (password 제외)
     * @throws IllegalArgumentException 데이터 미존재
     */
    @Transactional
    public void update(UsersDto dto) {
        Users existing = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        existing.setUsername(dto.getUsername());
        existing.setCompanyId(companyService.findByCompanyId(dto.getCompanyId()));
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
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        usersRepository.delete(user);
    }

    /**
     * ETC.엔티티 → DTO 변환용 private 메서드
     *
     * @param Entity 데이터
     */
    private UsersDto toDto(Users entity) {
        return UsersDto.builder()
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
    }
}
