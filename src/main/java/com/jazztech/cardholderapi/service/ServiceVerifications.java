package com.jazztech.cardholderapi.service;

import com.jazztech.cardholderapi.handler.exceptions.CardHolderDoesNotMatchCreditCardException;
import com.jazztech.cardholderapi.handler.exceptions.CardHolderNotFoundException;
import com.jazztech.cardholderapi.handler.exceptions.CreditCardNotFoundException;
import com.jazztech.cardholderapi.mapper.CardHolderMapper;
import com.jazztech.cardholderapi.repository.CardHolderRepository;
import com.jazztech.cardholderapi.repository.CreditCardRepository;
import com.jazztech.cardholderapi.repository.entity.cardholder.CardHolderEntity;
import com.jazztech.cardholderapi.repository.entity.creditcard.CreditCardEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ServiceVerifications {
    private final CreditCardRepository creditCardRepository;
    private final CardHolderRepository cardHolderRepository;
    private final CardHolderMapper cardHolderMapper;

    public BigDecimal verifyAvailableCardHolderLimit(UUID cardHolderId, BigDecimal cardHolderCreditLimit) {
        final List<CreditCardEntity> creditCardEntities = creditCardRepository.findAllByCardHolderId(cardHolderId);
        final BigDecimal usedCreditLimit = creditCardEntities.stream().map(CreditCardEntity::getCardLimit).reduce(BigDecimal.ZERO, BigDecimal::add);
        return cardHolderCreditLimit.subtract(usedCreditLimit);
    }

    public CardHolderEntity getCardHolderById(UUID id) {
        return cardHolderRepository.findById(id).orElseThrow(
                () -> new CardHolderNotFoundException("Card Holder not found by id %s".formatted(id))
        );
    }

    public CreditCardEntity getCreditCardById(UUID cardHolderId, UUID id) {
        final CreditCardEntity creditCardEntity =
                creditCardRepository.findById(id).orElseThrow(() -> new CreditCardNotFoundException("Credit card not found by id %s".formatted(id)));

        if (!creditCardEntity.getCardHolderId().equals(cardHolderId)) {
            throw new CardHolderDoesNotMatchCreditCardException("card holder id doesn't match to credit card id");
        }
        return creditCardEntity;
    }
}
