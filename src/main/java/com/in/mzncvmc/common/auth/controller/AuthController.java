package com.in.mzncvmc.common.auth.controller;

import com.in.mzncvmc.common.auth.dto.LoginRequest;
import com.in.mzncvmc.common.auth.dto.RegisterRequest;
import com.in.mzncvmc.common.auth.service.AuthService;
import com.in.mzncvmc.common.auth.util.JwtUtil;
import com.in.mzncvmc.content.userHistory.UserHistoryService;
import com.in.mzncvmc.content.userOtp.UserOtpService;
import com.in.mzncvmc.content.userOtp.UserOtpVerifyDto;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

import static com.in.mzncvmc.content.common.constants.CommonConstants.SLASH_API;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(SLASH_API + "/auth")
public class AuthController {
    @Value("${user.otp.use}")
    private String userOtpUse;

    @Autowired
    private UserHistoryService userHistoryService;
    @Autowired
    private UserOtpService userOtpService;
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

        Users users = usersService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (users.getConnected() == Users.Connected.Y) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "이미 다른 곳에서 접속 중입니다."));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }

        if(userOtpUse.equals("Y")){
            // OTP 생성 + 저장
            return authService.returnUserOtpMail(users, httpRequest);
        }else{
            // 사용자 로그인 성공 및 토큰 처리
            return authService.returnSuccessLogin(users, httpRequest);
        }
    }
    /**
     * 사용자 로그인 이메일 OTP 코드 확인 후 로그인 처리
     *
     * @param request 사용자 아이디, Otp Code
     *        request
     * @return 로그인 LoginResponse
     */
    @PostMapping("/login/verifyUserOtp")
    public ResponseEntity<?> verifyUserOtp(@RequestBody UserOtpVerifyDto request, HttpServletRequest httpRequest) {
        Users users = usersService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        // OTP 검증
        userOtpService.verifyUserOtp(users.getUserId(), request.getOtp());

        return authService.returnSuccessLogin(users, httpRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            Users users = usersService.createUser(
                    registerRequest.getUsername(),
                    registerRequest.getEmail(),
                    registerRequest.getPassword()
            );

            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("username", users.getUsername());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

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