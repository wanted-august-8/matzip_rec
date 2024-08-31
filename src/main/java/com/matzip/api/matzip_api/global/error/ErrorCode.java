package com.matzip.api.matzip_api.global.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE(BAD_REQUEST, "유효하지 않은 입력 값입니다."),

    // 사용자
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    ACCOUNT_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 등록된 계정입니다."),

    // 맛집
    RESTRT_NOT_FOUND(HttpStatus.BAD_REQUEST, "식당을 찾을 수 없습니다."),

    //시군구
    SGG_DATA_ISEMPTY(BAD_REQUEST,"데이터베이스가 비어 있습니다. 데이터를 추가한 후 다시 시도해 주세요.")

    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
