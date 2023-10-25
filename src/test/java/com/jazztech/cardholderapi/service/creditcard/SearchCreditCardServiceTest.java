package com.jazztech.cardholderapi.service.creditcard;

import static com.jazztech.cardholderapi.service.cardholder.CardHolderFactory.cardHolderEntityFactory;
import static com.jazztech.cardholderapi.service.creditcard.CreditCardFactory.creditCardEntityFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.jazztech.cardholderapi.controller.response.CreditCardResponse;
import com.jazztech.cardholderapi.handler.exceptions.NoCreditCardsFoundException;
import com.jazztech.cardholderapi.mapper.CreditCardMapper;
import com.jazztech.cardholderapi.mapper.CreditCardMapperImpl;
import com.jazztech.cardholderapi.repository.CreditCardRepository;
import com.jazztech.cardholderapi.repository.entity.creditcard.CreditCardEntity;
import com.jazztech.cardholderapi.service.ServiceVerifications;
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
class SearchCreditCardServiceTest {

    @Captor
    ArgumentCaptor<UUID> cardHolderIdCaptor;
    @Captor
    ArgumentCaptor<UUID> creditCardIdCaptor;

    @Mock
    private ServiceVerifications serviceVerifications;
    @Mock
    private CreditCardRepository creditCardRepository;

    @Spy
    private CreditCardMapperImpl creditCardMapper;

    @InjectMocks
    private SearchCreditCardService searchCreditCardService;

    @Test
    void should_return_a_list_of_credit_card_if_it_exists() {
        final List<CreditCardEntity> creditCardEntities = Collections.singletonList(creditCardEntityFactory());
        when(creditCardRepository.findAllByCardHolderId(cardHolderIdCaptor.capture())).thenReturn(creditCardEntities);

        final List<CreditCardResponse> creditCardResponses =
                searchCreditCardService.getAllCardsByCardHolderId(creditCardEntityFactory().getCardHolderId());
        assertNotNull(creditCardResponses);
        assertEquals(creditCardEntities.size(), creditCardResponses.size());
        assertEquals(creditCardEntities.get(0).getCvv(), creditCardResponses.get(0).cvv());
        assertEquals(creditCardEntities.get(0).getDueDate(), creditCardResponses.get(0).dueDate());
        assertEquals(creditCardEntities.get(0).getCardLimit(), creditCardResponses.get(0).limit());
    }

    @Test
    void should_throw_NoCreditCardsFoundException() {
        when(creditCardRepository.findAllByCardHolderId(cardHolderIdCaptor.capture())).thenReturn(Collections.emptyList());

        final NoCreditCardsFoundException exception = assertThrows(NoCreditCardsFoundException.class,
                () -> searchCreditCardService.getAllCardsByCardHolderId(creditCardEntityFactory().getCardHolderId()));

        assertEquals("No credit card found, check the clientId", exception.getMessage());
    }

    @Test
    void should_return_a_credit_card_if_it_exists() {
        when(serviceVerifications.getCreditCardById(cardHolderIdCaptor.capture(), creditCardIdCaptor.capture())).thenReturn(creditCardEntityFactory());

        final CreditCardResponse creditCardResponse =
                searchCreditCardService.getCreditCardById(creditCardEntityFactory().getCardHolderId(), creditCardEntityFactory().getId());

        assertNotNull(creditCardResponse);
        assertEquals(creditCardEntityFactory().getId(), creditCardResponse.cardId());
        assertEquals(creditCardEntityFactory().getCvv(), creditCardResponse.cvv());
        assertEquals(creditCardEntityFactory().getDueDate(), creditCardResponse.dueDate());
        assertEquals(creditCardEntityFactory().getCardLimit(), creditCardResponse.limit());
    }
}