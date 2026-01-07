package com.in.mzncvmc.common.system.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    private String runtimeSecret;

    @PostConstruct
    public void init() {
        // 서버 재기동 시마다 새로운 키 생성
        runtimeSecret = Base64.getEncoder()
                .encodeToString(UUID.randomUUID().toString().getBytes());
        System.out.println("🔑 JWT Runtime Secret: " + runtimeSecret);
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(runtimeSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), refreshExpiration);
    }

    private String createToken(Map<String, Object> claims, String subject, Long expiration) {

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}