package com.in.mzncvmc.content.userOtp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {

    /**
     * Otp 데이터 단일 조회
     *
     * @param userId 사용자 아이디
     * @return Optional<Vo> 데이터 엔티티
     */
    @Query("""
        SELECT u
          FROM UserOtp u
         WHERE u.userId = :userId
         ORDER BY u.createdAt DESC
         LIMIT 1
    """)
    Optional<UserOtp> findTopByUserIdOrderByCreatedAtDescLimit1(Long userId);

    @Modifying
    @Query("""
        DELETE FROM UserOtp o
        WHERE o.expiredAt < :threshold
    """)
    int deleteExpiredOtp(@Param("threshold") LocalDateTime threshold);
}
