package com.likelion.backend.global.exception.advice;

import com.likelion.backend.global.exception.BaseException;
import com.likelion.backend.global.response.ApiResponse;
import com.likelion.backend.global.response.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException e) {

        return ResponseEntity
                .status(e.getStatusCode())
                .body(ApiResponse.failOnly(e.getErrorStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {

        return ResponseEntity
                .status(500)
                .body(ApiResponse.fail(500, ErrorStatus.INTERNAL_SERVER_ERROR.getMessage()));
    }
}
