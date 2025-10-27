package com.seonbistudy.seonbistudy.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for ErrorCode enum
 */
class ErrorCodeTest {

    @Test
    void testErrorCodeStructure() {
        // Test AUTH-1001
        ErrorCode errorCode = ErrorCode.AUTH_INVALID_CREDENTIALS;
        
        assertEquals("AUTH-1001", errorCode.getCode());
        assertEquals("Invalid username or password", errorCode.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, errorCode.getHttpStatus());
    }

    @Test
    void testAllErrorCodesHaveUniqueCode() {
        ErrorCode[] errorCodes = ErrorCode.values();
        
        for (int i = 0; i < errorCodes.length; i++) {
            for (int j = i + 1; j < errorCodes.length; j++) {
                assertNotEquals(errorCodes[i].getCode(), errorCodes[j].getCode(),
                    "Error codes must be unique: " + errorCodes[i].name() + " and " + errorCodes[j].name());
            }
        }
    }

    @Test
    void testAllErrorCodesHaveMessage() {
        for (ErrorCode errorCode : ErrorCode.values()) {
            assertNotNull(errorCode.getMessage(), 
                errorCode.name() + " must have a message");
            assertFalse(errorCode.getMessage().isEmpty(), 
                errorCode.name() + " message must not be empty");
        }
    }

    @Test
    void testAllErrorCodesHaveHttpStatus() {
        for (ErrorCode errorCode : ErrorCode.values()) {
            assertNotNull(errorCode.getHttpStatus(), 
                errorCode.name() + " must have an HTTP status");
        }
    }

    @Test
    void testAuthenticationErrorsHaveAuthPrefix() {
        assertTrue(ErrorCode.AUTH_INVALID_CREDENTIALS.getCode().startsWith("AUTH-"));
        assertTrue(ErrorCode.AUTH_USER_NOT_FOUND.getCode().startsWith("AUTH-"));
        assertTrue(ErrorCode.AUTH_TOKEN_EXPIRED.getCode().startsWith("AUTH-"));
    }

    @Test
    void testCommonErrorsHaveCmnPrefix() {
        assertTrue(ErrorCode.CMN_VALIDATION_FAILED.getCode().startsWith("CMN-"));
        assertTrue(ErrorCode.CMN_RESOURCE_NOT_FOUND.getCode().startsWith("CMN-"));
        assertTrue(ErrorCode.CMN_INTERNAL_ERROR.getCode().startsWith("CMN-"));
    }

    @Test
    void testCredentialErrorsHaveAuth1xxxCode() {
        assertTrue(ErrorCode.AUTH_INVALID_CREDENTIALS.getCode().startsWith("AUTH-1"));
        assertTrue(ErrorCode.AUTH_USER_NOT_FOUND.getCode().startsWith("AUTH-1"));
        assertTrue(ErrorCode.AUTH_ACCOUNT_DISABLED.getCode().startsWith("AUTH-1"));
    }

    @Test
    void testTokenErrorsHaveAuth2xxxCode() {
        assertTrue(ErrorCode.AUTH_TOKEN_INVALID.getCode().startsWith("AUTH-2"));
        assertTrue(ErrorCode.AUTH_TOKEN_EXPIRED.getCode().startsWith("AUTH-2"));
        assertTrue(ErrorCode.AUTH_TOKEN_MISSING.getCode().startsWith("AUTH-2"));
    }

    @Test
    void testPasswordErrorsHaveAuth3xxxCode() {
        assertTrue(ErrorCode.AUTH_PASSWORD_TOO_WEAK.getCode().startsWith("AUTH-3"));
    }

    @Test
    void testOAuth2ErrorsHaveAuth4xxxCode() {
        assertTrue(ErrorCode.AUTH_OAUTH2_PROVIDER_ERROR.getCode().startsWith("AUTH-4"));
        assertTrue(ErrorCode.AUTH_OAUTH2_EMAIL_NOT_VERIFIED.getCode().startsWith("AUTH-4"));
    }

    @Test
    void testPermissionErrorsHaveAuth5xxxCode() {
        assertTrue(ErrorCode.AUTH_ACCESS_DENIED.getCode().startsWith("AUTH-5"));
        assertTrue(ErrorCode.AUTH_INSUFFICIENT_PERMISSIONS.getCode().startsWith("AUTH-5"));
    }

    @Test
    void testRegistrationErrorsHaveAuth6xxxCode() {
        assertTrue(ErrorCode.AUTH_USERNAME_TOO_SHORT.getCode().startsWith("AUTH-6"));
        assertTrue(ErrorCode.AUTH_INVALID_ROLE.getCode().startsWith("AUTH-6"));
    }

    @Test
    void testValidationErrorsHaveCmn1xxxCode() {
        assertTrue(ErrorCode.CMN_VALIDATION_FAILED.getCode().startsWith("CMN-1"));
        assertTrue(ErrorCode.CMN_REQUIRED_FIELD_MISSING.getCode().startsWith("CMN-1"));
        assertTrue(ErrorCode.CMN_INVALID_INPUT_FORMAT.getCode().startsWith("CMN-1"));
    }

    @Test
    void testResourceErrorsHaveCmn2xxxCode() {
        assertTrue(ErrorCode.CMN_RESOURCE_NOT_FOUND.getCode().startsWith("CMN-2"));
        assertTrue(ErrorCode.CMN_RESOURCE_ALREADY_EXISTS.getCode().startsWith("CMN-2"));
    }

    @Test
    void testSystemErrorsHaveCmn9xxxCode() {
        assertTrue(ErrorCode.CMN_INTERNAL_ERROR.getCode().startsWith("CMN-9"));
        assertTrue(ErrorCode.CMN_SERVICE_UNAVAILABLE.getCode().startsWith("CMN-9"));
        assertTrue(ErrorCode.CMN_DATABASE_ERROR.getCode().startsWith("CMN-9"));
    }
}
