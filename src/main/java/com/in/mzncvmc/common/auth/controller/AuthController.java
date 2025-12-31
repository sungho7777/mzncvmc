package com.in.mzncvmc.common.auth.controller;

import com.in.mzncvmc.common.auth.dto.LoginRequest;
import com.in.mzncvmc.common.auth.dto.LoginResponse;
import com.in.mzncvmc.common.auth.dto.RegisterRequest;
import com.in.mzncvmc.common.auth.util.JwtUtil;
import com.in.mzncvmc.content.userHistory.UserHistoryService;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersDto;
import com.in.mzncvmc.content.users.UsersRepository;
import com.in.mzncvmc.content.users.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserHistoryService userHistoryService;

    @Autowired
    private PasswordEncoder passwordEncoder;
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

    @Value("${user.first.password}")
    private String userFirstPassword;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        // 1. 사용자 엔티티 조회
        Users users = usersService.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
        // 2. 접속 상태 확인
        if (users.getConnected() == Users.Connected.Y) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "이미 다른 곳에서 접속 중입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error); // 409 Conflict
        }
        // 3. 인증 시도
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        // 4. 토큰 생성
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String accessToken = jwtUtil.generateToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        // 5. 로그인 성공 → 상태 Y 로 변경
        usersService.updateUsersConnected(loginRequest.getUsername(), "Y");
        // 6. 히스토리 저장
        userHistoryService.saveLogin(loginRequest.getUsername(), getClientIp(request));
        // 7. 응답 리턴
        LoginResponse response = new LoginResponse(
                accessToken,
                refreshToken,
                users.getUserId(),
                userDetails.getUsername(),
                users.getPwNotifyDuration(), // 사용자 최초 접속 여부
                "Bearer"
        );

        return ResponseEntity.ok(response);
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
                userHistoryService.saveLogout(username, getClientIp(request));

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
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        log.debug("resetPassword. : " + loginRequest.getUsername());

        authService.resetPassword(loginRequest.getUsername(), userFirstPassword);

        return ResponseEntity.ok().build();
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}