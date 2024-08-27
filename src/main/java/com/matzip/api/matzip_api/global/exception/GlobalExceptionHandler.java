package com.matzip.api.matzip_api.global.exception;

import com.matzip.api.matzip_api.global.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException customException) {
        ErrorResponse errorResponse = new ErrorResponse(customException.getErrorCode(),
            customException.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }
}
