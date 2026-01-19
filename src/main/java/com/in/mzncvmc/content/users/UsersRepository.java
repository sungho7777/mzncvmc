package com.in.mzncvmc.content.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    boolean existsByUsername(String username);
    //
    boolean existsByEmail(String email);
    //
    Optional<Users> findByProviderAndProviderId(String provider, String providerId);



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
    @Query("""
        UPDATE Users u
           SET u.connected = :connected
               , u.lastLogin = CASE WHEN :connected = 'Y' THEN CURRENT_TIMESTAMP
                             ELSE u.lastLogin
                              END
         WHERE u.username = :username
    """)
    int updateUsersConnected(String username, Users.Connected connected);

    @Modifying
    @Query("UPDATE Users u SET u.connected = :connected")
    int updateAllUsersConnected(Users.Connected connected);

    @Modifying
    @Query("""
        UPDATE Users u
           SET u.password = :password
               , u.pwNotifyDuration = '10'
         WHERE u.username = :username
    """)
    int updatePassword(
            @Param("username") String username,
            @Param("password") String password
    );

    @Modifying
    @Query("""
        UPDATE Users u
           SET u.password = :password
               , u.pwNotifyDuration = '999'
         WHERE u.username = :username
    """)
    int resetPassword(
            @Param("username") String username,
            @Param("password") String password
    );
}
