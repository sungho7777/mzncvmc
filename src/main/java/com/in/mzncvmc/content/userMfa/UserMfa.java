package com.in.mzncvmc.content.userMfa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_mfa")
public class UserMfa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId; // 기존 User 테이블의 ID 참조

    @Column(name = "mfa_enabled", nullable = false)
    private boolean mfaEnabled = false;

    @Column(name = "mfa_secret", columnDefinition = "TEXT")
    private String mfaSecret; // AES 암호화되어 저장

    @Column(name = "mfa_verified", nullable = false)
    private boolean mfaVerified = false;

    @Column(name = "backup_codes", columnDefinition = "TEXT")
    private String backupCodes; // 선택사항: 백업 코드 (JSON 또는 암호화)

    @Column(name = "failed_attempts", nullable = false)
    private int failedAttempts = 0; // 실패 횟수

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil; // 잠금 해제 시간

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_verified_at")
    private LocalDateTime lastVerifiedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }




}
