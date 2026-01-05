package com.in.mzncvmc.content.userOtp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_otp",
        indexes = {
                @Index(name = "idx_user_id", columnList = "user_id")
        }
)
@Getter
@Setter
public class UserOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "otp_code", length = 6, nullable = false)
    private String otpCode;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Column(name = "used_yn", length = 1)
    private String usedYn = "N";

    @Column(name = "fail_count")
    private Integer failCount = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 비즈니스 메서드(Y: 사용됨, N: 미사용)
    public void markUsed() {
        this.usedYn = "Y";
    }

    // OTP 인증 실패 횟수
    public void increaseFailCount() {
        this.failCount++;
    }
    // OTP 만료 시각
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
    }
}
