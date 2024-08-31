package com.matzip.api.matzip_api.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.api.matzip_api.global.auth.domain.CustomUserDetails;
import com.matzip.api.matzip_api.global.auth.dto.LoginRequestDto;
import com.matzip.api.matzip_api.global.auth.util.JwtTokenProvider;
import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.error.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login"; // "/login"으로 오는 요청을 처리
    private static final String HTTP_METHOD = HttpMethod.POST.name(); // 로그인 HTTP 메소드는 POST
    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE; // JSON 타입의 데이터로 오는 로그인 요청만 처리
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
        DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); // "/login" + POST로 온 요청에 매칭

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager,
        JwtTokenProvider jwtTokenProvider) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER); // 위에서 설정한 "login" + POST로 온 요청을 처리하기 위해 설정
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException, IOException {
        if (!Objects.equals(request.getContentType(), CONTENT_TYPE)) {
            throw new AuthenticationServiceException(
                "지원하지 않는 Content-Type 입니다: " + request.getContentType());
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(),
            StandardCharsets.UTF_8);
        LoginRequestDto loginRequestDto = objectMapper.readValue(messageBody,
            LoginRequestDto.class);

        Set<ConstraintViolation<LoginRequestDto>> violations = Validation.buildDefaultValidatorFactory()
            .getValidator().validate(loginRequestDto);

        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
            throw new AuthenticationServiceException("Validation failed: " + errorMessages);
        }

        // 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getAccount(), loginRequestDto.getPassword());
        return authenticationManager.authenticate(authToken); // AuthenticationManager에 토큰 전달
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        String username = customUserDetails.getUsername();

        String accessToken = jwtTokenProvider.createJwt("access", username);
        String refreshToken = jwtTokenProvider.createJwt("refresh", username);

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.error("사용자 로그인 인증 실패: {}", failed.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(
            objectMapper.writeValueAsString(new ErrorResponse(ErrorCode.AUTHENTICATION_FAILED)));
    }
}
