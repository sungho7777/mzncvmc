package com.in.mzncvmc.content.bbs.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BbsPostsRepository extends JpaRepository<BbsPosts, Long> {


    Page<BbsPosts> findByCategoryId(Long categoryId, Pageable pageable);
    /**
     * 데이터 목록 조회
     *
     * @param search 조회할 데이터
     * @return Optional<Vo> 데이터 엔티티
     */
    @Query("SELECT u " +
            " FROM BbsPosts u " +
            "WHERE (:search IS NULL OR :search = '' " +
            "       OR u.title LIKE CONCAT('%', :search, '%') " +
            "       OR u.bbsContent LIKE CONCAT('%', :search, '%') " +
            ") " +
            "  AND (:categoryId IS NULL OR u.categoryId = :categoryId)" +
            "ORDER BY u.postId DESC")
    Page<BbsPosts> searchAll(
            @Param("categoryId") String categoryId,
            @Param("search") String search,
            Pageable pageable
    );

}
