package com.jazztech.cardholderapi.service;

import static com.jazztech.cardholderapi.service.cardholder.CardHolderFactory.cardHolderEntityFactory;
import static com.jazztech.cardholderapi.service.cardholder.CardHolderFactory.cardHolderModelFactory;
import static com.jazztech.cardholderapi.service.creditcard.CreditCardFactory.creditCardEntityFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.jazztech.cardholderapi.handler.exceptions.CardHolderDoesNotMatchCreditCardException;
import com.jazztech.cardholderapi.handler.exceptions.CardHolderNotFoundException;
import com.jazztech.cardholderapi.handler.exceptions.CreditCardNotFoundException;
import com.jazztech.cardholderapi.mapper.CardHolderMapperImpl;
import com.jazztech.cardholderapi.repository.CardHolderRepository;
import com.jazztech.cardholderapi.repository.CreditCardRepository;
import com.jazztech.cardholderapi.repository.entity.cardholder.CardHolderEntity;
import com.jazztech.cardholderapi.repository.entity.creditcard.CreditCardEntity;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ServiceVerificationsTest {

    @Mock
    private CardHolderRepository cardHolderRepository;
    @Mock
    private CreditCardRepository creditCardRepository;

    @Spy
    private CardHolderMapperImpl cardHolderMapper;

    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;

    @InjectMocks
    private ServiceVerifications serviceVerifications;

    @Test
    void should_throw_CardHolderNotFoundException_if_it_does_not_exist_by_id() {
        final UUID uuid = UUID.randomUUID();
        when(cardHolderRepository.findById(uuidCaptor.capture())).thenReturn(Optional.empty());
        CardHolderNotFoundException exception =
                assertThrows(CardHolderNotFoundException.class, () -> serviceVerifications.getCardHolderById(uuid));
        assertEquals("Card Holder not found by id %s".formatted(uuidCaptor.getValue()), exception.getMessage());
    }

    @Test
    void should_return_card_holder_available_limit() {
        BigDecimal availableLimit = BigDecimal.valueOf(5000);
        final List<CreditCardEntity> creditCardEntities = Collections.singletonList(creditCardEntityFactory());
        when(creditCardRepository.findAllByCardHolderId(uuidCaptor.capture())).thenReturn(creditCardEntities);
        final BigDecimal availableCardHolderLimit =
                serviceVerifications.verifyAvailableCardHolderLimit(cardHolderModelFactory().clientId(), cardHolderModelFactory().creditLimit());
        assertEquals(availableLimit, availableCardHolderLimit);
    }

    @Test
    void should_return_a_card_holder_if_it_exists_by_id() {
        when(cardHolderRepository.findById(uuidCaptor.capture())).thenReturn(Optional.ofNullable(cardHolderEntityFactory()));
        final CardHolderEntity cardHolderEntity = serviceVerifications.getCardHolderById(cardHolderEntityFactory().getId());
        assertEquals(cardHolderEntity.getId(), uuidCaptor.getValue());
        assertEquals(cardHolderEntityFactory().getCreditLimit(), cardHolderEntity.getCreditLimit());
        assertEquals(cardHolderEntityFactory().getStatus(), cardHolderEntity.getStatus());
    }

    @Test
    void should_return_a_credit_card_if_it_exists() {
        when(creditCardRepository.findById(uuidCaptor.capture())).thenReturn(Optional.ofNullable(creditCardEntityFactory()));

        final CreditCardEntity creditCardEntity =
                serviceVerifications.getCreditCardById(creditCardEntityFactory().getCardHolderId(), creditCardEntityFactory().getId());

        assertNotNull(creditCardEntity);
        assertEquals(creditCardEntityFactory().getId(), creditCardEntity.getId());
        assertEquals(creditCardEntityFactory().getCvv(), creditCardEntity.getCvv());
        assertEquals(creditCardEntityFactory().getDueDate(), creditCardEntity.getDueDate());
        assertEquals(creditCardEntityFactory().getCardLimit(), creditCardEntity.getCardLimit());
    }

    @Test
    void should_throw_CreditCardNotFoundException() {
        when(creditCardRepository.findById(uuidCaptor.capture())).thenReturn(Optional.empty());

        final CreditCardNotFoundException exception = assertThrows(CreditCardNotFoundException.class,
                () -> serviceVerifications.getCreditCardById(creditCardEntityFactory().getCardHolderId(), creditCardEntityFactory().getId())
        );
        assertEquals("Credit card not found by id %s".formatted(uuidCaptor.getValue()), exception.getMessage());
    }

    @Test
    void should_throw_CardHolderDoesNotMatchCreditCardException() {
        final Optional<CreditCardEntity> creditCardEntityModified = Optional.ofNullable(creditCardEntityFactory().toBuilder().cardHolderId(UUID.randomUUID()).build());
                when(creditCardRepository.findById(uuidCaptor.capture())).thenReturn(creditCardEntityModified);

        final CardHolderDoesNotMatchCreditCardException exception = assertThrows(CardHolderDoesNotMatchCreditCardException.class,
                () -> serviceVerifications.getCreditCardById(creditCardEntityFactory().getCardHolderId(), creditCardEntityFactory().getId())
        );
        assertEquals("card holder id doesn't match to credit card id", exception.getMessage());
    }
}