package com.matheusjmoura.currencyconversion.service;

import com.matheusjmoura.currencyconversion.application.v1.request.ExchangeRequest;
import com.matheusjmoura.currencyconversion.application.v1.response.ExchangeResponse;
import com.matheusjmoura.currencyconversion.domain.Exchange;
import com.matheusjmoura.currencyconversion.exception.IdenticalCurrencyExchangeException;
import com.matheusjmoura.currencyconversion.repository.ExchangeRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final UserService userService;
    private final ExchangeRepository exchangeRepository;
    private final ExchangeRatesCacheService exchangeRatesCacheService;

    public Mono<ExchangeResponse> exchange(ExchangeRequest exchangeRequest) {
        return validateCurrencies(exchangeRequest)
            .flatMap(request -> userService.getById(request.getUserId()))
            .flatMap(user -> calculateTaxRate(exchangeRequest.getOriginCurrency(), exchangeRequest.getDestinyCurrency())
                .flatMap(taxRate -> exchangeRepository.save(Exchange.from(exchangeRequest, taxRate, user.getId()))))
            .map(ExchangeResponse::from);
    }

    public Mono<Page<ExchangeResponse>> getAllByUserID(UUID userId, Pageable pageable) {
        return userService.getById(userId)
            .flatMap(user -> exchangeRepository.findAllByAndUserId(pageable, user.getId()).collectList()
                .map(exchangeList -> exchangeList.stream().map(ExchangeResponse::from).collect(Collectors.toList()))
                .zipWith(exchangeRepository.countAllByUserId(userId)))
            .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    private Mono<ExchangeRequest> validateCurrencies(ExchangeRequest exchangeRequest) {
        return Mono.just(exchangeRequest)
            .filter(request -> Boolean.FALSE.equals(request.isSameCurrency()))
            .switchIfEmpty(Mono.error(() -> new IdenticalCurrencyExchangeException(exchangeRequest.getOriginCurrency())))
            .doOnError(throwable -> log.error("Error exchanging two identical currencies. User ID: " + exchangeRequest.getUserId() +
                " - Currency: " + exchangeRequest.getOriginCurrency(), throwable));
    }

    private Mono<BigDecimal> calculateTaxRate(String originCurrency, String destinyCurrency) {
        return exchangeRatesCacheService.getExchangeRatesCache()
            .map(exchangeRates -> exchangeRates.get(destinyCurrency)
                .divide(exchangeRates.get(originCurrency), 6, RoundingMode.HALF_EVEN));
    }


}
