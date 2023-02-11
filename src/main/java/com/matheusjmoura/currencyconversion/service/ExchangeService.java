package com.matheusjmoura.currencyconversion.service;

import com.matheusjmoura.currencyconversion.application.v1.request.ExchangeRequest;
import com.matheusjmoura.currencyconversion.application.v1.response.ExchangeResponse;
import com.matheusjmoura.currencyconversion.client.ExchangeRatesClient;
import com.matheusjmoura.currencyconversion.domain.Exchange;
import com.matheusjmoura.currencyconversion.exception.IdenticalCurrencyExchangeException;
import com.matheusjmoura.currencyconversion.repository.ExchangeRepository;
import com.matheusjmoura.currencyconversion.repository.redis.ExchangeRatesCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.matheusjmoura.currencyconversion.repository.redis.ExchangeRatesCacheRepository.EXCHANGE_RATES_CACHE_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final UserService userService;
    private final ExchangeRatesClient exchangeRatesClient;
    private final ExchangeRepository exchangeRepository;
    private final ExchangeRatesCacheRepository exchangeRatesCacheRepository;

    public Mono<ExchangeResponse> exchange(ExchangeRequest exchangeRequest) {
        return validateCurrencies(exchangeRequest)
            .flatMap(request -> userService.getById(request.getUserId()))
            .flatMap(user -> calculateTaxRate(exchangeRequest.getOriginCurrency(), exchangeRequest.getDestinyCurrency())
                .flatMap(taxRate -> exchangeRepository.save(Exchange.from(exchangeRequest, taxRate, user.getId()))))
            .map(ExchangeResponse::from);
    }

    public Mono<Page<ExchangeResponse>> getAllByUserID(UUID userId, Pageable pageable) {
        return exchangeRepository.findAllByAndUserId(pageable, userId)
            .collectList()
            .map(exchangeList -> exchangeList.stream().map(ExchangeResponse::from).collect(Collectors.toList()))
            .zipWith(exchangeRepository.count())
            .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    private Mono<ExchangeRequest> validateCurrencies(ExchangeRequest exchangeRequest) {
        return Mono.just(exchangeRequest)
            .filter(request -> Boolean.FALSE.equals(request.isSameCurrency()))
            .switchIfEmpty(Mono.error(() -> new IdenticalCurrencyExchangeException(exchangeRequest.getOriginCurrency())))
            .doOnError(throwable -> log.error("Error exchanging two identical currencies. User ID: {} - Currency: {}",
                exchangeRequest.getUserId(), exchangeRequest.getOriginCurrency()))
            .thenReturn(exchangeRequest);
    }

    private Mono<BigDecimal> calculateTaxRate(String originCurrency, String destinyCurrency) {
        return exchangeRatesCacheRepository.getEuroBasedExchangeRates()
            .map(exchangeRates -> exchangeRates.get(destinyCurrency)
                .divide(exchangeRates.get(originCurrency), 6, RoundingMode.HALF_EVEN))
            .switchIfEmpty(requestAndSaveUpdatedExchangeRates()
                .flatMap(unused -> calculateTaxRate(originCurrency, destinyCurrency)));
    }

    private Mono<Boolean> requestAndSaveUpdatedExchangeRates() {
        return exchangeRatesClient.getEuroBasedExchangeRates()
            .flatMap(exchangeRatesResponse -> exchangeRatesCacheRepository.save(EXCHANGE_RATES_CACHE_KEY,
                exchangeRatesResponse))
            .doOnError(throwable -> log.error("Error communicating with Exchange Rates Data API.", throwable));
    }

}
