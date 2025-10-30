package com.seonbistudy.seonbistudy.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    
    // ============================================
    // COMMON/GENERAL ERRORS (CMN-xxxx)
    // ============================================
    
    // Validation & Input Errors (CMN-1xxx)
    CMN_VALIDATION_FAILED("CMN-1001", "Validation failed", HttpStatus.BAD_REQUEST),
    CMN_REQUIRED_FIELD_MISSING("CMN-1002", "Required field is missing", HttpStatus.BAD_REQUEST),
    CMN_INVALID_INPUT_FORMAT("CMN-1003", "Invalid input format", HttpStatus.BAD_REQUEST),
    CMN_INVALID_PARAMETER("CMN-1004", "Invalid parameter provided", HttpStatus.BAD_REQUEST),
    CMN_INVALID_REQUEST_BODY("CMN-1005", "Invalid request body", HttpStatus.BAD_REQUEST),
    
    // Resource Errors (CMN-2xxx)
    CMN_RESOURCE_NOT_FOUND("CMN-2001", "Resource not found", HttpStatus.NOT_FOUND),
    CMN_RESOURCE_ALREADY_EXISTS("CMN-2002", "Resource already exists", HttpStatus.CONFLICT),
    CMN_RESOURCE_CONFLICT("CMN-2003", "Resource conflict occurred", HttpStatus.CONFLICT),
    
    // System Errors (CMN-9xxx)
    CMN_INTERNAL_ERROR("CMN-9001", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    CMN_SERVICE_UNAVAILABLE("CMN-9002", "Service is temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE),
    CMN_DATABASE_ERROR("CMN-9003", "Database error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    CMN_EXTERNAL_SERVICE_ERROR("CMN-9004", "External service error", HttpStatus.BAD_GATEWAY),
    
    // ============================================
    // AUTHENTICATION & AUTHORIZATION (AUTH-xxxx)
    // ============================================
    
    // Credential Errors (AUTH-1xxx)
    AUTH_INVALID_CREDENTIALS("AUTH-1001", "Invalid email or password", HttpStatus.UNAUTHORIZED),
    AUTH_USER_NOT_FOUND("AUTH-1002", "User not found", HttpStatus.NOT_FOUND),
    AUTH_ACCOUNT_DISABLED("AUTH-1003", "Account is disabled", HttpStatus.FORBIDDEN),
    AUTH_ACCOUNT_LOCKED("AUTH-1004", "Account is locked", HttpStatus.FORBIDDEN),
    AUTH_USERNAME_EXISTS("AUTH-1005", "Username is already taken", HttpStatus.BAD_REQUEST),
    AUTH_EMAIL_EXISTS("AUTH-1006", "Email is already in use", HttpStatus.BAD_REQUEST),
    
    // Token Errors (AUTH-2xxx)
    AUTH_TOKEN_INVALID("AUTH-2001", "Invalid or malformed token", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_EXPIRED("AUTH-2002", "Token has expired", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_MISSING("AUTH-2003", "Authentication token is missing", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_REVOKED("AUTH-2004", "Token has been revoked", HttpStatus.UNAUTHORIZED),
    
    // Password Errors (AUTH-3xxx)
    AUTH_PASSWORD_TOO_WEAK("AUTH-3001", "Password does not meet security requirements", HttpStatus.BAD_REQUEST),
    AUTH_PASSWORD_MISMATCH("AUTH-3002", "Password confirmation does not match", HttpStatus.BAD_REQUEST),
    AUTH_OLD_PASSWORD_INCORRECT("AUTH-3003", "Old password is incorrect", HttpStatus.BAD_REQUEST),
    
    // OAuth2 Errors (AUTH-4xxx)
    AUTH_OAUTH2_PROVIDER_ERROR("AUTH-4001", "OAuth2 provider authentication failed", HttpStatus.BAD_REQUEST),
    AUTH_OAUTH2_EMAIL_NOT_VERIFIED("AUTH-4002", "Email from OAuth2 provider is not verified", HttpStatus.BAD_REQUEST),
    AUTH_OAUTH2_USER_INFO_ERROR("AUTH-4003", "Failed to retrieve user info from OAuth2 provider", HttpStatus.INTERNAL_SERVER_ERROR),
    AUTH_OAUTH2_STATE_MISMATCH("AUTH-4004", "OAuth2 state parameter mismatch", HttpStatus.BAD_REQUEST),
    
    // Permission/Authorization Errors (AUTH-5xxx)
    AUTH_ACCESS_DENIED("AUTH-5001", "Access denied", HttpStatus.FORBIDDEN),
    AUTH_INSUFFICIENT_PERMISSIONS("AUTH-5002", "Insufficient permissions to perform this action", HttpStatus.FORBIDDEN),
    AUTH_ROLE_NOT_ALLOWED("AUTH-5003", "This role is not allowed to perform this action", HttpStatus.FORBIDDEN),
    
    // Registration Errors (AUTH-6xxx)
    AUTH_USERNAME_TOO_SHORT("AUTH-6001", "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    AUTH_USERNAME_INVALID_FORMAT("AUTH-6002", "Username contains invalid characters", HttpStatus.BAD_REQUEST),
    AUTH_EMAIL_INVALID_FORMAT("AUTH-6003", "Invalid email format", HttpStatus.BAD_REQUEST),
    AUTH_INVALID_ROLE("AUTH-6004", "Invalid role specified", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
