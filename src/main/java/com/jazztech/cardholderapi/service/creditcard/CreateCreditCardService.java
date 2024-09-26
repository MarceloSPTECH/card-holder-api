package com.jazztech.cardholderapi.service.creditcard;

import com.jazztech.cardholderapi.controller.request.CreditCardRequest;
import com.jazztech.cardholderapi.controller.response.CreditCardResponse;
import com.jazztech.cardholderapi.handler.exceptions.CardHolderNotFoundException;
import com.jazztech.cardholderapi.handler.exceptions.PathCardHolderDoesNotMatchRequestCardHolderException;
import com.jazztech.cardholderapi.handler.exceptions.RequestedCardLimitUnavailableException;
import com.jazztech.cardholderapi.mapper.CardHolderMapper;
import com.jazztech.cardholderapi.mapper.CreditCardMapper;
import com.jazztech.cardholderapi.model.cardholder.CardHolderModel;
import com.jazztech.cardholderapi.model.creditcard.CreditCardModel;
import com.jazztech.cardholderapi.repository.CardHolderRepository;
import com.jazztech.cardholderapi.repository.CreditCardRepository;
import com.jazztech.cardholderapi.repository.entity.cardholder.CardHolderEntity;
import com.jazztech.cardholderapi.repository.entity.creditcard.CreditCardEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateCreditCardService {
    private final CardHolderMapper cardHolderMapper;
    private final CreditCardMapper creditCardMapper;
    private final CardHolderRepository cardHolderRepository;
    private final CreditCardRepository creditCardRepository;

    public CreditCardResponse createCreditCard(UUID pathCardHolderId, CreditCardRequest creditCardRequest) {
        final CardHolderModel cardHolderModel = getCardHolderById(pathCardHolderId, creditCardRequest.cardHolderId());
        final CreditCardModel creditCardModel = creditCardMapper.modelFromRequest(creditCardRequest);
        final CreditCardModel creditCardGenerated = validateCreditLimit(creditCardModel, cardHolderModel);
        final CreditCardEntity creditCardEntity = creditCardMapper.entityFromModel(creditCardGenerated);
        final CreditCardEntity creditCardEntitySaved = creditCardRepository.save(creditCardEntity);
        return creditCardMapper.responseFromEntity(creditCardEntitySaved);
    }

    private CreditCardModel validateCreditLimit(CreditCardModel creditCardModel, CardHolderModel cardHolderModel) {
        final List<CreditCardEntity> creditCardEntities = creditCardRepository.findAllByCardHolderId(creditCardModel.cardHolderId());
        final BigDecimal usedCreditLimit =
                creditCardEntities.stream().map(CreditCardEntity::getCardLimit).reduce(BigDecimal.ZERO, BigDecimal::add);
        final BigDecimal availableLimit = cardHolderModel.creditLimit().subtract(usedCreditLimit);

        if (creditCardModel.limit().compareTo(availableLimit) > 0) {
            throw new RequestedCardLimitUnavailableException(
                    "Required limit %s is greater than available limit %s.".formatted(creditCardModel.limit(), availableLimit)
            );
        }
        return creditCardModel.generateCreditCard();
    }

    private CardHolderModel getCardHolderById(UUID pathCardHolderId, UUID cardHolderId) {
        if (!pathCardHolderId.equals(cardHolderId)) {
            throw new PathCardHolderDoesNotMatchRequestCardHolderException("Path cardholderId doesn't match body cardHolderId");
        }
        final CardHolderEntity cardHolderEntity = cardHolderRepository.findById(cardHolderId).orElseThrow(
                () -> new CardHolderNotFoundException("Card Holder not found by id %s".formatted(cardHolderId))
        );
        return cardHolderMapper.modelFromEntity(cardHolderEntity);
    }
}
