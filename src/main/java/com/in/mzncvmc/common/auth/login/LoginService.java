package com.in.mzncvmc.common.auth.login;

import com.in.mzncvmc.common.system.jwt.JwtUtil;
import com.in.mzncvmc.common.system.mail.MailService;
import com.in.mzncvmc.common.system.response.ApiResponse;
import com.in.mzncvmc.common.system.util.ClientUtil;
import com.in.mzncvmc.common.system.util.CookieUtil;
import com.in.mzncvmc.content.userHistory.UserHistoryService;
import com.in.mzncvmc.content.userOtp.UserOtpService;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class LoginService {
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
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private ClientUtil clientUtil;

    public ResponseEntity<?> returnGenerateUserOtp(
            Users users,
            HttpServletRequest httpRequest) {

        String otp = userOtpService.generateUserOtp();
        userOtpService.createUserOtp(users.getUserId(), otp);

        // 이메일 발송
        mailService.sendGenerateUserOtp(users.getEmail(), otp);

        return ResponseEntity.ok(
                new LoginResponse(
                        "generateUserOtp",
                        "",
                        "",
                        users.getUserId(),
                        users.getUsername(),
                        users.getPwNotifyDuration(),
                        "",
                        "설정을 위한 인증 코드가 이메일로 전송되었습니다."
                )
        );
    }


    /**
     * 사용자 이메일 OTP 생성 + 저장 > 팝업
     *
     * @param users 사용자 정보
     *        httpRequest
     * @return 로그인 LoginResponse
     */
    public ResponseEntity<?> returnLoginUserOtp(
            Users users,
            HttpServletRequest httpRequest) {

        String otp = userOtpService.generateUserOtp();
        userOtpService.createUserOtp(users.getUserId(), otp);

        // 이메일 발송
        mailService.sendUserOtpMail(users.getEmail(), otp);

        return ResponseEntity.ok(
                new LoginResponse(
                        "loginUserOtp",
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
     * 사용자 구글 TOTP 생성 + 저장 > 팝업
     *
     * @param users 사용자 정보
     *        httpRequest
     * @return 로그인 LoginResponse
     */
    public ResponseEntity<?> returnLoginUserMfa(
            Users users,
            HttpServletRequest httpRequest) {

        return ResponseEntity.ok(
                new LoginResponse(
                        "loginUserMfa",
                        "",
                        "",
                        users.getUserId(),
                        users.getUsername(),
                        users.getPwNotifyDuration(),
                        "",
                        "loginUserMfa"
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
            HttpServletResponse response,
            String message) {

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(users.getUsername());

        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        usersService.updateUsersConnected(users.getUsername(), "Y");
        userHistoryService.saveLogin(
                users.getUsername(),
                clientUtil.getClientIp(httpRequest)
        );

        cookieUtil.insertAccessTokenCookie("accessToken", accessToken, response);

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
     * 사용자 로그아웃 성공 및 토큰 처리
     *
     * @param
     * @return ApiResponse
     */
    public ApiResponse<?> returnSuccessLogout(HttpServletResponse response) {

        cookieUtil.deleteAccessTokenCookie("accessToken", response);
        cookieUtil.deleteAccessTokenCookie("refreshToken", response);

        return ApiResponse.success(true,"Logged out successfully");
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

}
