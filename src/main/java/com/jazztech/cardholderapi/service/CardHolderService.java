package com.jazztech.cardholderapi.service;

import com.jazztech.cardholderapi.apiclient.CreditAnalysisClient;
import com.jazztech.cardholderapi.apiclient.dtos.CreditAnalysisDTO;
import com.jazztech.cardholderapi.controller.request.CardHolderRequest;
import com.jazztech.cardholderapi.controller.response.CardHolderResponse;
import com.jazztech.cardholderapi.handler.exceptions.CardHolderAlreadyRegisteredException;
import com.jazztech.cardholderapi.handler.exceptions.ClientDoesNotCorrespondToCreditAnalysisException;
import com.jazztech.cardholderapi.handler.exceptions.CreditAnalysisNotApprovedException;
import com.jazztech.cardholderapi.handler.exceptions.CreditAnalysisNotFoundException;
import com.jazztech.cardholderapi.handler.exceptions.InvalidCardHolderStatusException;
import com.jazztech.cardholderapi.mapper.CardHolderMapper;
import com.jazztech.cardholderapi.model.CardHolderModel;
import com.jazztech.cardholderapi.repository.CardHolderRepository;
import com.jazztech.cardholderapi.repository.entity.CardHolderEntity;
import java.util.List;
import java.util.Objects;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardHolderService {
    private final CreditAnalysisClient creditAnalysisClient;
    private final CardHolderMapper cardHolderMapper;
    private final CardHolderRepository cardHolderRepository;

    @Generated
    public CardHolderResponse createCardHolder(CardHolderRequest cardHolderRequest) {
        final CardHolderModel cardHolderModel = cardHolderMapper.modelFromRequest(cardHolderRequest);
        final CreditAnalysisDTO creditAnalysisDTO = getCreditAnalysisById(cardHolderModel);
        final CardHolderModel cardHolderModelUpdated = cardHolderModel.updateStatusAndCreditLimit(creditAnalysisDTO.approvedLimit());
        final CardHolderEntity cardHolderEntity = cardHolderMapper.entityFromModel(cardHolderModelUpdated);
        final CardHolderEntity cardHolderEntitySaved = saveClient(cardHolderEntity);
        return cardHolderMapper.responseFromEntity(cardHolderEntitySaved);
    }

    public CreditAnalysisDTO getCreditAnalysisById(CardHolderModel cardHolderModel) {
        final CreditAnalysisDTO creditAnalysisDTO = creditAnalysisClient.getCreditAnalysisById(cardHolderModel.creditAnalysisId());
        if (Objects.isNull(creditAnalysisDTO.id())) {
            throw new CreditAnalysisNotFoundException("Credit analysis not found by ID %s".formatted(cardHolderModel.creditAnalysisId()));
        } else if (!cardHolderModel.clientId().equals(creditAnalysisDTO.clientId())) {
            throw new ClientDoesNotCorrespondToCreditAnalysisException(
                    "clientId %s does not correspond to credit analysisId %s".formatted(cardHolderModel.clientId(), creditAnalysisDTO.id()));
        } else if (!creditAnalysisDTO.approved()) {
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

    public List<CardHolderResponse> getAllCardholders() {
        final List<CardHolderEntity> cardHolderEntities = cardHolderRepository.findAll();
        return cardHolderEntities.stream().map(cardHolderMapper::responseFromEntity).toList();
    }

    public List<CardHolderResponse> getAllCardholdersByStatus(String statusRequest) {
        final String statusUpperCase = statusRequest.toUpperCase();
        try {
            final CardHolderEntity.Status status = CardHolderEntity.Status.valueOf(statusUpperCase);
            final List<CardHolderEntity> cardHolderEntities = cardHolderRepository.findAllByStatus(status);
            return cardHolderEntities.stream().map(cardHolderMapper::responseFromEntity).toList();
        } catch (IllegalArgumentException e) {
            throw new InvalidCardHolderStatusException("The informed card holder status is invalid.");
        }
    }
}
