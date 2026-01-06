package com.in.mzncvmc.content.userMfa;

import com.in.mzncvmc.content.users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMfaRepository extends JpaRepository<UserMfa, Long> {

    Optional<UserMfa> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    void deleteByUserId(Long userId);



    /**
     * 데이터 목록 조회
     *
     * @param search 조회할 데이터
     * @return Optional<Vo> 데이터 엔티티
     */
    @Query("SELECT u " +
            " FROM UserMfa u " +
            "WHERE (:search IS NULL OR :search = '' " +
            "       OR u.mfaSecret LIKE CONCAT('%', :search, '%') " +
            ") " +
            "ORDER BY u.userId DESC")
    Page<UserMfa> searchAll(
            @Param("search") String search,
            Pageable pageable
    );
}
