package com.jazztech.cardholderapi.controller.response;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record LimitUpdateResponse(UUID cardId, BigDecimal updatedLimit) {
}
