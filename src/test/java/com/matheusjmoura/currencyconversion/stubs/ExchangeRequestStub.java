package com.matheusjmoura.currencyconversion.stubs;

import com.matheusjmoura.currencyconversion.application.v1.request.ExchangeRequest;

import java.math.BigDecimal;
import java.util.UUID;

public class ExchangeRequestStub {

    public static ExchangeRequest create() {
        return new ExchangeRequest(
            UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a"),
            "UAH",
            "BRL",
            BigDecimal.ONE
        );
    }

    public static ExchangeRequest createSameCurrency() {
        return new ExchangeRequest(
            UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a"),
            "BRL",
            "BRL",
            BigDecimal.ONE
        );
    }

}
