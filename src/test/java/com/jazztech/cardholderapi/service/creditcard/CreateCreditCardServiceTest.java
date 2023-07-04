package com.jazztech.cardholderapi.service.creditcard;

import static com.jazztech.cardholderapi.service.cardholder.CardHolderFactory.cardHolderEntityFactory;
import static com.jazztech.cardholderapi.service.creditcard.CreditCardFactory.creditCardEntityFactory;
import static com.jazztech.cardholderapi.service.creditcard.CreditCardFactory.creditCardRequestFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.jazztech.cardholderapi.controller.request.CreditCardRequest;
import com.jazztech.cardholderapi.controller.response.CreditCardResponse;
import com.jazztech.cardholderapi.handler.exceptions.CardHolderNotFoundException;
import com.jazztech.cardholderapi.handler.exceptions.PathCardHolderDoesNotMatchRequestCardHolderException;
import com.jazztech.cardholderapi.handler.exceptions.RequestedCardLimitUnavailableException;
import com.jazztech.cardholderapi.mapper.CardHolderMapper;
import com.jazztech.cardholderapi.mapper.CardHolderMapperImpl;
import com.jazztech.cardholderapi.mapper.CreditCardMapper;
import com.jazztech.cardholderapi.mapper.CreditCardMapperImpl;
import com.jazztech.cardholderapi.repository.CardHolderRepository;
import com.jazztech.cardholderapi.repository.CreditCardRepository;
import com.jazztech.cardholderapi.repository.entity.creditcard.CreditCardEntity;
import java.math.BigDecimal;
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
class CreateCreditCardServiceTest {

    @Captor
    ArgumentCaptor<CreditCardEntity> cardEntityCaptor;
    @Captor
    ArgumentCaptor<UUID> uuidCaptor;

    @Mock
    private CreditCardRepository creditCardRepository;
    @Mock
    private CardHolderRepository cardHolderRepository;

    @Spy
    private CardHolderMapper cardHolderMapper = new CardHolderMapperImpl();
    @Spy
    private CreditCardMapper creditCardMapper = new CreditCardMapperImpl();

    @InjectMocks
    private CreateCreditCardService createCreditCardService;

    @Test
    void should_create_credit_card() {
        when(cardHolderRepository.findById(uuidCaptor.capture())).thenReturn(Optional.of(cardHolderEntityFactory()));
        when(creditCardRepository.save(cardEntityCaptor.capture())).thenReturn(creditCardEntityFactory());
        final CreditCardResponse creditCardResponse =
                createCreditCardService.createCreditCard(creditCardRequestFactory().cardHolderId(), creditCardRequestFactory());
        assertNotNull(creditCardResponse);
        assertEquals(creditCardResponse.limit(), creditCardRequestFactory().limit());
    }

    @Test
    void should_throw_RequestedCardLimitUnavailableException_when_requested_limit_is_greater_than_available_limit() {
        when(cardHolderRepository.findById(uuidCaptor.capture())).thenReturn(Optional.of(cardHolderEntityFactory()));
        final CreditCardRequest creditCardRequest = creditCardRequestFactory().toBuilder().limit(BigDecimal.valueOf(100_000)).build();

        final RequestedCardLimitUnavailableException exception = assertThrows(RequestedCardLimitUnavailableException.class,
                () -> createCreditCardService.createCreditCard(creditCardRequest.cardHolderId(), creditCardRequest));
        assertEquals("Required limit %s is greater than available limit %s.".formatted(creditCardRequest.limit(),
                cardHolderEntityFactory().getCreditLimit()), exception.getMessage());
    }

    @Test
    void should_throw_PathCardHolderDoesNotMatchRequestCardHolderException() {
        final PathCardHolderDoesNotMatchRequestCardHolderException exception =
                assertThrows(PathCardHolderDoesNotMatchRequestCardHolderException.class,
                        () -> createCreditCardService.createCreditCard(UUID.randomUUID(), creditCardRequestFactory()));
        assertEquals("Path cardholderId doesn't match body cardHolderId", exception.getMessage());
    }

    @Test
    void should_throw_CardHolderNotFoundException() {
        when(cardHolderRepository.findById(uuidCaptor.capture())).thenReturn(Optional.empty());
        final CardHolderNotFoundException exception = assertThrows(CardHolderNotFoundException.class,
                () -> createCreditCardService.createCreditCard(creditCardRequestFactory().cardHolderId(), creditCardRequestFactory()));
        assertEquals("Card Holder not found by id %s".formatted(creditCardRequestFactory().cardHolderId()), exception.getMessage());
    }
}