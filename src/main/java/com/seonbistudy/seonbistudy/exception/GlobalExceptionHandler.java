package com.seonbistudy.seonbistudy.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle custom SeonbiException
     */
    @ExceptionHandler(SeonbiException.class)
    public ResponseEntity<ErrorResponse> handleSeonbiException(SeonbiException ex, WebRequest request) {
        log.error("SeonbiException: {} - {}", ex.getErrorCode().getCode(), ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .status(ex.getErrorCode().getHttpStatus().value())
                .path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }

    /**
     * Handle Spring Security BadCredentialsException
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        log.error("BadCredentialsException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.AUTH_INVALID_CREDENTIALS.getCode())
                .message(ErrorCode.AUTH_INVALID_CREDENTIALS.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle Spring Security DisabledException
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException ex, WebRequest request) {
        log.error("DisabledException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.AUTH_ACCOUNT_DISABLED.getCode())
                .message(ErrorCode.AUTH_ACCOUNT_DISABLED.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handle Spring Security LockedException
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedException(LockedException ex, WebRequest request) {
        log.error("LockedException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.AUTH_ACCOUNT_LOCKED.getCode())
                .message(ErrorCode.AUTH_ACCOUNT_LOCKED.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handle UsernameNotFoundException
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        log.error("UsernameNotFoundException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.AUTH_USER_NOT_FOUND.getCode())
                .message(ErrorCode.AUTH_USER_NOT_FOUND.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle JWT ExpiredJwtException
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        log.error("ExpiredJwtException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.AUTH_TOKEN_EXPIRED.getCode())
                .message(ErrorCode.AUTH_TOKEN_EXPIRED.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle other JWT exceptions
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex, WebRequest request) {
        log.error("JwtException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.AUTH_TOKEN_INVALID.getCode())
                .message(ErrorCode.AUTH_TOKEN_INVALID.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle AccessDeniedException
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        log.error("AccessDeniedException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.AUTH_ACCESS_DENIED.getCode())
                .message(ErrorCode.AUTH_ACCESS_DENIED.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handle general AuthenticationException
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        log.error("AuthenticationException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.AUTH_INVALID_CREDENTIALS.getCode())
                .message(ex.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle validation errors from @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        log.error("ValidationException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.CMN_VALIDATION_FAILED.getCode())
                .message("Validation failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .path(getRequestPath(request))
                .build();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorResponse.addValidationError(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.error("IllegalArgumentException: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.CMN_INVALID_INPUT_FORMAT.getCode())
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle generic Exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: ", ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.CMN_INTERNAL_ERROR.getCode())
                .message("An unexpected error occurred. Please try again later.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Extract request path from WebRequest
     */
    private String getRequestPath(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
            return httpRequest.getRequestURI();
        }
        return request.getDescription(false);
    }
}
