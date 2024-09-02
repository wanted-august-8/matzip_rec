package com.matzip.api.matzip_api.global.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE(BAD_REQUEST, "유효하지 않은 입력 값입니다."),
    REDIS_SERVER_ERROR(BAD_REQUEST, "Redis 서버에서 오류가 발생했습니다."),

    // 인증&인가
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "사용자 인증에 실패했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    AUTHORIZATION_HEADER_MISSING(HttpStatus.UNAUTHORIZED, "Authorization 헤더값이 유효하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰 인증 시간이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 형식이 유효하지 않습니다."),
    ACCESS_TOKEN_HEADER_MISSING(HttpStatus.UNAUTHORIZED, "access 토큰 헤더값이 유효하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "refresh 토큰을 찾을 수 없습니다."),

    // 사용자
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    ACCOUNT_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 등록된 계정입니다."),

    // 맛집
    RESTRT_NOT_FOUND(HttpStatus.BAD_REQUEST, "식당을 찾을 수 없습니다."),
    RESTRT_FETCH_FAILED(INTERNAL_SERVER_ERROR, "식당 정보를 저장하는 데 실패했습니다."),
    SORT_PARAMETER_INVALID(HttpStatus.BAD_REQUEST, "정렬은 '거리순','평점순'만 가능합니다."),

    //시군구
    SGG_DATA_ISEMPTY(BAD_REQUEST,"데이터베이스가 비어 있습니다. 데이터를 추가한 후 다시 시도해 주세요."),
    INVALID_FILE_FORMAT(BAD_REQUEST, "파일 형식이 유효하지 않습니다."),

    //file
    FILE_READ_ERROR(INTERNAL_SERVER_ERROR, "파일 읽기 오류가 발생했습니다."),


    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
