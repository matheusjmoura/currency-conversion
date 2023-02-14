package com.matheusjmoura.currencyconversion.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.matheusjmoura.currencyconversion.exception.ExchangeRatesClientException;
import com.matheusjmoura.currencyconversion.stubs.ExchangeRatesClientResponseStub;
import com.matheusjmoura.currencyconversion.stubs.ExchangeRatesErrorResponseStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.badRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.unauthorized;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("UnitTest")
@WireMockTest(httpPort = 8000)
@ExtendWith(MockitoExtension.class)
class ExchangeRatesClientTest {

    private static final String LATEST_EXCHANGE_RATES_PATH = "/exchangerates_data/latest";
    private static final String BASE_URL = "http://localhost:8000";
    private static final String API_KEY = "API_KEY";

    private ObjectMapper objectMapper;
    private ExchangeRatesClient exchangeRatesClient;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.exchangeRatesClient = new ExchangeRatesClient();
        ReflectionTestUtils.setField(this.exchangeRatesClient, "baseUrl", BASE_URL);
        ReflectionTestUtils.setField(this.exchangeRatesClient, "apiKey", API_KEY);
    }

    @Test
    @DisplayName("Exchange rates client should retrieve data")
    void getEuroBasedExchangeRates() {
        stubFor(get(urlPathEqualTo(LATEST_EXCHANGE_RATES_PATH))
            .withHeader("apiKey", containing(API_KEY))
            .willReturn(ok()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withStatus(HttpStatus.OK.value())
                .withJsonBody(objectMapper.valueToTree(ExchangeRatesClientResponseStub.create()))));

        StepVerifier.create(exchangeRatesClient.getEuroBasedExchangeRates())
            .consumeNextWith(exchangeRatesClientResponse -> assertThat(exchangeRatesClientResponse)
                .extracting(response -> response.getRates().size())
                .isEqualTo(170))
            .verifyComplete();
    }

    @Test
    @DisplayName("Exchange rates client should throw exception if response status is 400")
    void getEuroBasedExchangeRatesErrorBody() {
        stubFor(get(urlPathEqualTo(LATEST_EXCHANGE_RATES_PATH))
            .withHeader("apiKey", containing(API_KEY))
            .willReturn(badRequest()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withJsonBody(objectMapper.valueToTree(ExchangeRatesErrorResponseStub.createErrorBody()))));

        StepVerifier.create(exchangeRatesClient.getEuroBasedExchangeRates())
            .consumeErrorWith(throwable -> {
                assertEquals(ExchangeRatesClientException.class, throwable.getClass());
                assertEquals("invalid_base_currency - An unexpected error ocurred. [Technical Support: support@apilayer.com]",
                    throwable.getMessage());
            })
            .verify();
    }

    @Test
    @DisplayName("Exchange rates client should throw exception if response status is 401")
    void getEuroBasedExchangeRatesErrorMessage() {
        stubFor(get(urlPathEqualTo(LATEST_EXCHANGE_RATES_PATH))
            .withHeader("apiKey", containing(API_KEY))
            .willReturn(unauthorized()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withStatus(HttpStatus.UNAUTHORIZED.value())
                .withJsonBody(objectMapper.valueToTree(ExchangeRatesErrorResponseStub.createErrorMessage()))));

        StepVerifier.create(exchangeRatesClient.getEuroBasedExchangeRates())
            .consumeErrorWith(throwable -> {
                assertEquals(ExchangeRatesClientException.class, throwable.getClass());
                assertEquals("No API key found in request.", throwable.getMessage());
            })
            .verify();
    }

}