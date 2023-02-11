package com.matheusjmoura.currencyconversion.client.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRatesErrorResponse {

    private Map<String, String> error;
    private String message;

}
