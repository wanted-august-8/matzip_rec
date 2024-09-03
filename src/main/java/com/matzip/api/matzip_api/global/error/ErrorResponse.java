package com.matzip.api.matzip_api.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    private final ErrorCode errorCode;
    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String message;

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.statusCode = errorCode.getHttpStatus().value();
        this.httpStatus = errorCode.getHttpStatus();
        this.message = message;
    }

    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }
}
