package com.matheusjmoura.currencyconversion.exception;

import com.matheusjmoura.currencyconversion.exception.common.BusinessException;

public class ExchangeRatesClientException extends BusinessException {

    public ExchangeRatesClientException(String message) {
        super("exchange.client.exception", message);
    }

}
