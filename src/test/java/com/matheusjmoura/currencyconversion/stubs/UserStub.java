package com.matheusjmoura.currencyconversion.stubs;

import com.matheusjmoura.currencyconversion.domain.User;

import java.util.UUID;

public class UserStub {

    public static User create() {
        return new User(
            UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a"),
            "Matheus Moura",
            0L
        );
    }

}
