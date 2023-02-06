package com.matheusjmoura.currencyconversion.service;

import com.matheusjmoura.currencyconversion.application.v1.request.CreateUserRequest;
import com.matheusjmoura.currencyconversion.application.v1.response.UserResponse;
import com.matheusjmoura.currencyconversion.domain.User;
import com.matheusjmoura.currencyconversion.exception.UserAlreadyExistException;
import com.matheusjmoura.currencyconversion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

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
            .doOnSuccess(userResponse -> log.info("User created with ID: {}.", userResponse.getId()))
            .doOnError(throwable -> log.error("Error creating user. Request body: " + request, throwable));
    }

    public Mono<Page<UserResponse>> findAll(Pageable pageable) {
        return userRepository.findAllBy(pageable)
            .collectList()
            .map(userList -> userList.stream().map(UserResponse::from).collect(Collectors.toList()))
            .zipWith(userRepository.count())
            .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

}
