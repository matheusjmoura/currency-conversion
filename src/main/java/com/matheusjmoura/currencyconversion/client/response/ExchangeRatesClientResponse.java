package com.matheusjmoura.currencyconversion.client.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRatesClientResponse {

    private Boolean success;
    private Long timestamp;
    private String base;
    private String date;
    private HashMap<String, BigDecimal> rates;

}
