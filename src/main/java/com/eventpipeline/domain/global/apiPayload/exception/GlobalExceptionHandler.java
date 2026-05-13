package com.eventpipeline.domain.global.apiPayload.exception;

import com.eventpipeline.domain.global.common.ApiResponse;
import com.eventpipeline.domain.global.apiPayload.code.status.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("[%s]: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        log.warn("Validation failed: {}", errorMessage);
        return ResponseEntity.badRequest()
                .body(ApiResponse.onFailure("4000", errorMessage, null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Business logic error: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.onFailure("4001", e.getMessage(), null));
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(GeneralException e) {
        ErrorStatus status = e.getErrorStatus();
        return ResponseEntity.badRequest()
                .body(ApiResponse.onFailure(status.getCode(), status.getMessage(), null));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("JSON parsing error: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.onFailure("4002", "요청 데이터 형식이 잘못되었거나 누락된 필드가 있습니다.", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unhandled exception occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.onFailure("5999", "알 수 없는 서버 오류가 발생했습니다.", null));
    }
}
