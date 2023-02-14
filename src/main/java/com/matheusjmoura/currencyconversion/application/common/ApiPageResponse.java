package com.matheusjmoura.currencyconversion.application.common;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class ApiPageResponse<T> {

    @Schema(description = "Indicates if there is a next content page", example = "true")
    private boolean hasNext;
    @Schema(description = "The size of the page to be returned", example = "20")
    private int pageSize;
    @Schema(description = "Zero-based page index (0..N)", example = "0")
    private int pageNumber;
    @Schema(description = "Total amount of elements spanning all pages", example = "1")
    private long totalElements;
    @ArraySchema(schema = @Schema(description = "Current page content as List"))
    private Collection<T> content;

    public static <T> ApiPageResponse<T> from(Page<T> page) {
        return new ApiPageResponse<>(
            page.hasNext(),
            page.getSize(),
            page.getNumber(),
            page.getTotalElements(),
            page.getContent());
    }

}
