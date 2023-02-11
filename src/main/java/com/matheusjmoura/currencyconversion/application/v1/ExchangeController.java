package com.matheusjmoura.currencyconversion.application.v1;

import com.matheusjmoura.currencyconversion.application.common.ApiPageResponse;
import com.matheusjmoura.currencyconversion.application.v1.request.ExchangeRequest;
import com.matheusjmoura.currencyconversion.application.v1.response.ExchangeResponse;
import com.matheusjmoura.currencyconversion.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "Exchange Operations")
@RequestMapping(path = "/api/v1/exchanges")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @PostMapping
    @Operation(summary = "Make an exchange operation between two different currencies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful exchange operation")
    })
    public Mono<ResponseEntity<ExchangeResponse>> exchange(@RequestBody @Valid ExchangeRequest exchangeRequest) {
        log.info("Receiving request to exchange two currencies. Request: {}.", exchangeRequest);

        return exchangeService.exchange(exchangeRequest)
            .map(ResponseEntity::ok);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all exchange operations by user ID using pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exchanges retrieved")
    })
    public Mono<ResponseEntity<ApiPageResponse<ExchangeResponse>>> getAllByUserId(
        @Parameter(name = "userId", description = "User ID", example = "a96c7f4d-760d-416a-9828-5c57ed0fb888")
        @PathVariable UUID userId,
        @ParameterObject Pageable pageable
    ) {
        log.info("Receiving request to get exchange operations by user ID. User ID: {}.", userId);

        return exchangeService.getAllByUserID(userId, pageable)
            .map(ApiPageResponse::from)
            .map(ResponseEntity::ok);
    }

}
