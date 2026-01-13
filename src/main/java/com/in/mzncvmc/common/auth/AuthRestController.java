package com.in.mzncvmc.common.auth;

import com.in.mzncvmc.common.login.LoginRequest;
import com.in.mzncvmc.common.system.jwt.JwtUtil;
import com.in.mzncvmc.common.system.response.ApiResponse;
import com.in.mzncvmc.common.system.service.AuthService;
import com.in.mzncvmc.common.system.service.MfaService;
import com.in.mzncvmc.content.userHistory.UserHistoryService;
import com.in.mzncvmc.content.userMfa.UserMfa;
import com.in.mzncvmc.content.userMfa.UserMfaService;
import com.in.mzncvmc.content.userMfa.UserMfaVerifyDto;
import com.in.mzncvmc.content.userOtp.UserOtpService;
import com.in.mzncvmc.content.userOtp.UserOtpVerifyDto;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.in.mzncvmc.common.system.constants.CommonConstants.SLASH_API;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(SLASH_API + "/auth")
public class AuthRestController {
    @Value("${user.otp.use}")
    private String userOtpUse;
    @Value("${user.mfa.use}")
    private String userMfaUse;

    @Autowired
    private UserHistoryService userHistoryService;
    @Autowired
    private UserOtpService userOtpService;
    @Autowired
    private UserMfaService userMfaService;
    @Autowired
    private MfaService mfaService;
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 사용자 로그인 시도
     *
     * @param request 사용자 아이디, 패스워드
     *        request
     * @return 로그인 LoginResponse
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        Optional<Users> optional = usersService.findByUsername(request.getUsername());
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "The user does not exist."));
        }
        Users users = optional.get();

        // 확인
        if (users.getConnected() == Users.Connected.Y) {
            return authService.returnFallLogin(users, httpRequest, "The user is already logged in elsewhere.");
        }

        // 인증
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return authService.returnFallLogin(users, httpRequest, "Invalid username or password");
        }

        // 설정에 의해 분기처리.
        if(users.getPwNotifyDuration().equals("999")){
            // 신규 사용자 일단 로그인 시켜서 비밀번호변경 하도록 한다.
            // 사용자 로그인 성공 및 토큰 처리
            // statusLogin == 'success'
            return authService.returnSuccessLogin(users, httpRequest, "Login successful. You are a new member. Please change your password.");
        }else{
            if(users.getUserId() == 1L){
                // TODO 테스트를 위해 [admin] 계정은 무조건 통과
            }else{
                // 사용자의 구글 TOTP 설정 여부를 확인 한다.
                boolean isMfaEnabled = userMfaService.isMfaEnabled(users.getUserId());
                if(isMfaEnabled){
                    // true → MFA 활성화
                    if(userMfaUse.equals("Y")){
                        if(userOtpUse.equals("Y")) {
                            // OTP 생성 + 저장
                            // statusLogin == 'loginUserOtp'
                            return authService.returnLoginUserOtp(users, httpRequest);
                        }
                        // statusLogin == 'loginUserMfa'
                        return authService.returnLoginUserMfa(users, httpRequest);
                    }
                }else{
                    // false → MFA 비활성화 또는 설정 정보 없음 -> 설정 시작(이메일 OTP, 구글 TOTP)
                    // statusLogin == 'generateUserOtp'
                    return authService.returnGenerateUserOtp(users, httpRequest);
                }

            }
            // 사용자 로그인 성공 및 토큰 처리
            // statusLogin == 'success'
            return authService.returnSuccessLogin(users, httpRequest, "Login successful");
        }
    }

    /**
     * 사용자 MFA 설정을 위한 선작업 메일 OTP 인증
     *
     * @param request 사용자 아이디, Otp Code
     *        request
     * @return 로그인 LoginResponse
     */
    @PostMapping("/generateUserOtp")
    public ApiResponse generateUserOtp(@RequestBody UserOtpVerifyDto request, HttpServletRequest httpRequest) {
        Optional<Users> optional = usersService.findByUsername(request.getUsername());
        if (optional.isEmpty()) {
            // "사용자가 존재하지 않습니다."
            return ApiResponse.fail("The user does not exist.");
        }
        Users users = optional.get();

        // OTP 검증
        return userOtpService.verifyUserOtp(users.getUserId(), request.getOtpCode());
    }

    /**
     * 사용자 로그인 구글 TOTP 설정을 위한 QR 코드(이미지) 전송
     *
     * @param request 사용자 Username
     *        request
     * @return response
     */
    @PostMapping("/generateQR")
    public ApiResponse<Map<String, Object>> generateQRCode(@RequestBody UserMfaVerifyDto request) throws Exception {
        Optional<Users> optional = usersService.findByUsername(request.getUsername());
        if (optional.isEmpty()) {
            // "사용자가 존재하지 않습니다."
            return ApiResponse.fail("The user does not exist.");
        }
        Users users = optional.get();

        Long userId = users.getUserId();
        String username = users.getUsername();

        String secret = userMfaService.initiateAndStoreMFASecret(userId);
        String qrCodeBase64 = mfaService.generateQRCodeBase64(username, secret);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("qrCode", "data:image/png;base64," + qrCodeBase64);

        // 사용자는 QR 코드를 사용하여 MFA 인증을 완료하세요.
        return ApiResponse.success(response, "Users complete MFA authentication using a QR code.");
    }

    /**
     * 사용자 로그인 구글 TOTP 설정을 위한 코드 확인.
     *
     * @param request 사용자 아이디, Mfa Code
     *        request
     * @return 로그인 LoginResponse
     */
    @PostMapping("/generateUserMfa")
    public ApiResponse<?> generateUserMfa(@RequestBody UserMfaVerifyDto request, HttpServletRequest httpRequest) throws Exception {
        Optional<Users> optional = usersService.findByUsername(request.getUsername());
        if (optional.isEmpty()) {
            // "사용자가 존재하지 않습니다."
            return ApiResponse.fail("The user does not exist.");
        }
        Users users = optional.get();

        Long userId = users.getUserId();
        String email = users.getEmail();
        String mfaCode =request.getMfaCode();
        //String mfaType =request.getMfaType(); // "mfaCodeRadio", "backupCodeRadio"

        // 구글 TOTP 코드 [초기 설정]을 위한 검증_generate
        return userMfaService.verifyAndEnableMFA(userId, mfaCode, email, "generate");
    }

    /**
     * 사용자 로그인 이메일 OTP 코드 확인 후 로그인 처리
     *
     * @param request 사용자 아이디, Otp Code
     *        request
     * @return 로그인 LoginResponse
     */
    @PostMapping("/verifyUserOtp")
    public ResponseEntity<?> verifyUserOtp(@RequestBody UserOtpVerifyDto request, HttpServletRequest httpRequest) {
        Optional<Users> optional = usersService.findByUsername(request.getUsername());
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "The user does not exist."));
        }
        Users users = optional.get();

        // OTP 검증
        ApiResponse apiResponse = userOtpService.verifyUserOtp(users.getUserId(), request.getOtpCode());

        return (apiResponse.getStatus().equals("success")) ?
                authService.returnSuccessLogin(users, httpRequest, apiResponse.getMessage()):
                authService.returnFallLogin(users, httpRequest, apiResponse.getMessage());
    }

    /**
     * 사용자 로그인 구글 TOTP 코드 확인 후 로그인 처리
     *
     * @param request 사용자 아이디, Mfa Code
     *        request
     * @return 로그인 LoginResponse
     */
    @PostMapping("/verifyUserMfa")
    public ResponseEntity<?> verifyUserMfa(@RequestBody UserMfaVerifyDto request, HttpServletRequest httpRequest) throws Exception {
        Optional<Users> optional = usersService.findByUsername(request.getUsername());
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "The user does not exist."));
        }
        Users users = optional.get();

        Long userId = users.getUserId();
        String email = users.getEmail();
        String mfaCode =request.getMfaCode();
        String mfaType =request.getMfaType(); // "mfaCodeRadio", "backupCodeRadio"

        // 구글 TOTP 코드 [로그인]을 위한 검증_verify
        ApiResponse apiResponse =null;
        if(mfaType.equals("mfaCodeRadio")){
            apiResponse = userMfaService.verifyAndEnableMFA(userId, mfaCode, email, "verify");

        }else if(mfaType.equals("backupCodeRadio")){
            apiResponse = userMfaService.verifyBackupCode(userId, mfaCode, email);
        }

        return (apiResponse.getStatus().equals("success")) ?
                authService.returnSuccessLogin(users, httpRequest, apiResponse.getMessage()):
                authService.returnFallLogin(users, httpRequest, apiResponse.getMessage());
    }

    /**
     * 사용자 토큰 refresh
     *
     * @param request 사용자 token
     * @return 로그아웃 response message
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid refresh token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.generateToken(userDetails);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("tokenType", "Bearer");

        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 로그아웃 처리
     *
     * @param request 사용자 token
     * @return 로그아웃 response message
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            String actualToken = token.substring(7);
            try {
                String username = jwtUtil.extractUsername(actualToken);
                userHistoryService.saveLogout(username, authService.getClientIp(request));

                // 로그인 접속상태 Y 으로 업데이트
                usersService.updateUsersConnected(username, "N");
            } catch (Exception e) {
                // 토큰이 유효하지 않아도 로그아웃은 성공으로 처리
            }
        }
        // JWT는 stateless이므로 서버에서 토큰을 무효화할 수 없음
        // 클라이언트에서 토큰을 삭제하도록 응답
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");

        return ResponseEntity.ok(response);
    }
}