package com.jazztech.cardholderapi.mapper;

import com.jazztech.cardholderapi.controller.request.CreditCardRequest;
import com.jazztech.cardholderapi.controller.response.CreditCardResponse;
import com.jazztech.cardholderapi.model.creditcard.CreditCardModel;
import com.jazztech.cardholderapi.repository.entity.creditcard.CreditCardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {

    CreditCardModel modelFromRequest(CreditCardRequest creditCardRequest);

    @Mapping(source = "limit", target = "cardLimit")
    CreditCardEntity entityFromModel(CreditCardModel creditCardModel);

    @Mapping(source = "id", target = "cardId")
    @Mapping(source = "cardLimit", target = "limit")
    CreditCardResponse responseFromEntity(CreditCardEntity creditCardEntity);

    CreditCardModel modelFromResponse(CreditCardResponse creditCardResponse);
}
