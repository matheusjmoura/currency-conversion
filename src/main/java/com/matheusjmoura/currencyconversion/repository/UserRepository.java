package com.matheusjmoura.currencyconversion.repository;

import com.matheusjmoura.currencyconversion.domain.User;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveSortingRepository<User, UUID> {

    Mono<Boolean> existsByName(String name);

}
