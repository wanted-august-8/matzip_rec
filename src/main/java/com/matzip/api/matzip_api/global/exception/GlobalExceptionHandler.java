package com.matzip.api.matzip_api.global.exception;

import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.error.ErrorResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());

        String combinedErrorMessage = String.join(", ", errorMessages);

        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE, combinedErrorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
