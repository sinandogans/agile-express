package com.obss.jcp.sinandogan.agileexpress.infrastructure.config.exceptionhandling;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BaseBusinessException.class)
    public ResponseEntity<ApiError> handleBusinessExceptions(BaseBusinessException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ApiError.builder()
                        .message(exception.getMessage())
                        .status(exception.getHttpStatus().value())
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .details(exception.getExtensions())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralExceptions(Exception exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}