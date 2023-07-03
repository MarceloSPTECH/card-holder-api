package com.jazztech.cardholderapi.model;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record CardHolderModel(UUID clientId, UUID creditAnalysisId, Status status, BigDecimal creditLimit, BankAccountModel bankAccount) {
    public CardHolderModel updateStatusAndCreditLimit(BigDecimal approvedLimit) {
        return this.toBuilder()
                .status(Status.ACTIVE)
                .creditLimit(approvedLimit)
                .build();
    }

    public enum Status {
        ACTIVE,
        INACTIVE,
    }
}