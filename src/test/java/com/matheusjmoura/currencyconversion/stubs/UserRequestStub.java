package com.matheusjmoura.currencyconversion.stubs;

import com.matheusjmoura.currencyconversion.application.v1.request.UserRequest;

public class UserRequestStub {

    public static UserRequest create() {
        return new UserRequest(
            "Matheus Moura"
        );
    }

}
