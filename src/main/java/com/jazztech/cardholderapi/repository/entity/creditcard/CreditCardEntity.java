package com.jazztech.cardholderapi.repository.entity.creditcard;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "CREDIT_CARD")
@Immutable
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class CreditCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    BigDecimal cardLimit;

    Long cardNumber;

    Short cvv;

    LocalDate dueDate;

    UUID cardHolderId;

    public CreditCardEntity() {
    }
}
