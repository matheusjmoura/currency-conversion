package com.matheusjmoura.currencyconversion.stubs;

import com.matheusjmoura.currencyconversion.domain.Exchange;
import com.matheusjmoura.currencyconversion.domain.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ExchangeStub {

    public static Exchange create(User user) {
        return new Exchange(
            UUID.fromString("a96c7f4d-760d-416a-9828-5c57ed0fb888"),
            user.getId(),
            user,
            "UAH",
            BigDecimal.ONE,
            "BRL",
            BigDecimal.valueOf(0.140096),
            LocalDateTime.of(2023, 2, 10, 13, 15, 20),
            0L
        );
    }

}
