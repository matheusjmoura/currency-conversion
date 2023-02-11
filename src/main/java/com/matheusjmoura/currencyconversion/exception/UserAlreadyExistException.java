package com.matheusjmoura.currencyconversion.exception;

import com.matheusjmoura.currencyconversion.exception.common.BusinessException;

public class UserAlreadyExistException extends BusinessException {

    public UserAlreadyExistException(Object... args) {
        super("user.exist.exception", args);
    }

}
