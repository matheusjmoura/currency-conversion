package com.matheusjmoura.currencyconversion.stubs;

import com.matheusjmoura.currencyconversion.client.response.ExchangeRatesErrorResponse;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRatesErrorResponseStub {

    public static ExchangeRatesErrorResponse createErrorBody() {
        Map<String, String> error = new HashMap<>();
        error.put("code", "invalid_base_currency");
        error.put("message", "An unexpected error ocurred. [Technical Support: support@apilayer.com]");
        return new ExchangeRatesErrorResponse(error, null);
    }

    public static ExchangeRatesErrorResponse createErrorMessage() {
        return new ExchangeRatesErrorResponse(null, "No API key found in request.");
    }

}
