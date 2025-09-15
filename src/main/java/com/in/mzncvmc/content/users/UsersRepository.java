package com.in.mzncvmc.content.users;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;        // Page<T>
import org.springframework.data.domain.Pageable;   // Pageable
import org.springframework.data.domain.PageRequest; // PageRequest.of()
import org.springframework.data.domain.Sort;        // 정렬 옵션

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

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

    @Modifying
    @Query("UPDATE Users u SET u.connected = :connected WHERE u.username = :username")
    int updateUsersConnected(String username, Users.Connected connected);

    @Modifying
    @Query("UPDATE Users u SET u.connected = :connected")
    int updateAllUsersConnected(Users.Connected connected);
}
