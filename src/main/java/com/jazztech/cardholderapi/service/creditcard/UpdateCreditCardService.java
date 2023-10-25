package com.jazztech.cardholderapi.service.creditcard;

import com.jazztech.cardholderapi.controller.request.LimitUpdateRequest;
import com.jazztech.cardholderapi.controller.response.LimitUpdateResponse;
import com.jazztech.cardholderapi.handler.exceptions.RequestedCardLimitUnavailableException;
import com.jazztech.cardholderapi.mapper.CardHolderMapper;
import com.jazztech.cardholderapi.mapper.LimitUpdateMapper;
import com.jazztech.cardholderapi.model.LimitUpdateModel;
import com.jazztech.cardholderapi.model.cardholder.CardHolderModel;
import com.jazztech.cardholderapi.repository.CreditCardRepository;
import com.jazztech.cardholderapi.repository.entity.cardholder.CardHolderEntity;
import com.jazztech.cardholderapi.repository.entity.creditcard.CreditCardEntity;
import com.jazztech.cardholderapi.service.ServiceVerifications;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateCreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final LimitUpdateMapper limitUpdateMapper;
    private final CardHolderMapper cardHolderMapper;
    private final ServiceVerifications serviceVerifications;

    public LimitUpdateResponse updateCreditCardLimit(UUID cardHolderId, UUID id, LimitUpdateRequest limitUpdateRequest) {
        final LimitUpdateModel limitUpdateModel = limitUpdateMapper.modelFromRequest(limitUpdateRequest);
        final CardHolderEntity cardHolderEntity = serviceVerifications.getCardHolderById(cardHolderId);
        final CardHolderModel cardHolderModel = cardHolderMapper.modelFromEntity(cardHolderEntity);
        final CreditCardEntity creditCardEntity = serviceVerifications.getCreditCardById(cardHolderId, id);
        final CreditCardEntity creditCardEntitySaved = updateLimit(creditCardEntity, limitUpdateModel.limit(), cardHolderModel.creditLimit());
        return limitUpdateMapper.responseFromEntity(creditCardEntitySaved);
    }

    private CreditCardEntity updateLimit(CreditCardEntity creditCardEntity, BigDecimal updateLimit, BigDecimal cardHolderCreditLimit) {
        final BigDecimal availableLimit =
                serviceVerifications.verifyAvailableCardHolderLimit(creditCardEntity.getCardHolderId(), cardHolderCreditLimit);
        final BigDecimal availableLimitUpdated = availableLimit.subtract(creditCardEntity.getCardLimit());

        if (updateLimit.compareTo(availableLimitUpdated) > 0) {
            throw new RequestedCardLimitUnavailableException(
                    "Requested limit %s is greater than available limit %s.".formatted(updateLimit, availableLimitUpdated));
        }
        creditCardRepository.updateCardLimitById(creditCardEntity.getId(), updateLimit);
        return creditCardEntity.toBuilder().cardLimit(updateLimit).build();
    }
}
