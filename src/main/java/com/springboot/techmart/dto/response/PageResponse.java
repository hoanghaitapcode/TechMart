package com.springboot.techmart.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter @Builder
public class PageResponse<T> {
    private List<T> content;
    private  int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean isLast;
}
