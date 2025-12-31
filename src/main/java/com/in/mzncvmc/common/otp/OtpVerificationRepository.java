package com.in.mzncvmc.common.otp;

import com.in.mzncvmc.content.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<Users, Long> {

    /*
        특정 사용자의 인증 OTP정보를 조회 한다.
        (인증 완료 여부, 만료 여부가 유효 한 것.)
    */
    @Query("""
        SELECT o 
          FROM OtpVerification o 
         WHERE o.userId = :userId
           AND o.isVerified = false 
           AND o.isExpired = false
         ORDER BY o.createdAt DESC
    """)
    Optional<OtpVerification> findLatestUnverifiedOtp(@Param("userId") int userId);

    /*
        특정 사용자의 미인증 OTP를 모두 만료 처리한다.
        (신규 OTP 발급 전 기존 OTP 무효화 목적)
    */
    @Modifying
    @Query("""
        UPDATE OtpVerification o 
           SET o.isExpired = true
         WHERE o.userId = :userId AND o.isVerified = false AND o.isExpired = false
    """)
    void expireUnverifiedOtps(@Param("userId") int userId);

    /*
        정기적 정리용 (스케줄러에서 사용)
    */
    @Modifying
    @Query("""
        DELETE 
          FROM OtpVerification o 
         WHERE o.expiresAt < :threshold
    """)
    void deleteExpiredOtps(@Param("threshold") LocalDateTime threshold);
}
