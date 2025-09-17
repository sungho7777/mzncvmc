package com.in.mzncvmc.content.bbs.posts;

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
public class BbsPostsService {
    private final  BbsPostsRepository bbsPostsRepository;

    @Autowired
    public BbsPostsService(BbsPostsRepository bbsPostsRepository) {
        this.bbsPostsRepository = bbsPostsRepository;
    }

    /**
     * C.데이터 생성
     *
     * @param dto 해당 정보 DTO
     * @return 생성된 데이터 ID
     * @throws IllegalArgumentException 입력 값 검증 실패 시
     */
    @Transactional
    public Long insert(BbsPostsDto dto) {
        BbsPosts bbsPosts = new BbsPosts();

        //bbsPosts.setPostId(dto.getPostId());
        bbsPosts.setCategoryId(dto.getCategoryId());
        bbsPosts.setTitle(dto.getTitle());
        bbsPosts.setBbsContent(dto.getBbsContent());
        bbsPosts.setAuthorId(dto.getAuthorId());
        bbsPosts.setAuthorName(dto.getAuthorName());
        bbsPosts.setAuthorIp(dto.getAuthorIp());
        bbsPosts.setPassword(dto.getPassword());
        bbsPosts.setStatus(BbsPosts.Status.valueOf(dto.getStatus().toUpperCase()));
        bbsPosts.setIsNotice(dto.getIsNotice());
        bbsPosts.setIsTopFixed(dto.getIsTopFixed());
        bbsPosts.setIsSecret(dto.getIsSecret());
        bbsPosts.setViewCount(dto.getViewCount());
        bbsPosts.setLikeCount(dto.getLikeCount());
        bbsPosts.setDislikeCount(dto.getDislikeCount());
        bbsPosts.setCommentCount(dto.getCommentCount());
        bbsPosts.setFileCount(dto.getFileCount());
        bbsPosts.setParentId(dto.getParentId());
        bbsPosts.setDepth(dto.getDepth());
        bbsPosts.setGroupId(dto.getGroupId());
        bbsPosts.setGroupOrder(dto.getGroupOrder());
        bbsPosts.setTags(dto.getTags());
        bbsPosts.setMetaData(dto.getMetaData());
        //bbsPosts.setCreatedDate(dto.getCreatedDate());
        //bbsPosts.setUpdatedDate(dto.getUpdatedDate());
        //bbsPosts.setDeletedDate(dto.getDeletedDate());

        BbsPosts saved = bbsPostsRepository.save(bbsPosts);

        return saved.getPostId();
    }

    /**
     * R.데이터 목록조회
     *
     * @param search 조회할 목록 데이터 search
     * @return DataList 조회된 목록 데이터
     *
     */
    @Transactional(readOnly = true)
    public Page<BbsPostsDto> getPagedLists(int page, int size, String categoryId, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "postId"));

        Page<BbsPosts> DataPage;

        if (search == null || search.isBlank()) {
            log.debug("BbsPostsService.getPagedDatas.findByCategoryId");
            //DataPage = bbsPostsRepository.findAll(pageable);
            DataPage = bbsPostsRepository.findByCategoryId(Long.valueOf(categoryId), pageable);
        } else {
            // 검색어 존재
            log.debug("BbsPostsService.getPagedDatas.searchAll");
            DataPage = bbsPostsRepository.searchAll(categoryId, search.trim(), pageable);
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
    public BbsPostsDto findById(Long id) {
        BbsPosts bbsPosts = bbsPostsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Data not found"));

        return entityToDto(bbsPosts); // 엔티티 → DTO 변환 메서드
    }

    /**
     * U.데이터 수정
     *
     * @param !Data 수정할 데이터 엔티티 (password 제외)
     * @throws IllegalArgumentException 데이터 미존재
     */
    @Transactional
    public void update(BbsPostsDto dto) {
        BbsPosts existing = bbsPostsRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Data not found"));


        //existing.setPostId(dto.getPostId());
        existing.setCategoryId(dto.getCategoryId());
        existing.setTitle(dto.getTitle());
        existing.setBbsContent(dto.getBbsContent());
        existing.setAuthorId(dto.getAuthorId());
        existing.setAuthorName(dto.getAuthorName());
        existing.setAuthorIp(dto.getAuthorIp());
        existing.setPassword(dto.getPassword());
        existing.setStatus(BbsPosts.Status.valueOf(dto.getStatus().toUpperCase()));
        existing.setIsNotice(dto.getIsNotice());
        existing.setIsTopFixed(dto.getIsTopFixed());
        existing.setIsSecret(dto.getIsSecret());
        existing.setViewCount(dto.getViewCount());
        existing.setLikeCount(dto.getLikeCount());
        existing.setDislikeCount(dto.getDislikeCount());
        existing.setCommentCount(dto.getCommentCount());
        existing.setFileCount(dto.getFileCount());
        existing.setParentId(dto.getParentId());
        existing.setDepth(dto.getDepth());
        existing.setGroupId(dto.getGroupId());
        existing.setGroupOrder(dto.getGroupOrder());
        existing.setTags(dto.getTags());
        existing.setMetaData(dto.getMetaData());
        //existing.setCreatedDate(dto.getCreatedDate());
        //existing.setUpdatedDate(dto.getUpdatedDate());
        //existing.setDeletedDate(dto.getDeletedDate());

        bbsPostsRepository.save(existing);
    }

    /**
     * D.데이터 삭제
     *
     * @param id 삭제할 데이터 ID
     * @throws IllegalArgumentException 데이터 미존재
     */
    @Transactional
    public void delete(Long id) {
        BbsPosts bbsPosts = bbsPostsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Data not found"));

        bbsPostsRepository.delete(bbsPosts);
    }

    /**
     * ETC.엔티티 → DTO 변환용 private 메서드
     *
     * @param !Entity 데이터
     */
    private BbsPostsDto entityToDto(BbsPosts entity) {
        BbsPostsDto dto = BbsPostsDto.builder()
            .postId(entity.getPostId())
            .categoryId(entity.getCategoryId())
            .title(entity.getTitle())
            .bbsContent(entity.getBbsContent())
            .authorId(entity.getAuthorId())
            .authorName(entity.getAuthorName())
            .authorIp(entity.getAuthorIp())
            .password(entity.getPassword())
            .status(entity.getStatus().name())
            .isNotice(entity.getIsNotice())
            .isTopFixed(entity.getIsTopFixed())
            .isSecret(entity.getIsSecret())
            .viewCount(entity.getViewCount())
            .likeCount(entity.getLikeCount())
            .dislikeCount(entity.getDislikeCount())
            .commentCount(entity.getCommentCount())
            .fileCount(entity.getFileCount())
            .parentId(entity.getParentId())
            .depth(entity.getDepth())
            .groupId(entity.getGroupId())
            .groupOrder(entity.getGroupOrder())
            .tags(entity.getTags())
            .metaData(entity.getMetaData())
            .createdDate(entity.getCreatedDate())
            .updatedDate(entity.getUpdatedDate())
            .deletedDate(entity.getDeletedDate())

            .build();

        return dto;
    }
}
