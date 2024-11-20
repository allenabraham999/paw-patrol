package com.simplogics.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse<T> {

    private List<T> items;
    private long totalCount;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private Boolean hasNextPage;
    private Boolean hasPrevPage;

}
