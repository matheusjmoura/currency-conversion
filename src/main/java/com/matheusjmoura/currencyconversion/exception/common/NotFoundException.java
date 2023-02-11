package com.matheusjmoura.currencyconversion.exception.common;

public class NotFoundException extends BusinessException {

    public NotFoundException(String messageKey, Object... args) {
        super(messageKey, args);
    }

}
