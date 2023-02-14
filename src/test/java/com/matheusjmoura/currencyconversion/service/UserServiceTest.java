package com.matheusjmoura.currencyconversion.service;

import com.matheusjmoura.currencyconversion.application.v1.response.UserResponse;
import com.matheusjmoura.currencyconversion.domain.User;
import com.matheusjmoura.currencyconversion.exception.UserAlreadyExistException;
import com.matheusjmoura.currencyconversion.exception.UserNotFoundException;
import com.matheusjmoura.currencyconversion.repository.UserRepository;
import com.matheusjmoura.currencyconversion.stubs.UserRequestStub;
import com.matheusjmoura.currencyconversion.stubs.UserStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Create method should verify and save a new user into database")
    void create() {
        when(userRepository.existsByName("Matheus Moura")).thenReturn(Mono.just(Boolean.FALSE));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(UserStub.create()));

        StepVerifier.create(userService.create(UserRequestStub.create()))
            .consumeNextWith(userResponse -> assertThat(userResponse)
                .extracting(UserResponse::getId, UserResponse::getName)
                .containsExactly(UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a"), "Matheus Moura"))
            .verifyComplete();

        verify(userRepository).existsByName("Matheus Moura");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Create method should throw exception if user already exist with name")
    void createWithNameAlreadyExists() {
        when(userRepository.existsByName("Matheus Moura")).thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(userService.create(UserRequestStub.create()))
            .consumeErrorWith(throwable -> {
                assertEquals(UserAlreadyExistException.class, throwable.getClass());
                assertEquals("User already exist with name: Matheus Moura.", throwable.getMessage());
            })
            .verify();

        verify(userRepository).existsByName("Matheus Moura");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Get user by ID should get user data by ID")
    void getById() {
        UUID uuid = UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a");

        when(userRepository.findById(uuid)).thenReturn(Mono.just(UserStub.create()));

        StepVerifier.create(userService.getById(uuid))
            .consumeNextWith(user -> assertThat(user)
                .extracting(User::getId, User::getName, User::getVersion)
                .containsExactly(uuid, "Matheus Moura", 0L))
            .verifyComplete();

        verify(userRepository).findById(uuid);
    }

    @Test
    @DisplayName("Get user by ID should throw exception if no user is found with ID")
    void getByIdNotFound() {
        UUID uuid = UUID.fromString("5d851f41-d926-4108-b72d-377bec951c0a");

        when(userRepository.findById(uuid)).thenReturn(Mono.empty());

        StepVerifier.create(userService.getById(uuid))
            .consumeErrorWith(throwable -> {
                assertEquals(UserNotFoundException.class, throwable.getClass());
                assertEquals("User not found with ID: 5d851f41-d926-4108-b72d-377bec951c0a.", throwable.getMessage());
                assertEquals(uuid, ((UserNotFoundException) throwable).getId());
            })
            .verify();

        verify(userRepository).findById(uuid);
    }

}