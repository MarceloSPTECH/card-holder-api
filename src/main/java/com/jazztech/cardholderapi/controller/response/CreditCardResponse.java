package com.jazztech.cardholderapi.controller.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

public record CreditCardResponse(UUID cardId, BigDecimal limit, String cardNumber, Short cvv, LocalDate dueDate) {

    private static final int FOUR = 4;
    private static final int EIGHT = 8;
    private static final int TWELVE = 12;
    private static final String WHITE_SPACE = " ";

    @Builder(toBuilder = true)
    public CreditCardResponse(UUID cardId, BigDecimal limit, String cardNumber, Short cvv, LocalDate dueDate) {
        this.cardId = cardId;
        this.limit = limit;
        this.cardNumber = formatCardNumber(cardNumber);
        this.cvv = cvv;
        this.dueDate = dueDate;
    }

    private String formatCardNumber(String cardNumber) {
        return cardNumber.substring(0, FOUR) + WHITE_SPACE
                +
                cardNumber.substring(FOUR, EIGHT) + WHITE_SPACE
                +
                cardNumber.substring(EIGHT, TWELVE) + WHITE_SPACE
                +
                cardNumber.substring(TWELVE);
    }
}
