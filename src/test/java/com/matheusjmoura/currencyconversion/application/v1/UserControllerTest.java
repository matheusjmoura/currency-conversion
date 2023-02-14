package com.matheusjmoura.currencyconversion.application.v1;

import com.matheusjmoura.currencyconversion.application.v1.request.UserRequest;
import com.matheusjmoura.currencyconversion.service.UserService;
import com.matheusjmoura.currencyconversion.stubs.UserRequestStub;
import com.matheusjmoura.currencyconversion.stubs.UserResponseStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private WebTestClient webTestClient;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
            .bindToController(new UserController(userService))
            .configureClient()
            .baseUrl("/api/v1/users")
            .build();
    }

    @Test
    @DisplayName("Should receive and process request to create a new user")
    void create() {
        when(userService.create(any(UserRequest.class))).thenReturn(Mono.just(UserResponseStub.create()));

        webTestClient.post()
            .body(BodyInserters.fromValue(UserRequestStub.create()))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody()
            .jsonPath("$.id").isEqualTo("5d851f41-d926-4108-b72d-377bec951c0a")
            .jsonPath("$.name").isEqualTo("Matheus Moura");

        verify(userService).create(any(UserRequest.class));
    }

}