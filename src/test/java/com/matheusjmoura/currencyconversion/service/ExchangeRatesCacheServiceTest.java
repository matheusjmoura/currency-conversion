package com.matheusjmoura.currencyconversion.service;

import com.matheusjmoura.currencyconversion.client.ExchangeRatesClient;
import com.matheusjmoura.currencyconversion.stubs.ExchangeRatesClientResponseStub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRatesCacheServiceTest {

    private static final Long TIME_TO_LIVE = 3L;

    @Mock
    private ExchangeRatesClient exchangeRatesClient;
    private ExchangeRatesCacheService exchangeRatesCacheService;

    @BeforeEach
    void setUp() {
        this.exchangeRatesCacheService = new ExchangeRatesCacheService(exchangeRatesClient, TIME_TO_LIVE);
    }

    @Test
    @DisplayName("Get exchange rates cache method should return cached data if cache is available")
    void getExchangeRatesCache() {
        when(exchangeRatesClient.getEuroBasedExchangeRates()).thenReturn(Mono.just(ExchangeRatesClientResponseStub.create()));

        StepVerifier.create(exchangeRatesCacheService.getExchangeRatesCache())
            .consumeNextWith(stringBigDecimalHashMap -> Assertions.assertEquals(BigDecimal.valueOf(3.949265), stringBigDecimalHashMap.get("AED")))
            .verifyComplete();
        StepVerifier.create(exchangeRatesCacheService.getExchangeRatesCache())
            .expectNextCount(1)
            .verifyComplete();

        verify(exchangeRatesClient).getEuroBasedExchangeRates();
    }

    @Test
    @DisplayName("Get exchange rates cache method should obtain new data if cache is expired")
    void getExchangeRatesCacheExpired() {
        when(exchangeRatesClient.getEuroBasedExchangeRates()).thenReturn(Mono.just(ExchangeRatesClientResponseStub.create()));

        StepVerifier.create(exchangeRatesCacheService.getExchangeRatesCache())
            .consumeNextWith(stringBigDecimalHashMap -> Assertions.assertEquals(BigDecimal.valueOf(3.949265), stringBigDecimalHashMap.get("AED")))
            .verifyComplete();

        exchangeRatesCacheService = new ExchangeRatesCacheService(exchangeRatesClient, TIME_TO_LIVE);

        StepVerifier.create(exchangeRatesCacheService.getExchangeRatesCache())
            .expectNextCount(1)
            .verifyComplete();

        verify(exchangeRatesClient, times(2)).getEuroBasedExchangeRates();
    }

    @Test
    @DisplayName("Is TTL valid method should return true if value is greater than or equal to 3")
    void isTimeToLiveValid() {
        assertTrue(ExchangeRatesCacheService.isTimeToLiveValid("3"));
    }

    @Test
    @DisplayName("Is TTL valid method should return false if value is less than 3")
    void isTimeToLiveValidMinValue() {
        assertFalse(ExchangeRatesCacheService.isTimeToLiveValid("2"));
    }

    @Test
    @DisplayName("Is TTL valid method should return false if value is not a number")
    void isTimeToLiveValidNotANumber() {
        assertFalse(ExchangeRatesCacheService.isTimeToLiveValid("NaN"));
    }

    @Test
    @DisplayName("Is TTL valid method should return false if value is null")
    void isTimeToLiveValidNull() {
        assertFalse(ExchangeRatesCacheService.isTimeToLiveValid(null));
    }


}