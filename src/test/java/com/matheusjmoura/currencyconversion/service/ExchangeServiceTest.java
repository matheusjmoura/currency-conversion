package com.matheusjmoura.currencyconversion.service;

import com.matheusjmoura.currencyconversion.application.v1.response.ExchangeResponse;
import com.matheusjmoura.currencyconversion.domain.Exchange;
import com.matheusjmoura.currencyconversion.exception.IdenticalCurrencyExchangeException;
import com.matheusjmoura.currencyconversion.exception.UserNotFoundException;
import com.matheusjmoura.currencyconversion.repository.ExchangeRepository;
import com.matheusjmoura.currencyconversion.stubs.ExchangeRatesClientResponseStub;
import com.matheusjmoura.currencyconversion.stubs.ExchangeRequestStub;
import com.matheusjmoura.currencyconversion.stubs.ExchangeStub;
import com.matheusjmoura.currencyconversion.stubs.UserStub;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class ExchangeServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private ExchangeRepository exchangeRepository;
    @Mock
    private ExchangeRatesCacheService exchangeRatesCacheService;
    @InjectMocks
    private ExchangeService exchangeService;

    @Test
    @DisplayName("Exchange method should make an exchange operation between two different currencies")
    void exchange() {
        when(userService.getById(UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a")))
            .thenReturn(Mono.just(UserStub.create()));
        when(exchangeRatesCacheService.getExchangeRatesCache())
            .thenReturn(Mono.just(ExchangeRatesClientResponseStub.create().getRates()));
        when(exchangeRepository.save(any(Exchange.class))).thenReturn(Mono.just(ExchangeStub.create(UserStub.create())));

        StepVerifier.create(exchangeService.exchange(ExchangeRequestStub.create()))
            .consumeNextWith(exchangeResponse -> assertThat(exchangeResponse)
                .extracting(ExchangeResponse::getId, ExchangeResponse::getTaxRate)
                .containsExactly(UUID.fromString("a96c7f4d-760d-416a-9828-5c57ed0fb888"), BigDecimal.valueOf(0.140096)))
            .verifyComplete();

        verify(userService).getById(UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a"));
        verify(exchangeRatesCacheService).getExchangeRatesCache();
        verify(exchangeRepository).save(any(Exchange.class));
    }

    @Test
    @DisplayName("Exchange method should throw exception if origin and destiny currency requested are the same")
    void exchangeSameCurrency() {
        StepVerifier.create(exchangeService.exchange(ExchangeRequestStub.createSameCurrency()))
            .consumeErrorWith(throwable -> {
                assertEquals(IdenticalCurrencyExchangeException.class, throwable.getClass());
                assertEquals("Origin and destiny currencies are identical: BRL.", throwable.getMessage());
                assertEquals("BRL", ((IdenticalCurrencyExchangeException) throwable).getCurrency());
            })
            .verify();

        verify(userService, never()).getById(any(UUID.class));
        verify(exchangeRatesCacheService, never()).getExchangeRatesCache();
        verify(exchangeRepository, never()).save(any(Exchange.class));
    }

    @Test
    @DisplayName("Exchange method should throw exception if no user is found with requested ID")
    void exchangeUserNotFound() {
        UUID uuid = UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a");

        when(userService.getById(uuid)).thenReturn(Mono.error(new UserNotFoundException(uuid)));

        StepVerifier.create(exchangeService.exchange(ExchangeRequestStub.create()))
            .consumeErrorWith(throwable -> {
                assertEquals(UserNotFoundException.class, throwable.getClass());
                assertEquals("User not found with ID: 5d851f41-d926-4108-b72d-377bec951c0a.", throwable.getMessage());
                assertEquals(uuid, ((UserNotFoundException) throwable).getId());
            })
            .verify();

        verify(userService).getById(any(UUID.class));
        verify(exchangeRatesCacheService, never()).getExchangeRatesCache();
        verify(exchangeRepository, never()).save(any(Exchange.class));
    }

    @Test
    void getAllByUserID() {
        UUID uuid = UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a");
        Pageable pageable = Pageable.ofSize(20);

        when(userService.getById(uuid)).thenReturn(Mono.just(UserStub.create()));
        when(exchangeRepository.findAllByAndUserId(pageable, uuid)).thenReturn(Flux.just(ExchangeStub.create(UserStub.create())));
        when(exchangeRepository.countAllByUserId(uuid)).thenReturn(Mono.just(1L));

        StepVerifier.create(exchangeService.getAllByUserID(uuid, pageable))
            .consumeNextWith(exchangeResponses -> {
                AssertionsForClassTypes.assertThat(exchangeResponses)
                    .extracting(
                        Page::hasNext,
                        Page::getSize,
                        Page::getNumber,
                        Page::getTotalElements,
                        Page::getTotalPages
                    )
                    .containsExactly(false, 20, 0, 1L, 1);
                assertThat(exchangeResponses)
                    .extracting(
                        ExchangeResponse::getId,
                        ExchangeResponse::getTaxRate
                    )
                    .containsExactly(Tuple.tuple(UUID.fromString("a96c7f4d-760d-416a-9828-5c57ed0fb888"), BigDecimal.valueOf(0.140096)));
            })
            .verifyComplete();

        verify(userService).getById(any(UUID.class));
        verify(exchangeRepository).findAllByAndUserId(pageable, uuid);
        verify(exchangeRepository).countAllByUserId(uuid);
    }

    @Test
    @DisplayName("Get all exchange operations by user ID should throw exception if no user is found with ID")
    void getAllByUserIDNotFound() {
        UUID uuid = UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a");
        Pageable pageable = Pageable.ofSize(20);

        when(userService.getById(uuid)).thenReturn(Mono.error(new UserNotFoundException(uuid)));

        StepVerifier.create(exchangeService.getAllByUserID(uuid, pageable))
            .consumeErrorWith(throwable -> {
                assertEquals(UserNotFoundException.class, throwable.getClass());
                assertEquals("User not found with ID: 5d851f41-d926-4108-b72d-377bec951c0a.", throwable.getMessage());
                assertEquals(uuid, ((UserNotFoundException) throwable).getId());
            })
            .verify();

        verify(userService).getById(any(UUID.class));
        verify(exchangeRepository, never()).findAllByAndUserId(pageable, uuid);
        verify(exchangeRepository, never()).countAllByUserId(uuid);
    }

}