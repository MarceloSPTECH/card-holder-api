package com.jazztech.cardholderapi.apiclient.dtos;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record CreditAnalysisDTO(UUID id, UUID clientId, Boolean approved, BigDecimal approvedLimit) {
}
