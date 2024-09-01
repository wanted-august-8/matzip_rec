package com.matzip.api.matzip_api.global.auth.util;

import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.exception.JwtAuthenticationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final Long accessExpiration;
    private final Long refreshExpiration;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
        @Value("${jwt.access-token-validate-in-seconds}") String accessExpiration,
        @Value("${jwt.refresh-token-validate-in-seconds}") String refreshExpiration) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
            Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessExpiration = Long.parseLong(accessExpiration) * 1000;
        this.refreshExpiration = Long.parseLong(refreshExpiration) * 1000;
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token); // JWT 서명 및 유효성 검증
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException(ErrorCode.TOKEN_EXPIRED, e);
        } catch (MalformedJwtException | UnsupportedJwtException |
                 SignatureException | IllegalArgumentException e) {
            throw new JwtAuthenticationException(ErrorCode.INVALID_TOKEN, e);
        }
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("account", String.class);
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String createJwt(String category, String username) {
        return Jwts.builder()
            .claim("category", category)
            .claim("account", username)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(
                System.currentTimeMillis() + (category.equals("refresh") ? refreshExpiration: accessExpiration)))
            .signWith(secretKey)
            .compact();
    }
}
