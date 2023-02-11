package com.matheusjmoura.currencyconversion.application.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRequest {

    @NotNull(message = "{exchange.userId.notNull}")
    @Schema(description = "User ID", example = "5d851f41-d926-4108-b72d-377bec951c0a", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID userId;

    @NotBlank(message = "{exchange.originCurrency.notBlank}")
    @Size(min = 3, max = 3, message = "{exchange.originCurrency.size}")
    @Schema(description = "Origin currency", example = "UAH", requiredMode = Schema.RequiredMode.REQUIRED)
    private String originCurrency;

    @NotBlank(message = "{exchange.destinyCurrency.notBlank}")
    @Size(min = 3, max = 3, message = "{exchange.destinyCurrency.size}")
    @Schema(description = "Destiny currency", example = "BRL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String destinyCurrency;

    @NotNull(message = "{exchange.originValue.notNull}")
    @Positive(message = "{exchange.originValue.positive}")
    @Schema(description = "Origin value", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal originValue;

    @JsonIgnore
    public boolean isSameCurrency() {
        return getOriginCurrency().equalsIgnoreCase(getDestinyCurrency());
    }

}
