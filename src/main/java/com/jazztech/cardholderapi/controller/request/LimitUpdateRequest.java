package com.jazztech.cardholderapi.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Builder;

@Builder(toBuilder = true)
public record LimitUpdateRequest(
        @NotNull(message = "limit cannot be null.")
        @Positive(message = "The requested credit card limit is invalid.")
        BigDecimal limit
) {
}
