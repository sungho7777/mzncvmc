package com.in.mzncvmc.common.mfa;

import com.in.mzncvmc.content.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MfaSessionService {

    @Autowired
    private RedisTemplate<String, MfaSession> redisTemplate;

    /**
     * 1단계: 비밀번호 인증 성공 후
     */
    public String createMfaSession(Users user, String ipAddress, String userAgent) {
        String sessionId = UUID.randomUUID().toString();

        MfaSession session = new MfaSession();
        session.setSessionId(sessionId);
        session.setUserId(user.getUserId());
        session.setUsername(user.getUsername());
        session.setPasswordVerified(true);
        session.setOtpVerified(false);
        session.setIpAddress(ipAddress);
        session.setUserAgent(userAgent);
        session.setCreatedAt(LocalDateTime.now());

        redisTemplate.opsForValue().set("mfa:" + sessionId, session, 5, TimeUnit.MINUTES);

        return sessionId;
    }
    /**
     * 2단계: OTP 인증 성공 후
     */
    public String completeMfaAndGenerateJwt(String sessionId) {
        MfaSession session = redisTemplate.opsForValue().get("mfa:" + sessionId);

        if (session == null || !session.isPasswordVerified()) {
            throw new RuntimeException("session is null");
        }

        // OTP 인증 완료 표시
        session.setOtpVerified(true);

        // JWT 생성해서 return 할 것.
        /*
        String jwt = jwtTokenProvider.createToken(
                session.getUserId(),
                session.getUsername()
        );*/

        // MFA 세션 삭제
        redisTemplate.delete("mfa:" + sessionId);

        return null; //jwt;
    }
    /**
     * MFA 세션 검증
     */
    public MfaSession validateMfaSession(String sessionId) {
        MfaSession session = redisTemplate.opsForValue().get("mfa:" + sessionId);

        if (session == null) {
            throw new RuntimeException("session is null");
        }

        return session;
    }
}
