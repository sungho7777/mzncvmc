package com.in.mzncvmc.content.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CompanyService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * C.데이터 생성
     *
     * @param dto 해당 정보 DTO
     * @return 생성된 데이터 ID
     * @throws IllegalArgumentException 입력 값 검증 실패 시
     */
    @Transactional
    public Long insert(CompanyDto dto) {
        Company company = new Company();
        company.setCompanyName(dto.getCompanyName());
        company.setCompanyEngName(dto.getCompanyEngName());
        company.setCompanyType(dto.getCompanyType());

        Company saved = companyRepository.save(company);

        return saved.getCompanyId();
    }

    /**
     * R.데이터 목록조회
     *
     * @param search 조회할 목록 데이터 search
     * @return DataList 조회된 목록 데이터
     *
     */
    @Transactional(readOnly = true)
    public Page<CompanyDto> getPagedDatas(int page, int size, String search, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "companyId"));

        // status 처리
        Company.Status statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = Company.Status.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // 유효하지 않은 status는 null로 처리
                statusEnum = null;
            }
        }

        Page<Company> DataPage;

        if ((search == null || search.isBlank()) && statusEnum == null) {
            DataPage = companyRepository.findAll(pageable);
        } else {
            // 검색어 존재
            DataPage = companyRepository.searchAll(search.trim(), statusEnum, pageable);
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
    public CompanyDto findById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Data not found"));

        return toDto(company); // 엔티티 → DTO 변환 메서드
    }

    /**
     * U.데이터 수정
     *
     * @param !Data 수정할 데이터 엔티티 (password 제외)
     * @throws IllegalArgumentException 데이터 미존재
     */
    @Transactional
    public void update(CompanyDto dto) {
        Company existing = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        existing.setCompanyName(dto.getCompanyName());
        existing.setCompanyEngName(dto.getCompanyEngName());
        existing.setCompanyType(dto.getCompanyType());

        existing.setStatus(Company.Status.valueOf(dto.getStatus().toUpperCase()));

        companyRepository.save(existing);
    }

    /**
     * D.데이터 삭제
     *
     * @param id 삭제할 데이터 ID
     * @throws IllegalArgumentException 데이터 미존재
     */
    @Transactional
    public void delete(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Data not found"));

        companyRepository.delete(company);
    }

    /**
     * ETC.엔티티 → DTO 변환용 private 메서드
     *
     * @param !Entity 데이터
     */
    private CompanyDto toDto(Company entity) {
        CompanyDto dto = CompanyDto.builder()
            .companyId(entity.getCompanyId())
            .companyName(entity.getCompanyName())
            .companyEngName(entity.getCompanyEngName())
            .companyType(entity.getCompanyType())

            .status(entity.getStatus().name())

            .build();

        //logger.debug("CompanyDto : " + dto);
        return dto;
    }
}
