package com.matheusjmoura.currencyconversion.service;

import com.matheusjmoura.currencyconversion.application.v1.request.CreateUserRequest;
import com.matheusjmoura.currencyconversion.application.v1.response.UserResponse;
import com.matheusjmoura.currencyconversion.domain.User;
import com.matheusjmoura.currencyconversion.exception.UserAlreadyExistException;
import com.matheusjmoura.currencyconversion.exception.UserNotFoundException;
import com.matheusjmoura.currencyconversion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<UserResponse> create(CreateUserRequest request) {
        return userRepository.existsByName(request.getName())
            .filter(Boolean.FALSE::equals)
            .switchIfEmpty(Mono.error(() -> new UserAlreadyExistException(request.getName())))
            .flatMap(unused -> userRepository.save(User.from(request)))
            .map(UserResponse::from)
            .doOnSuccess(userResponse -> log.info("User created successfully. Response body: {}.", userResponse))
            .doOnError(throwable -> log.error("An error occurred while creating user. Request body: " + request, throwable));
    }

    public Mono<User> getById(UUID userId) {
        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(() -> new UserNotFoundException(userId)))
            .doOnError(throwable -> log.error("An error occurred while fetching user. User ID requested: " + userId, throwable));
    }

}
