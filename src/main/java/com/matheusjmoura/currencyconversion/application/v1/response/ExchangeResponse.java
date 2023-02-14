package com.matheusjmoura.currencyconversion.application.v1.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.matheusjmoura.currencyconversion.domain.Exchange;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
public class ExchangeResponse {

    @Schema(description = "Exchange ID", example = "a96c7f4d-760d-416a-9828-5c57ed0fb888")
    private UUID id;
    @Schema(description = "User ID", example = "5d851f41-d926-4108-b72d-377bec951c0a")
    private UUID userId;
    @Schema(description = "Origin currency", example = "UAH")
    private String originCurrency;
    @Schema(description = "Origin value", example = "1")
    private BigDecimal originValue;
    @Schema(description = "Destiny currency", example = "BRL")
    private String destinyCurrency;
    @Schema(description = "Destiny value", example = "0.140096")
    private BigDecimal destinyValue;
    @Schema(description = "Exchange tax rate", example = "0.140096")
    private BigDecimal taxRate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Schema(description = "Exchange date time", example = "2023-02-10T13:15:20.4755491")
    private LocalDateTime dateTime;

    public static ExchangeResponse from(Exchange exchange) {
        return new ExchangeResponse(
            exchange.getId(),
            exchange.getUserId(),
            exchange.getOriginCurrency(),
            exchange.getOriginValue(),
            exchange.getDestinyCurrency(),
            exchange.getDestinyValue(),
            exchange.getTaxRate(),
            exchange.getDateTime()
        );
    }

}
