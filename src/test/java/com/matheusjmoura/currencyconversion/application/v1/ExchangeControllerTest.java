package com.matheusjmoura.currencyconversion.application.v1;

import com.matheusjmoura.currencyconversion.application.v1.request.ExchangeRequest;
import com.matheusjmoura.currencyconversion.service.ExchangeService;
import com.matheusjmoura.currencyconversion.stubs.ExchangeRequestStub;
import com.matheusjmoura.currencyconversion.stubs.ExchangeResponseStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class ExchangeControllerTest {

    private WebTestClient webTestClient;
    @Mock
    private ExchangeService exchangeService;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
            .bindToController(new ExchangeController(exchangeService))
            .argumentResolvers(argumentResolverConfigurer -> argumentResolverConfigurer
                .addCustomResolver(new ReactivePageableHandlerMethodArgumentResolver()))
            .configureClient()
            .baseUrl("/api/v1/exchanges")
            .build();
    }

    @Test
    @DisplayName("Should receive and process request to make an exchange operation between two different currencies")
    void exchange() {
        when(exchangeService.exchange(any(ExchangeRequest.class))).thenReturn(Mono.just(ExchangeResponseStub.create()));

        webTestClient.post()
            .body(BodyInserters.fromValue(ExchangeRequestStub.create()))
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo("a96c7f4d-760d-416a-9828-5c57ed0fb888");

        verify(exchangeService).exchange(any(ExchangeRequest.class));
    }

    @Test
    @DisplayName("Should receive and process request to get all exchange operations by user ID using pagination")
    void getAllByUserId() {
        UUID uuid = UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a");
        Pageable pageable = Pageable.ofSize(20);

        when(exchangeService.getAllByUserID(uuid, pageable))
            .thenReturn(Mono.just(ExchangeResponseStub.createPageResponse(pageable)));

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/user/" + uuid)
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .build())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .jsonPath("$.totalElements").isEqualTo(1)
            .jsonPath("$.content[0].id").isNotEmpty();

        verify(exchangeService).getAllByUserID(uuid, pageable);
    }

}