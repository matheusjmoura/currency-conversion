package com.matheusjmoura.currencyconversion.application.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BodyErrorResponse {

    @Schema(description = "Exception title", example = "Fail on task execution.")
    private final String title;
    @Schema(description = "Exception details", example = "[\n\"User already exist with name Matheus Moura.\"\n]")
    private final List<String> details = new ArrayList<>();

    public BodyErrorResponse(String title) {
        this.title = title;
    }

    public void addDetail(String detail) {
        details.add(detail);
    }

}
