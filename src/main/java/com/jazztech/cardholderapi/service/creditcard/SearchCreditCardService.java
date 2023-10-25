package com.jazztech.cardholderapi.service.creditcard;

import com.jazztech.cardholderapi.controller.response.CreditCardResponse;
import com.jazztech.cardholderapi.handler.exceptions.NoCreditCardsFoundException;
import com.jazztech.cardholderapi.mapper.CreditCardMapper;
import com.jazztech.cardholderapi.repository.CreditCardRepository;
import com.jazztech.cardholderapi.repository.entity.creditcard.CreditCardEntity;
import com.jazztech.cardholderapi.service.ServiceVerifications;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SearchCreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final CreditCardMapper creditCardMapper;
    private final ServiceVerifications serviceVerifications;

    public List<CreditCardResponse> getAllCardsByCardHolderId(UUID cardHolderId) {
        final List<CreditCardEntity> creditCardEntities = creditCardRepository.findAllByCardHolderId(cardHolderId);
        if (creditCardEntities.isEmpty()) {
            throw new NoCreditCardsFoundException("No credit card found, check the clientId");
        }
        return creditCardEntities.stream().map(creditCardMapper::responseFromEntity).toList();
    }

    public CreditCardResponse getCreditCardById(UUID cardHolderId, UUID id) {
        final CreditCardEntity creditCardEntity = serviceVerifications.getCreditCardById(cardHolderId, id);
        return creditCardMapper.responseFromEntity(creditCardEntity);
    }
}
