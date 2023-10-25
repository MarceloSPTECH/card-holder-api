package com.jazztech.cardholderapi.service.creditcard;

import com.jazztech.cardholderapi.controller.request.CreditCardRequest;
import com.jazztech.cardholderapi.controller.request.LimitUpdateRequest;
import com.jazztech.cardholderapi.model.creditcard.CreditCardModel;
import com.jazztech.cardholderapi.repository.entity.creditcard.CreditCardEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CreditCardFactory {

    static final UUID MOCKED_UUID = UUID.fromString("1a17e5da-028e-4bc5-a61c-a1d8bdab1416");
    static final Long MOCKED_CARD_NUMBER = 4505846369294670L;
    static final BigDecimal MOCKED_LIMIT = BigDecimal.valueOf(10000);
    static final Short MOCKED_CVV = 392;
    static final LocalDate MOCKED_DUE_DATE = LocalDate.of(2033,6,26);

    public static CreditCardRequest creditCardRequestFactory() {
        return CreditCardRequest.builder()
                .cardHolderId(MOCKED_UUID)
                .limit(MOCKED_LIMIT)
                .build();
    }

    public static CreditCardEntity creditCardEntityFactory() {
        return CreditCardEntity.builder()
                .id(MOCKED_UUID)
                .cardLimit(MOCKED_LIMIT)
                .cardNumber(MOCKED_CARD_NUMBER)
                .cvv(MOCKED_CVV)
                .dueDate(MOCKED_DUE_DATE)
                .cardHolderId(MOCKED_UUID)
                .build();
    }

    public static CreditCardModel creditCardModelFactory() {
        return CreditCardModel.builder()
                .limit(MOCKED_LIMIT)
                .cardNumber(MOCKED_CARD_NUMBER)
                .cvv(MOCKED_CVV)
                .dueDate(MOCKED_DUE_DATE)
                .cardHolderId(MOCKED_UUID)
                .build();
    }

    public static LimitUpdateRequest limitUpdateRequestFactory() {
        return LimitUpdateRequest.builder()
                .limit(BigDecimal.TEN)
                .build();
    }
}
