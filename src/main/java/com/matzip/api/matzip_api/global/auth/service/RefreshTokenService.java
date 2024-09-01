package com.matzip.api.matzip_api.global.auth.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.refresh-token-validate-in-seconds}") // Refresh Token 만료 시간
    private long refreshTokenExpiration;

    // Refresh Token을 Redis에 저장
    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set(
            getRefreshTokenKey(username),  // Redis에 저장할 키
            refreshToken,                  // 저장할 Refresh Token 값
            refreshTokenExpiration,        // 만료 시간
            TimeUnit.MILLISECONDS          // 시간 단위
        );
    }

    // Refresh Token을 Redis에서 조회
    public String getRefreshToken(String username) {
        return (String) redisTemplate.opsForValue().get(getRefreshTokenKey(username));
    }

    // Refresh Token을 Redis에서 삭제
    public void deleteRefreshToken(String username) {
        redisTemplate.delete(getRefreshTokenKey(username));
    }

    // Redis에 저장할 Refresh Token 키 생성
    private String getRefreshTokenKey(String username) {
        return "refreshToken:" + username;
    }
}
