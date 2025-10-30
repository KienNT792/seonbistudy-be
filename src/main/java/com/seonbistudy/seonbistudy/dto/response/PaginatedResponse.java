package com.seonbistudy.seonbistudy.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    
    private List<T> data;
    
    private PaginationInfo pagination;
    
    private String message;
    
    @Builder.Default
    private boolean success = true;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginationInfo {
        private int page;
        private int limit;
        private long total;
        private int totalPages;
        private boolean hasNext;
        private boolean hasPrev;
    }
    
    public static <T> PaginatedResponse<T> of(List<T> data, int page, int limit, long total) {
        int totalPages = (int) Math.ceil((double) total / limit);
        
        PaginationInfo pagination = PaginationInfo.builder()
                .page(page)
                .limit(limit)
                .total(total)
                .totalPages(totalPages)
                .hasNext(page < totalPages)
                .hasPrev(page > 1)
                .build();
        
        return PaginatedResponse.<T>builder()
                .data(data)
                .pagination(pagination)
                .message("Success")
                .success(true)
                .build();
    }
}
