package com.matzip.api.matzip_api.global.exception;


import com.matzip.api.matzip_api.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationException extends AuthenticationException {

    private final ErrorCode errorCode;

    public JwtAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public JwtAuthenticationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public JwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
        this.errorCode = ErrorCode.AUTHENTICATION_FAILED;
    }
}
