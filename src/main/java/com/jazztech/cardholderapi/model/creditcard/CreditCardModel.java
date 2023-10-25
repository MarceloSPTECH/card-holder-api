package com.jazztech.cardholderapi.model.creditcard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Builder;

@Builder(toBuilder = true)
public record CreditCardModel(BigDecimal limit, Long cardNumber, Short cvv, LocalDate dueDate, UUID cardHolderId) {

    private static final byte CARD_NUMBER_FIRST_DIGIT = 4;
    private static final byte CARD_NUMBER_DIGITS = 15;
    private static final byte CARD_NUMBER_DIGIT_ORIGIN = 0;
    private static final byte CARD_NUMBER_DIGIT_BOUND = 10;

    private static final byte CVV_ORIGIN = 100;
    private static final short CVV_BOUND = 1000;

    private static final byte DUE_DATE_YEARS = 5;

    public CreditCardModel generateCreditCard() {
        return this.toBuilder()
                .cardNumber(generateCardNumber())
                .cvv(generateCVV())
                .dueDate(generateDueDate())
                .build();
    }

    private Long generateCardNumber() {
        final StringBuilder sb = new StringBuilder();
        sb.append(CARD_NUMBER_FIRST_DIGIT);
        for (byte i = 0; i < CARD_NUMBER_DIGITS; i++) {
            final int digit = ThreadLocalRandom.current().nextInt(CARD_NUMBER_DIGIT_ORIGIN,CARD_NUMBER_DIGIT_BOUND);
            sb.append(digit);
        }
        return Long.valueOf(sb.toString());
    }

    private Short generateCVV() {
        return (short) ThreadLocalRandom.current().nextInt(CVV_ORIGIN, CVV_BOUND);
    }

    private LocalDate generateDueDate() {
        return LocalDate.now().plusYears(DUE_DATE_YEARS);
    }
}
