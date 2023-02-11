package com.matheusjmoura.currencyconversion.repository.redis;

import com.matheusjmoura.currencyconversion.client.response.ExchangeRatesClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;

@Slf4j
@Component
public class ExchangeRatesCacheRepository {

    public static final String EXCHANGE_RATES_CACHE_KEY = "EXCHANGE_RATES_CACHE_KEY";

    private final ReactiveRedisOperations<String, ExchangeRatesClientResponse> redisOperations;

    @Value("${exchange-rates.api.cache.expire-time:12}")
    private Long timeToLive;

    public ExchangeRatesCacheRepository(
        @Qualifier("exchangeRatesRedisOperations") ReactiveRedisOperations<String, ExchangeRatesClientResponse> redisOperations
    ) {
        this.redisOperations = redisOperations;
    }

    public Mono<Boolean> save(String key, ExchangeRatesClientResponse value) {
        return redisOperations.opsForValue().set(key, value, Duration.ofHours(timeToLive));
    }

    public Mono<HashMap<String, BigDecimal>> getEuroBasedExchangeRates() {
        return redisOperations.opsForValue().get(EXCHANGE_RATES_CACHE_KEY)
            .map(ExchangeRatesClientResponse::getRates)
            .switchIfEmpty(Mono.empty());
    }

}
