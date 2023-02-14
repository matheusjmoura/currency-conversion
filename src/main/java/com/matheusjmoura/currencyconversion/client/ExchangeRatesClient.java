package com.matheusjmoura.currencyconversion.client;

import com.matheusjmoura.currencyconversion.client.response.ExchangeRatesClientResponse;
import com.matheusjmoura.currencyconversion.client.response.ExchangeRatesErrorResponse;
import com.matheusjmoura.currencyconversion.exception.ExchangeRatesClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Component
public class ExchangeRatesClient {

    private static final String EURO_BASE_CURRENCY = "EUR";

    private final WebClient webClient = WebClient.create();

    @Value("${exchange-rates.api.url}")
    private String baseUrl;
    @Value("${exchange-rates.api.key}")
    private String apiKey;

    public Mono<ExchangeRatesClientResponse> getEuroBasedExchangeRates() {
        return webClient.get()
            .uri(getEuroBasedExchangeRatesUrl().toUriString())
            .headers(getHeaders())
            .retrieve()
            .onStatus(HttpStatus::isError, errorHandler())
            .bodyToMono(ExchangeRatesClientResponse.class)
            .doOnSuccess(exchangeRatesClientResponse -> log.info("Successfully retrieved exchange rates from Exchange Rates Data API."));
    }

    private UriComponentsBuilder getEuroBasedExchangeRatesUrl() {
        return UriComponentsBuilder.fromUriString(baseUrl)
            .pathSegment("exchangerates_data", "latest")
            .queryParam("base", EURO_BASE_CURRENCY);
    }

    private Consumer<HttpHeaders> getHeaders() {
        return (httpHeaders -> httpHeaders.set("apiKey", apiKey));
    }

    private Function<ClientResponse, Mono<? extends Throwable>> errorHandler() {
        return clientResponse -> clientResponse.bodyToMono(ExchangeRatesErrorResponse.class)
            .map(response -> {
                String message = Objects.nonNull(response.getError())
                    ? response.getError().get("code").concat(" - ").concat(response.getError().get("message"))
                    : response.getMessage();
                throw new ExchangeRatesClientException(message);
            });
    }

}
