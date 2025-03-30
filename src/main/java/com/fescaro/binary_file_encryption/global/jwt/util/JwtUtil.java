package com.fescaro.binary_file_encryption.global.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// JWTUtil 0.12.3
/**
 * <reference>
 * https://sjh9708.tistory.com/170
 * https://www.youtube.com/playlist?list=PLJkjrxxiBSFCcOjy0AAVGNtIa08VLk1EJ
 */
@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long tokenExpTime; // access token expiration time

    // Secret Key
    public JwtUtil(@Value("${spring.jwt.secret}") String secret,
                   @Value("${spring.jwt.expiration_time}") long tokenExpTime) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.tokenExpTime = tokenExpTime;
    }

    public String getUsernameFromJwt(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            if (!isExpired(jwt)) {
                return getUsername(jwt);
            } else {
                throw new RuntimeException("토큰이 만료되었습니다.");
            }
        } else {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }

    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // JWT 복호화
                .build() // 복호화에 사용할 Jwts Parser 생성
                // 생성된 Parser를 사용하여 token으로 부터 정보 추출
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        return getAllClaims(token).get("username", String.class);
    }

    public String getRole(String token) {
        return getAllClaims(token).get("role", String.class);
    }


    // 토큰 만료여부 확인
    public Boolean isExpired(String token) {
        return getAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    public String createJwt(String username, String role) {
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExpTime))
                .signWith(secretKey)
                .compact();
    }
}

