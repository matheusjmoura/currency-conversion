package com.matheusjmoura.currencyconversion.service;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.matheusjmoura.currencyconversion.client.ExchangeRatesClient;
import com.matheusjmoura.currencyconversion.client.response.ExchangeRatesClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Service
public class ExchangeRatesCacheService {

    private static final String EXCHANGE_RATES_CACHE_KEY = "EXCHANGE_RATES_CACHE_KEY";

    private final ExchangeRatesClient exchangeRatesClient;

    private final AsyncLoadingCache<String, HashMap<String, BigDecimal>> exchangeRatesCache;

    public ExchangeRatesCacheService(
        ExchangeRatesClient exchangeRatesClient,
        @Value("#{T(com.matheusjmoura.currencyconversion.service.ExchangeRatesCacheService).isTimeToLiveValid('${exchange-rates.api.cache.ttl}') ? '${exchange-rates.api.cache.ttl}' : 3L}")
        Long timeToLive
    ) {
        this.exchangeRatesClient = exchangeRatesClient;
        this.exchangeRatesCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(timeToLive))
            .buildAsync((key, executor) -> this.getEuroBasedExchangeRates().toFuture());
    }

    public Mono<HashMap<String, BigDecimal>> getExchangeRatesCache() {
        return Mono.fromFuture(exchangeRatesCache.get(EXCHANGE_RATES_CACHE_KEY));
    }

    private Mono<HashMap<String, BigDecimal>> getEuroBasedExchangeRates() {
        return exchangeRatesClient.getEuroBasedExchangeRates()
            .map(ExchangeRatesClientResponse::getRates);
    }

    public static boolean isTimeToLiveValid(String timeToLive) {
        try {
            return Objects.nonNull(timeToLive) && Long.parseLong(timeToLive) >= 3L;
        } catch (Exception e) {
            return false;
        }
    }

}
