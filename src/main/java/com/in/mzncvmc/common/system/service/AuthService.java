package com.in.mzncvmc.common.system.service;

import com.in.mzncvmc.common.login.LoginResponse;
import com.in.mzncvmc.common.system.jwt.JwtUtil;
import com.in.mzncvmc.common.system.mail.MailService;
import com.in.mzncvmc.content.userHistory.UserHistoryService;
import com.in.mzncvmc.content.userOtp.UserOtpService;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserHistoryService userHistoryService;
    @Autowired
    private UserOtpService userOtpService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JwtUtil jwtUtil;


    /**
     * 사용자 OTP 생성 + 저장 > 팝업
     *
     * @param users 사용자 정보
     *        httpRequest
     * @return 로그인 LoginResponse
     */
    public ResponseEntity<?> returnUserOtpMail(
            Users users,
            HttpServletRequest httpRequest) {

        String otp = userOtpService.generateUserOtp();
        userOtpService.createUserOtp(users.getUserId(), otp);

        // 이메일 발송
        mailService.sendUserOtpMail(users.getEmail(), otp);

        return ResponseEntity.ok(
                new LoginResponse(
                        "sendUserOtpMail",
                        "",
                        "",
                        users.getUserId(),
                        users.getUsername(),
                        users.getPwNotifyDuration(),
                        "",
                        "인증 코드가 이메일로 전송되었습니다."
                )
        );
    }

    /**
     * 사용자 QR 코드 이미지 생성
     *
     * @param users 사용자 정보
     *        httpRequest
     * @return 로그인 LoginResponse
     */
    public ResponseEntity<?> returnGenerateQRCode(
            Users users,
            HttpServletRequest httpRequest) {


        return ResponseEntity.ok(
                new LoginResponse(
                        "generateQRCode",
                        "",
                        "",
                        users.getUserId(),
                        users.getUsername(),
                        users.getPwNotifyDuration(),
                        "",
                        "QR 코드 생성 API."
                )
        );
    }
    /**
     * 사용자 로그인 성공 및 토큰 처리
     *
     * @param users 사용자 정보
     *        httpRequest
     * @return 로그인 LoginResponse
     */
    public ResponseEntity<?> returnSuccessLogin(
            Users users,
            HttpServletRequest httpRequest,
            String message) {

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(users.getUsername());

        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        usersService.updateUsersConnected(users.getUsername(), "Y");
        userHistoryService.saveLogin(
                users.getUsername(),
                getClientIp(httpRequest)
        );

        return ResponseEntity.ok(
                new LoginResponse(
                        "success",
                        accessToken,
                        refreshToken,
                        users.getUserId(),
                        users.getUsername(),
                        users.getPwNotifyDuration(),
                        "Bearer",
                        message // << VerificationResponse
                )
        );
    }
    /**
     * 사용자 로그인 실패처리
     *
     * @param users 사용자 정보
     *        httpRequest
     * @return 로그인 LoginResponse
     */
    public ResponseEntity<?> returnFallLogin(
            Users users,
            HttpServletRequest httpRequest,
            String message) {

        return ResponseEntity.ok(
                new LoginResponse(
                        "fall",
                        "",
                        "",
                        users.getUserId(),
                        users.getUsername(),
                        users.getPwNotifyDuration(),
                        "",
                        message // << VerificationResponse
                )
        );
    }

    /**
     * 사용자 클라이언트 IP 가져온다.
     *
     * @param request
     * @return 로그인 Remote Addr
     */
    public String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
