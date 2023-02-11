package com.matheusjmoura.currencyconversion.exception;

import com.matheusjmoura.currencyconversion.exception.common.NotFoundException;
import lombok.Getter;

import java.util.UUID;

public class UserNotFoundException extends NotFoundException {

    @Getter
    private final UUID id;

    public UserNotFoundException(UUID id) {
        super("user.notFound.exception", id);
        this.id = id;
    }

}
