package com.seonbistudy.seonbistudy.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private String message;
    
    private String code;
    
    private Map<String, Object> details;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    // Legacy fields for backward compatibility
    private Integer status;
    private String path;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }

    public void addValidationError(String field, String message) {
        if (details == null) {
            details = new HashMap<>();
        }
        @SuppressWarnings("unchecked")
        List<ValidationError> validationErrors = (List<ValidationError>) details.computeIfAbsent(
            "validationErrors", k -> new ArrayList<ValidationError>()
        );
        validationErrors.add(new ValidationError(field, message));
    }
    
    public void addDetail(String key, Object value) {
        if (details == null) {
            details = new HashMap<>();
        }
        details.put(key, value);
    }
    
    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .status(errorCode.getHttpStatus().value())
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .status(errorCode.getHttpStatus().value())
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
