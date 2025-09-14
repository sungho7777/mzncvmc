package com.in.mzncvmc.content.bbs.categories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BbsCategoriesRepository extends JpaRepository<BbsCategories, Long> {
    /**
     * 데이터 목록 조회
     *
     * @param
     * @return Optional<Vo> 데이터 엔티티
     */
    //List<BbsCategories> findAllByOrderByCategoriesIdDesc();

    /**
     * 데이터 목록 조회
     *
     * @param search 조회할 데이터
     * @return Optional<Vo> 데이터 엔티티
     */
    @Query("SELECT u " +
            " FROM BbsCategories u " +
            "WHERE (:search IS NULL OR :search = '' " +
            "       OR u.categoryName LIKE CONCAT('%', :search, '%') " +
            "       OR u.categoryCode LIKE CONCAT('%', :search, '%') " +
            "       OR u.description LIKE CONCAT('%', :search, '%') " +
            ") " +
            "ORDER BY u.categoryId DESC")
    Page<BbsCategories> searchAll(
            @Param("search") String search,
            Pageable pageable
    );
}
