package com.jazztech.cardholderapi.service.creditcard;

import com.jazztech.cardholderapi.controller.request.CreditCardRequest;
import com.jazztech.cardholderapi.controller.response.CreditCardResponse;
import com.jazztech.cardholderapi.handler.exceptions.PathCardHolderDoesNotMatchRequestCardHolderException;
import com.jazztech.cardholderapi.handler.exceptions.RequestedCardLimitUnavailableException;
import com.jazztech.cardholderapi.mapper.CardHolderMapper;
import com.jazztech.cardholderapi.mapper.CreditCardMapper;
import com.jazztech.cardholderapi.model.cardholder.CardHolderModel;
import com.jazztech.cardholderapi.model.creditcard.CreditCardModel;
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
public class CreateCreditCardService {
    private final CardHolderMapper cardHolderMapper;
    private final CreditCardMapper creditCardMapper;
    private final CreditCardRepository creditCardRepository;
    private final ServiceVerifications serviceVerifications;

    public CreditCardResponse createCreditCard(UUID pathCardHolderId, CreditCardRequest creditCardRequest) {
        final CardHolderEntity cardHolderEntity = getCardHolderById(pathCardHolderId, creditCardRequest.cardHolderId());
        final CardHolderModel cardHolderModel = cardHolderMapper.modelFromEntity(cardHolderEntity);
        final CreditCardModel creditCardModel = creditCardMapper.modelFromRequest(creditCardRequest);
        final CreditCardModel creditCardGenerated = verifyAvailableCardHolderLimit(creditCardModel, cardHolderModel.creditLimit());
        final CreditCardEntity creditCardEntity = creditCardMapper.entityFromModel(creditCardGenerated);
        final CreditCardEntity creditCardEntitySaved = creditCardRepository.save(creditCardEntity);
        return creditCardMapper.responseFromEntity(creditCardEntitySaved);
    }

    private CreditCardModel verifyAvailableCardHolderLimit(CreditCardModel creditCardModel, BigDecimal cardHolderCreditLimit) {
        final BigDecimal availableLimit =
                serviceVerifications.verifyAvailableCardHolderLimit(creditCardModel.cardHolderId(), cardHolderCreditLimit);
        if (creditCardModel.limit().compareTo(availableLimit) > 0) {
            throw new RequestedCardLimitUnavailableException(
                    "Requested limit %s is greater than available limit %s.".formatted(creditCardModel.limit(), availableLimit));
        }
        return creditCardModel.generateCreditCard();
    }

    private CardHolderEntity getCardHolderById(UUID pathCardHolderId, UUID cardHolderId) {
        if (!pathCardHolderId.equals(cardHolderId)) {
            throw new PathCardHolderDoesNotMatchRequestCardHolderException("Path cardholderId doesn't match body cardHolderId");
        }
        return serviceVerifications.getCardHolderById(cardHolderId);
    }
}
