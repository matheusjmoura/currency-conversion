package com.matheusjmoura.currencyconversion.service;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.matheusjmoura.currencyconversion.client.ExchangeRatesClient;
import com.matheusjmoura.currencyconversion.client.response.ExchangeRatesClientResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;

@Slf4j
@Service
public class ExchangeRatesCacheService {

    public static final String EXCHANGE_RATES_CACHE_KEY = "EXCHANGE_RATES_CACHE_KEY";

    private final ExchangeRatesClient exchangeRatesClient;

    @Getter
    private final AsyncLoadingCache<String, HashMap<String, BigDecimal>> cache;

    public ExchangeRatesCacheService(
        ExchangeRatesClient exchangeRatesClient,
        @Value("${exchange-rates.api.cache.expire-time:12}") Long timeToLive
    ) {
        this.exchangeRatesClient = exchangeRatesClient;
        this.cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(timeToLive))
            .buildAsync((key, executor) -> this.getEuroBasedExchangeRates().toFuture());
    }

    private Mono<HashMap<String, BigDecimal>> getEuroBasedExchangeRates() {
        return exchangeRatesClient.getEuroBasedExchangeRates()
            .map(ExchangeRatesClientResponse::getRates);
    }

}
