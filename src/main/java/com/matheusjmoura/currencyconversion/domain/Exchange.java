package com.matheusjmoura.currencyconversion.domain;

import com.matheusjmoura.currencyconversion.application.v1.request.ExchangeRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Table(name = "\"exchange\"")
@Schema(description = "Exchange Entity")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Exchange {

    @Id
    private UUID id;
    @Column("user_id")
    private UUID userId;
    @Transient
    private User user;
    @Column("origin_currency")
    private String originCurrency;
    @Column("origin_value")
    private BigDecimal originValue;
    @Column("destiny_currency")
    private String destinyCurrency;
    @Column("tax_rate")
    private BigDecimal taxRate;
    @CreatedDate
    @Column("date_time")
    private LocalDateTime dateTime;
    @Version
    Long version;

    public static Exchange from(@NonNull ExchangeRequest request, @NonNull BigDecimal taxRate, @NonNull UUID userId) {
        return new Exchange(
            UUID.randomUUID(),
            userId,
            null,
            request.getOriginCurrency(),
            request.getOriginValue(),
            request.getDestinyCurrency(),
            taxRate,
            null,
            null);
    }

    public BigDecimal getDestinyValue() {
        return originValue.multiply(taxRate);
    }

}
