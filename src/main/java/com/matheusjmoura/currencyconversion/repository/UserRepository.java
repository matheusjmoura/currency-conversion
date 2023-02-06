package com.matheusjmoura.currencyconversion.repository;

import com.matheusjmoura.currencyconversion.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveSortingRepository<User, UUID> {

    Flux<User> findAllBy(Pageable pageable);

    Mono<Boolean> existsByName(String name);

}
