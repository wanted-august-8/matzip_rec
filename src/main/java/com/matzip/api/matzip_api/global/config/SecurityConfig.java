package com.matzip.api.matzip_api.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.api.matzip_api.global.auth.filter.JwtFilter;
import com.matzip.api.matzip_api.global.auth.filter.LoginFilter;
import com.matzip.api.matzip_api.global.auth.util.TokenManager;
import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.error.ErrorResponse;
import com.matzip.api.matzip_api.global.exception.JwtAuthenticationException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PERMIT_URL_ARRAY = {
        "/v3/api-docs/**", "/swagger-ui/**", "/v3/api-docs", "/swagger-ui.html",
        "/error", "/signup", "/login", "/reissue",
        "/health-check","/sgg","/restrt/**"
    };
    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final TokenManager tokenManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PERMIT_URL_ARRAY).permitAll()
                .anyRequest().authenticated()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler))
            .addFilterBefore(new JwtFilter(tokenManager), UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(new LoginFilter(objectMapper, authenticationManager(authenticationConfiguration),
                tokenManager), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }

    private final AuthenticationEntryPoint authenticationEntryPoint =
        (request, response, authException) -> {
            log.error("인증 실패: {}", authException.getMessage());
            ErrorCode errorCode = ErrorCode.AUTHENTICATION_FAILED;
            if (authException instanceof JwtAuthenticationException) {
                errorCode = ((JwtAuthenticationException) authException).getErrorCode();
            }
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, errorCode);
        };

    private final AccessDeniedHandler accessDeniedHandler =
        (request, response, accessDeniedException) ->{
            log.error("Access Denied: {}", accessDeniedException.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, ErrorCode.ACCESS_DENIED);
        };

    private static void sendErrorResponse(HttpServletResponse response, int status, ErrorCode errorCode) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse(errorCode, errorCode.getMessage())));
    }
}
