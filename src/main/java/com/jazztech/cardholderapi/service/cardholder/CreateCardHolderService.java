package com.jazztech.cardholderapi.service.cardholder;

import com.jazztech.cardholderapi.apiclient.CreditAnalysisClient;
import com.jazztech.cardholderapi.apiclient.dtos.CreditAnalysisDTO;
import com.jazztech.cardholderapi.controller.request.CardHolderRequest;
import com.jazztech.cardholderapi.controller.response.CardHolderResponse;
import com.jazztech.cardholderapi.handler.exceptions.CardHolderAlreadyRegisteredException;
import com.jazztech.cardholderapi.handler.exceptions.ClientDoesNotCorrespondToCreditAnalysisException;
import com.jazztech.cardholderapi.handler.exceptions.CreditAnalysisNotApprovedException;
import com.jazztech.cardholderapi.handler.exceptions.CreditAnalysisNotFoundException;
import com.jazztech.cardholderapi.mapper.CardHolderMapper;
import com.jazztech.cardholderapi.model.cardholder.CardHolderModel;
import com.jazztech.cardholderapi.repository.CardHolderRepository;
import com.jazztech.cardholderapi.repository.entity.cardholder.CardHolderEntity;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCardHolderService {
    private final CreditAnalysisClient creditAnalysisClient;
    private final CardHolderMapper cardHolderMapper;
    private final CardHolderRepository cardHolderRepository;

    public CardHolderResponse createCardHolder(CardHolderRequest cardHolderRequest) {
        final CardHolderModel cardHolderModel = cardHolderMapper.modelFromRequest(cardHolderRequest);
        final CreditAnalysisDTO creditAnalysisDTO = getCreditAnalysisById(cardHolderModel);
        final CardHolderModel cardHolderModelUpdated = cardHolderModel.updateStatusAndCreditLimit(creditAnalysisDTO.approvedLimit());
        final CardHolderEntity cardHolderEntity = cardHolderMapper.entityFromModel(cardHolderModelUpdated);
        final CardHolderEntity cardHolderEntitySaved = saveClient(cardHolderEntity);
        return cardHolderMapper.responseFromEntity(cardHolderEntitySaved);
    }

    private CreditAnalysisDTO getCreditAnalysisById(CardHolderModel cardHolderModel) {
        final CreditAnalysisDTO creditAnalysisDTO = creditAnalysisClient.getCreditAnalysisById(cardHolderModel.creditAnalysisId());
        if (Objects.isNull(creditAnalysisDTO.id())) {
            throw new CreditAnalysisNotFoundException("Credit analysis not found by ID %s".formatted(cardHolderModel.creditAnalysisId()));
        } else if (!cardHolderModel.clientId().equals(creditAnalysisDTO.clientId())) {
            throw new ClientDoesNotCorrespondToCreditAnalysisException(
                    "clientId %s does not correspond to credit analysisId %s".formatted(cardHolderModel.clientId(), creditAnalysisDTO.id()));
        } else if (Boolean.FALSE.equals(creditAnalysisDTO.approved())) {
            throw new CreditAnalysisNotApprovedException("The credit analysis %s wasn't approved".formatted(cardHolderModel.creditAnalysisId()));
        }
        return creditAnalysisDTO;
    }

    private CardHolderEntity saveClient(CardHolderEntity cardHolderEntity) {
        try {
            return cardHolderRepository.save(cardHolderEntity);
        } catch (DataIntegrityViolationException dive) {
            throw new CardHolderAlreadyRegisteredException("Card Holder already registered, check the data sent for registration");
        }
    }
}
