package com.in.mzncvmc.content.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    /**
     * 데이터 목록 조회
     *
     * @param
     * @return Optional<Vo> 데이터 엔티티
     */
    List<Company> findAllByOrderByCompanyIdDesc();

    /**
     * 데이터 목록 조회
     *
     * @param search 조회할 데이터
     * @return Optional<Vo> 데이터 엔티티
     */
    @Query("SELECT u " +
            " FROM Company u " +
            "WHERE (:search IS NULL OR :search = '' " +
            "       OR u.companyName LIKE CONCAT('%', :search, '%') " +
            "       OR u.companyEngName LIKE CONCAT('%', :search, '%') " +
            "       OR u.businessNumber LIKE CONCAT('%', :search, '%') " +
            "       OR u.ceoName LIKE CONCAT('%', :search, '%')" +
            ") " +
            "  AND (:status IS NULL OR u.status = :status) " +
            "ORDER BY u.companyId DESC")
    Page<Company> searchAll(
            @Param("search") String search,
            @Param("status") Company.Status status,
            Pageable pageable
    );
}
