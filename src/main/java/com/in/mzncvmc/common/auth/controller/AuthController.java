package com.in.mzncvmc.common.auth.controller;

import com.in.mzncvmc.common.auth.dto.LoginRequest;
import com.in.mzncvmc.common.auth.dto.LoginResponse;
import com.in.mzncvmc.common.auth.dto.RegisterRequest;
import com.in.mzncvmc.common.auth.util.JwtUtil;
import com.in.mzncvmc.content.userHistory.UserHistoryService;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
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

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserHistoryService userHistoryService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
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

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(loginRequest.getUsername());
        final String accessToken = jwtUtil.generateToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        // 🔑 userId 조회 (예: 커스텀 User 엔티티)
        Users users = usersService.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        LoginResponse response = new LoginResponse(
                accessToken,
                refreshToken,
                users.getUserId(),
                userDetails.getUsername(),
                "Bearer"
        );

        // 로그인 성공시 히스토리 저장
        userHistoryService.saveLogin(loginRequest.getUsername(), getClientIp(request));

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
                String userId = jwtUtil.extractUsername(actualToken);
                userHistoryService.saveLogout(userId, getClientIp(request));


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


    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}