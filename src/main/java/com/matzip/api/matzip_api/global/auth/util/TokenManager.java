package com.matzip.api.matzip_api.global.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenManager {
    private final JwtTokenProvider jwtTokenProvider;
    public void validateToken(String token) {
        jwtTokenProvider.validateToken(token);
    }

    public void issueTokens(HttpServletResponse response, String username) {
        // JWT 생성
        String accessToken = jwtTokenProvider.createJwt("access", username);
        String refreshToken = jwtTokenProvider.createJwt("refresh", username);

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
}
