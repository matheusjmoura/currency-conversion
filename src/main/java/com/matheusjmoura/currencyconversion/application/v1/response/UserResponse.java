package com.matheusjmoura.currencyconversion.application.v1.response;

import com.matheusjmoura.currencyconversion.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserResponse {

    @Schema(description = "User's ID", example = "a96c7f4d-760d-416a-9828-5c57ed0fb888")
    private UUID id;
    @Schema(description = "User's name", example = "Matheus Moura")
    private String name;

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName());
    }

}
