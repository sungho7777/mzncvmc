package com.in.mzncvmc.common.otp;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerificationDto {
    private Long otpId; // OTP 고유 ID
    private Integer userId; // 사용자 ID (FK: users.user_id)
    private String otpCode; // 6자리 OTP 코드
    private String otpType; // OTP 유형(EMAIL, SNS)
    private Integer attemptCount; // 시도 횟수
    private Integer maxAttempts; // 최대 시도횟수
    private Boolean isVerified; // 인증 완료 여부
    private Boolean isExpired; // 만료 여부
    private LocalDateTime createdAt; // 생성 시각
    private LocalDateTime expiresAt; // 만료 시각
    private LocalDateTime verifiedAt; // 인증 완료 시각
    private String ipAddress; // 요청 IP 주소
    private String userAgent; // 사용자 에이전트
}
