package com.in.mzncvmc.common.mfa;

import com.in.mzncvmc.common.auth.dto.LoginRequest;
import com.in.mzncvmc.common.otp.OtpService;
import com.in.mzncvmc.common.otp.OtpVerificationDto;
import com.in.mzncvmc.content.users.Users;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class MfaRestController {

    //@Autowired
    //private AuthenticationService authService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private MfaSessionService mfaSessionService;



    /**
     * 1단계: 로그인 (비밀번호 검증)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        // 비밀번호 검증
        Users user = authService.authenticate(request.getUsername(), request.getPassword());

        String ipAddress = getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        // MFA 세션 생성
        String mfaSessionId = mfaSessionService.createMfaSession(user, ipAddress, userAgent);

        // OTP 생성 및 이메일 발송
        otpService.generateAndSendOtp(user, ipAddress, userAgent);

        return ResponseEntity.ok(new MfaRequiredResponse(mfaSessionId));
    }

    /**
     * 2단계: OTP 검증
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request, HttpServletRequest httpRequest) {

        // MFA 세션 검증
        MfaSession session = mfaSessionService.validateMfaSession(request.getMfaSessionId());

        String ipAddress = getClientIp(httpRequest);

        // OTP 검증
        OtpVerificationDto result = otpService.verifyOtp(
                session.getUserId(),
                request.getOtpCode(),
                ipAddress
        );

        if (result.isSuccess()) {
            // JWT 발급
            String jwt = mfaSessionService.completeMfaAndGenerateJwt(request.getMfaSessionId());

            // 마지막 로그인 시각 업데이트
            authService.updateLastLogin(session.getUserId());

            return ResponseEntity.ok(new LoginSuccessResponse(jwt));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new OtpVerifyErrorResponse(result));
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
