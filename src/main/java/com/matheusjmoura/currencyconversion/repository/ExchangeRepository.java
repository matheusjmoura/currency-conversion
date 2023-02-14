package com.matheusjmoura.currencyconversion.repository;

import com.matheusjmoura.currencyconversion.domain.Exchange;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ExchangeRepository extends ReactiveSortingRepository<Exchange, UUID> {

    Flux<Exchange> findAllByAndUserId(Pageable pageable, UUID userId);

    Mono<Long> countAllByUserId(UUID userId);

}
