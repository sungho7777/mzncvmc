package com.in.mzncvmc.content.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;        // Page<T>
import org.springframework.data.domain.Pageable;   // Pageable
import org.springframework.data.domain.PageRequest; // PageRequest.of()
import org.springframework.data.domain.Sort;        // 정렬 옵션

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    /**
     * 데이터 목록 조회
     *
     * @param
     * @return Optional<Vo> 데이터 엔티티
     */
    List<Users> findAllByOrderByUserIdDesc();

    /**
     * 데이터 목록 조회
     *
     * @param search 조회할 데이터
     * @return Optional<Vo> 데이터 엔티티
     */
    @Query("SELECT u " +
            " FROM Users u " +
            "WHERE (:search IS NULL OR :search = '' " +
            "       OR u.username LIKE CONCAT('%', :search, '%') " +
            "       OR u.fullName LIKE CONCAT('%', :search, '%') " +
            "       OR u.phone LIKE CONCAT('%', :search, '%') " +
            "       OR u.email LIKE CONCAT('%', :search, '%')" +
            ") " +
            "  AND (:status IS NULL OR u.status = :status) " +
            "ORDER BY u.userId DESC")
    Page<Users> searchAll(
            @Param("search") String search,
            @Param("status") Users.Status status,
            Pageable pageable
    );


}
