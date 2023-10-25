package com.jazztech.cardholderapi.model;

import java.math.BigDecimal;
import lombok.Builder;

@Builder(toBuilder = true)
public record LimitUpdateModel(BigDecimal limit) {
}
