package com.matheusjmoura.currencyconversion.exception;

import com.matheusjmoura.currencyconversion.exception.common.BusinessException;
import lombok.Getter;

public class IdenticalCurrencyExchangeException extends BusinessException {

    @Getter
    private final String currency;

    public IdenticalCurrencyExchangeException(String currency) {
        super("exchange.identicalCurrency.exception", new Object[]{currency});
        this.currency = currency;
    }

}
