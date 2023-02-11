package com.matheusjmoura.currencyconversion.exception.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorAttributes {

    @Schema(description = "Error title", example = "Fail on task execution.")
    private final String title;
    @Schema(description = "Error details", example = "[\n\"User already exist with name Matheus Moura.\"\n]")
    private final List<String> details = new ArrayList<>();

    public ErrorAttributes(String title) {
        this.title = title;
    }

    public void addDetail(String detail) {
        details.add(detail);
    }

}
