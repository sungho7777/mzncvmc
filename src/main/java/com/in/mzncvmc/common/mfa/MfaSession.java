package com.in.mzncvmc.common.mfa;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

/*
    MFA 진행 중인 임시 세션 (Redis 사용)
*/
@Getter
@Setter
@RedisHash(value = "mfa_session", timeToLive = 300) // 5분
public class MfaSession {
    @Id
    private String sessionId; // UUID(임시 로그인 번호표)
    private Long userId; // 사용자 아이디
    private String username; // JWT 만들 때 필요
    private LocalDateTime createdAt; // 생성 시간
    private boolean passwordVerified; // 비번 통과했는지
    private boolean otpVerified; // OTP 통과했는지
    private String ipAddress; // 세션 탈취 방지용
    private String userAgent; // 브라우저 정보

}
