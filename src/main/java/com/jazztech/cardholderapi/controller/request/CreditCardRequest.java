package com.jazztech.cardholderapi.controller.request;

import com.jazztech.cardholderapi.utils.ValidationCustom;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record CreditCardRequest(
        @NotNull(message = "cardHolderId cannot be null.")
        UUID cardHolderId,
        @NotNull(message = "limit cannot be null.")
        @Positive(message = "The requested credit card limit is invalid.")
        BigDecimal limit
) {
    public CreditCardRequest(UUID cardHolderId, BigDecimal limit) {
        this.cardHolderId = cardHolderId;
        this.limit = limit;
        ValidationCustom.validator(this);
    }
}
