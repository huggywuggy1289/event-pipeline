package com.eventpipeline.domain.global.apiPayload.exception;

import com.eventpipeline.domain.global.common.ApiResponse;
import com.eventpipeline.domain.global.apiPayload.code.status.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(GeneralException e) {
        ErrorStatus status = e.getErrorStatus();
        return ResponseEntity.badRequest()
                .body(ApiResponse.onFailure(status.getCode(), status.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        ErrorStatus status = ErrorStatus.UNKNOWN_ERROR;
        log.error("Unhandled exception occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.onFailure(status.getCode(), status.getMessage(), null));
    }
}
