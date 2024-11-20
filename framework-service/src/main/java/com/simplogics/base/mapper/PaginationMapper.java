package com.simplogics.base.mapper;

import com.simplogics.base.dto.PaginatedResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public class PaginationMapper {

    public static <U, T> PaginatedResponse<T> convertEntityToResponse(Page<U> page, List<T> dtoList) {
        return PaginatedResponse.<T>builder()
                .items(dtoList)
                .totalCount(page.getTotalElements())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .hasNextPage(page.hasNext())
                .hasPrevPage(page.hasPrevious())
                .build();
    }

}
