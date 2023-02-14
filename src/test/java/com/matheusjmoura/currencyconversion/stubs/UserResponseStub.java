package com.matheusjmoura.currencyconversion.stubs;

import com.matheusjmoura.currencyconversion.application.v1.response.UserResponse;

import java.util.UUID;

public class UserResponseStub {

    public static UserResponse create() {
        return new UserResponse(
            UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a"),
            "Matheus Moura"
        );
    }

}
