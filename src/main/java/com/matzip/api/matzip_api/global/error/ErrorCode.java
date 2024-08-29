package com.matzip.api.matzip_api.global.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE(BAD_REQUEST, "유효하지 않은 입력값입니다."),

    // 사용자
    USER_NOT_FOUND(BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    ACCOUNT_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 등록된 계정입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
