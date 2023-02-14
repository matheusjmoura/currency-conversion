package com.matheusjmoura.currencyconversion.stubs;

import com.matheusjmoura.currencyconversion.application.v1.response.ExchangeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

public class ExchangeResponseStub {

    public static ExchangeResponse create() {
        return new ExchangeResponse(
            UUID.fromString("a96c7f4d-760d-416a-9828-5c57ed0fb888"),
            UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a"),
            "UAH",
            BigDecimal.ONE,
            "BRL",
            BigDecimal.valueOf(0.140096),
            BigDecimal.valueOf(0.140096),
            LocalDateTime.of(2023, 2, 10, 13, 15, 20)
        );
    }

    public static Page<ExchangeResponse> createPageResponse(Pageable pageable) {
        return new PageImpl<>(Collections.singletonList(create()), pageable, 1);
    }

}
