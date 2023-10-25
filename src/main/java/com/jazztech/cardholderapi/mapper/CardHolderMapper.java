package com.jazztech.cardholderapi.mapper;

import com.jazztech.cardholderapi.controller.request.CardHolderRequest;
import com.jazztech.cardholderapi.controller.response.CardHolderResponse;
import com.jazztech.cardholderapi.model.cardholder.CardHolderModel;
import com.jazztech.cardholderapi.repository.entity.cardholder.CardHolderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardHolderMapper {

    CardHolderModel modelFromRequest(CardHolderRequest cardHolderRequest);

    CardHolderEntity entityFromModel(CardHolderModel cardHolderModel);

    CardHolderResponse responseFromEntity(CardHolderEntity cardHolderEntity);

    CardHolderModel modelFromEntity(CardHolderEntity cardHolderEntity);

}
