package com.jazztech.cardholderapi.mapper;

import com.jazztech.cardholderapi.controller.request.LimitUpdateRequest;
import com.jazztech.cardholderapi.controller.response.LimitUpdateResponse;
import com.jazztech.cardholderapi.model.LimitUpdateModel;
import com.jazztech.cardholderapi.repository.entity.creditcard.CreditCardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LimitUpdateMapper {


    LimitUpdateModel modelFromRequest(LimitUpdateRequest limitUpdateRequest);

    @Mapping(source = "id", target = "cardId")
    @Mapping(source = "cardLimit", target = "updatedLimit")
    LimitUpdateResponse responseFromEntity(CreditCardEntity creditCardEntity);
}
