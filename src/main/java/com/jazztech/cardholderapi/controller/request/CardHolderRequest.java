package com.jazztech.cardholderapi.controller.request;

import com.jazztech.cardholderapi.utils.ValidationCustom;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record CardHolderRequest(
        @NotNull(message = "clientId cannot be null")
        UUID clientId,
        @NotNull(message = "creditAnalysisId cannot be null")
        UUID creditAnalysisId,
        BankAccountRequest bankAccount
) {

    public CardHolderRequest(UUID clientId, UUID creditAnalysisId, BankAccountRequest bankAccount) {
        this.clientId = clientId;
        this.creditAnalysisId = creditAnalysisId;
        this.bankAccount = bankAccount;
        ValidationCustom.validator(this);
    }

    @Builder(toBuilder = true)
    public record BankAccountRequest(
            @NotNull(message = "account cannot be null")
            @Pattern(regexp = "\\d{8}-\\d", message = "Invalid account, must be 'XXXXXXXX-X'")
            String account,
            @NotNull(message = "agency cannot be null")
            @Pattern(regexp = "\\d{4}", message = "Invalid agency, must be 'XXXX'")
            String agency,
            @NotNull(message = "bankCode cannot be null")
            @Pattern(regexp = "\\d{3}", message = "Invalid bankCode, must be 'XXX'")
            String bankCode
    ) {
        public BankAccountRequest(String account, String agency, String bankCode) {
            this.account = account;
            this.agency = agency;
            this.bankCode = bankCode;
            ValidationCustom.validator(this);
        }
    }
}
