package com.matzip.api.matzip_api.global.auth.util;

import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.exception.JwtAuthenticationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secret,
        @Value("${jwt.token-validate-in-seconds}") String expiration) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
            Jwts.SIG.HS256.key().build().getAlgorithm());
        this.expiration = Long.parseLong(expiration) * 1000;
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token); // JWT 서명 및 유효성 검증
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException(ErrorCode.TOKEN_EXPIRED, e);
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException(ErrorCode.INVALID_TOKEN, e);
        }
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("account", String.class);
    }

    public String createJwt(String username) {
        return Jwts.builder()
            .claim("account", username)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(secretKey)
            .compact();
    }
}
