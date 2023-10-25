package com.jazztech.cardholderapi.repository;

import com.jazztech.cardholderapi.repository.entity.creditcard.CreditCardEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CreditCardRepository extends JpaRepository<CreditCardEntity, UUID> {

    @Transactional
    @Modifying
    @Query("update CreditCardEntity c set c.cardLimit = ?2 where c.id = ?1")
    void updateCardLimitById(UUID id, BigDecimal cardLimit);

    List<CreditCardEntity> findAllByCardHolderId(UUID cardHolderId);

    CreditCardEntity findByIdAndCardHolderId(UUID id, UUID cardHolderId);
}
