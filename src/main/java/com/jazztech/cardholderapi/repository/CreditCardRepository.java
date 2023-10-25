package com.jazztech.cardholderapi.repository;

import com.jazztech.cardholderapi.repository.entity.creditcard.CreditCardEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCardEntity, UUID> {
    List<CreditCardEntity> findAllByCardHolderId(UUID cardHolderId);

    CreditCardEntity findByIdAndCardHolderId(UUID id, UUID cardHolderId);
}
