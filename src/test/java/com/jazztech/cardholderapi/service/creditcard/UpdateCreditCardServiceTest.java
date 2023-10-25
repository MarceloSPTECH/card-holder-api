package com.jazztech.cardholderapi.service.creditcard;

import static com.jazztech.cardholderapi.service.cardholder.CardHolderFactory.cardHolderEntityFactory;
import static com.jazztech.cardholderapi.service.creditcard.CreditCardFactory.creditCardEntityFactory;
import static com.jazztech.cardholderapi.service.creditcard.CreditCardFactory.limitUpdateRequestFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.jazztech.cardholderapi.controller.response.LimitUpdateResponse;
import com.jazztech.cardholderapi.handler.exceptions.RequestedCardLimitUnavailableException;
import com.jazztech.cardholderapi.mapper.CardHolderMapperImpl;
import com.jazztech.cardholderapi.mapper.CreditCardMapperImpl;
import com.jazztech.cardholderapi.mapper.LimitUpdateMapperImpl;
import com.jazztech.cardholderapi.repository.CreditCardRepository;
import com.jazztech.cardholderapi.repository.entity.creditcard.CreditCardEntity;
import com.jazztech.cardholderapi.service.ServiceVerifications;
import java.math.BigDecimal;
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
class UpdateCreditCardServiceTest {

    @Captor
    ArgumentCaptor<UUID> cardHolderIdCaptor;
    @Captor
    ArgumentCaptor<UUID> creditCardIdCaptor;
    @Captor
    ArgumentCaptor<CreditCardEntity> cardEntityCaptor;
    @Captor
    ArgumentCaptor<BigDecimal> cardHolderLimitCaptor;

    @Mock
    private ServiceVerifications serviceVerifications;
    @Mock
    private CreditCardRepository creditCardRepository;

    @Spy
    private CreditCardMapperImpl creditCardMapper;
    @Spy
    private CardHolderMapperImpl cardHolderMapper;
    @Spy
    private LimitUpdateMapperImpl limitUpdateMapper;

    @InjectMocks
    private UpdateCreditCardService updateCreditCardService;

    @Test
    void should_return_credit_card_updated() {
        final CreditCardEntity creditCardEntityUpdated = creditCardEntityFactory().toBuilder().cardLimit(BigDecimal.TEN).build();
        when(serviceVerifications.getCardHolderById(cardHolderIdCaptor.capture())).thenReturn(cardHolderEntityFactory());
        when(serviceVerifications.getCreditCardById(cardHolderIdCaptor.capture(), creditCardIdCaptor.capture())).thenReturn(
                creditCardEntityFactory());
        when(serviceVerifications.verifyAvailableCardHolderLimit(cardHolderIdCaptor.capture(), cardHolderLimitCaptor.capture())).thenReturn(
                BigDecimal.valueOf(15000));

        final LimitUpdateResponse limitUpdateResponse =
                updateCreditCardService.updateCreditCardLimit(cardHolderEntityFactory().getId(), creditCardEntityFactory().getId(),
                        limitUpdateRequestFactory());

        assertNotNull(limitUpdateResponse);

        assertEquals(creditCardEntityUpdated.getCardLimit(), limitUpdateResponse.updatedLimit());
        assertEquals(creditCardEntityUpdated.getId(), limitUpdateResponse.cardId());
    }

    @Test
    void should_throw_RequestedCardLimitUnavailableException_when_requested_limit_id_greater_the_available() {
        when(serviceVerifications.getCardHolderById(cardHolderIdCaptor.capture())).thenReturn(cardHolderEntityFactory());
        when(serviceVerifications.getCreditCardById(cardHolderIdCaptor.capture(), creditCardIdCaptor.capture())).thenReturn(
                creditCardEntityFactory());
        when(serviceVerifications.verifyAvailableCardHolderLimit(cardHolderIdCaptor.capture(), cardHolderLimitCaptor.capture())).thenReturn(
                BigDecimal.ZERO);

        final RequestedCardLimitUnavailableException exception =
                assertThrows(RequestedCardLimitUnavailableException.class,
                        () -> updateCreditCardService.updateCreditCardLimit(cardHolderEntityFactory().getId(), creditCardEntityFactory().getId(),
                                limitUpdateRequestFactory())
                );
        assertEquals("Requested limit %s is greater than available limit %s.".formatted(limitUpdateRequestFactory().limit(), BigDecimal.valueOf(-10000)), exception.getMessage());
    }
}