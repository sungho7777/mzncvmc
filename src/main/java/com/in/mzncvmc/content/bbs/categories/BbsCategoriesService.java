package com.in.mzncvmc.content.bbs.categories;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional(readOnly = true)
public class BbsCategoriesService {
    private final BbsCategoriesRepository bbsCategoriesRepository;

    @Autowired
    public BbsCategoriesService(BbsCategoriesRepository bbsCategoriesRepository) {
        this.bbsCategoriesRepository = bbsCategoriesRepository;
    }

    /**
     * C.데이터 생성
     *
     * @param dto 해당 정보 DTO
     * @return 생성된 데이터 ID
     * @throws IllegalArgumentException 입력 값 검증 실패 시
     */
    @Transactional
    public Long insert(BbsCategoriesDto dto) {
        BbsCategories bbsCategories = new BbsCategories();

        bbsCategories.setCategoryName(dto.getCategoryName());
        bbsCategories.setCategoryCode(dto.getCategoryCode());
        bbsCategories.setDescription(dto.getDescription());
        bbsCategories.setSortOrder(dto.getSortOrder());
        bbsCategories.setIsActive(dto.getIsActive());
        bbsCategories.setAllowAnonymous(dto.getAllowAnonymous());
        bbsCategories.setAllowFileUpload(dto.getAllowFileUpload());
        bbsCategories.setMaxFileCount(dto.getMaxFileCount());
        bbsCategories.setReadPermission(dto.getReadPermission());
        bbsCategories.setWritePermission(dto.getWritePermission());
        bbsCategories.setCreatedDate(dto.getCreatedDate());
        bbsCategories.setUpdatedDate(dto.getUpdatedDate());
        bbsCategories.setCreatedBy(dto.getCreatedBy());

        BbsCategories saved = bbsCategoriesRepository.save(bbsCategories);

        return saved.getCategoryId();
    }


    /**
     * R.데이터 목록조회
     *
     * @param search 조회할 목록 데이터 search
     * @return DataList 조회된 목록 데이터
     *
     */
    @Transactional(readOnly = true)
    public Page<BbsCategoriesDto> getPagedLists(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "categoryId"));

        Page<BbsCategories> DataPage;

        if (search == null || search.isBlank()) {
            log.debug("BbsCategoriesService.getPagedDatas.findAll");
            DataPage = bbsCategoriesRepository.findAll(pageable);
        } else {
            // 검색어 존재
            log.debug("BbsCategoriesService.getPagedDatas.searchAll");
            DataPage = bbsCategoriesRepository.searchAll(search.trim(), pageable);
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
    public BbsCategoriesDto findById(Long id) {
        BbsCategories bbsCategories = bbsCategoriesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Data not found"));

        return entityToDto(bbsCategories); // 엔티티 → DTO 변환 메서드
    }

    /**
     * U.데이터 수정
     *
     * @param !Data 수정할 데이터 엔티티 (password 제외)
     * @throws IllegalArgumentException 데이터 미존재
     */
    @Transactional
    public void update(BbsCategoriesDto dto) {
        BbsCategories existing = bbsCategoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Data not found"));

        existing.setCategoryName(dto.getCategoryName());
        existing.setCategoryCode(dto.getCategoryCode());
        existing.setDescription(dto.getDescription());
        existing.setIsActive(dto.getIsActive());
        existing.setAllowAnonymous(dto.getAllowAnonymous());
        existing.setAllowFileUpload(dto.getAllowFileUpload());
        existing.setMaxFileCount(dto.getMaxFileCount());
        existing.setReadPermission(dto.getReadPermission());
        existing.setWritePermission(dto.getWritePermission());
        existing.setUpdatedDate(dto.getUpdatedDate());

        bbsCategoriesRepository.save(existing);
    }

    /**
     * D.데이터 삭제
     *
     * @param id 삭제할 데이터 ID
     * @throws IllegalArgumentException 데이터 미존재
     */
    @Transactional
    public void delete(Long id) {
        BbsCategories bbsCategories = bbsCategoriesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Data not found"));

        bbsCategoriesRepository.delete(bbsCategories);
    }
    /**
     * ETC.엔티티 → DTO 변환용 private 메서드
     *
     * @param !Entity 데이터
     */
    private BbsCategoriesDto entityToDto(BbsCategories entity) {
        BbsCategoriesDto dto = BbsCategoriesDto.builder()
                .categoryId(entity.getCategoryId())
                .categoryName(entity.getCategoryName())
                .categoryCode(entity.getCategoryCode())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .isActive(entity.getIsActive())
                .allowAnonymous(entity.getAllowAnonymous())
                .allowFileUpload(entity.getAllowFileUpload())
                .maxFileCount(entity.getMaxFileCount())
                .readPermission(entity.getReadPermission())
                .writePermission(entity.getWritePermission())
                .createdDate(entity.getCreatedDate())
                .updatedDate(entity.getUpdatedDate())
                .createdBy(entity.getCreatedBy())

                .build();

        //log.debug("BbsCategoriesDto : " + dto);
        return dto;
    }
}
