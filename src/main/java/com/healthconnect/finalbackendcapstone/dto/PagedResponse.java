package com.healthconnect.finalbackendcapstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    
    public static <T> PagedResponse<T> of(List<T> content, int page, int size, long totalElements, int totalPages, boolean first, boolean last) {
        return new PagedResponse<>(content, page, size, totalElements, totalPages, last, first);
    }
} 