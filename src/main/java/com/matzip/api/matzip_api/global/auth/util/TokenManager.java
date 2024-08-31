package com.matzip.api.matzip_api.global.auth.util;

import com.matzip.api.matzip_api.global.auth.service.RefreshTokenService;
import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.exception.CustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenManager {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public void validateToken(String token) {
        jwtTokenProvider.validateToken(token);
    }

    public void issueTokens(HttpServletResponse response, String username) {
        // JWT 생성
        String accessToken = jwtTokenProvider.createJwt("access", username);
        String refreshToken = jwtTokenProvider.createJwt("refresh", username);

        // Redis에 Refresh Token 저장
        saveRefreshToken(username, refreshToken);

        // 응답에 토큰 추가
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie("refresh", refreshToken));

        response.setStatus(HttpStatus.OK.value());
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60); // 하루 동안 유효
        cookie.setHttpOnly(true);
        return cookie;
    }

    public boolean isAccessToken(String accessToken) {
        return jwtTokenProvider.getCategory(accessToken).equals("access");
    }

    public boolean isRefreshToken(String refreshToken) {
        return jwtTokenProvider.getCategory(refreshToken).equals("refresh");
    }

    public String getUsername(String token) {
        return jwtTokenProvider.getUsername(token);
    }

    // Refresh Token Redis에 저장
    private void saveRefreshToken(String username, String refreshToken) {
        try {
            refreshTokenService.saveRefreshToken(username, refreshToken); // Redis에 저장
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_SERVER_ERROR);
        }
    }

    public void deleteRefreshToken(String refreshToken) {
        try {
            refreshTokenService.deleteRefreshToken(refreshToken); // Redis에 삭제
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_SERVER_ERROR);
        }
    }

    // Refresh Token 검증 메서드
    public void validateRefreshToken(String refreshToken) {
        String username = jwtTokenProvider.getUsername(refreshToken);
        // Redis에 저장된 Refresh Token과 비교
        String storedRefreshToken = refreshTokenService.getRefreshToken(username);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

}
