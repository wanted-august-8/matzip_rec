package com.matzip.api.matzip_api.global.auth.service;

import com.matzip.api.matzip_api.domain.user.entity.User;
import com.matzip.api.matzip_api.domain.user.repository.UserRepository;
import com.matzip.api.matzip_api.global.auth.dto.SignUpRequestDto;
import com.matzip.api.matzip_api.global.auth.util.TokenManager;
import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.exception.CustomException;
import com.matzip.api.matzip_api.global.util.PasswordManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordManager passwordManager;
    private final TokenManager tokenManager;

    public long signUp(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByAccount(signUpRequestDto.getAccount())) {
            throw new CustomException(ErrorCode.ACCOUNT_ALREADY_REGISTERED);
        }

        return userRepository.save(
            User.builder()
                .account(signUpRequestDto.getAccount())
                .password(passwordManager.encodePassword(signUpRequestDto.getPassword()))
                .build()
        ).getId();
    }

    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("refresh")).findFirst().orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)).getValue();

        tokenManager.validateToken(refreshToken);

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        if (!tokenManager.isRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        tokenManager.validateRefreshToken(refreshToken);
        String username = tokenManager.getUsername(refreshToken);
        Long userId = tokenManager.getUserId(refreshToken);
        tokenManager.deleteRefreshToken(refreshToken);
        tokenManager.issueTokens(response, username, userId);
    }
}
