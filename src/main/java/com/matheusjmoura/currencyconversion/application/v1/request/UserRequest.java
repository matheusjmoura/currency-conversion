package com.matheusjmoura.currencyconversion.application.v1.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequest {

    @NotBlank(message = "{user.name.notBlank}")
    @Size(min = 5, max = 255, message = "{user.name.size}")
    @Schema(description = "User name", example = "Matheus Moura", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

}
