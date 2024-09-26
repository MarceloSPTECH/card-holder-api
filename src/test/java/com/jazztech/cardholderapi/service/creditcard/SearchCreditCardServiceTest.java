package com.jazztech.cardholderapi.service.creditcard;

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
import java.util.Collections;
import java.util.List;
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
    ArgumentCaptor<UUID> uuidCaptor;

    @Mock
    private CreditCardRepository creditCardRepository;

    @Spy
    private CreditCardMapper creditCardMapper = new CreditCardMapperImpl();

    @InjectMocks
    private SearchCreditCardService searchCreditCardService;

    @Test
    void should_return_a_list_of_credit_card_if_it_exists() {
        final List<CreditCardEntity> creditCardEntities = List.of(creditCardEntityFactory());
        when(creditCardRepository.findAllByCardHolderId(uuidCaptor.capture())).thenReturn(creditCardEntities);

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
        when(creditCardRepository.findAllByCardHolderId(uuidCaptor.capture())).thenReturn(Collections.emptyList());

        final NoCreditCardsFoundException exception = assertThrows(NoCreditCardsFoundException.class,
                () -> searchCreditCardService.getAllCardsByCardHolderId(creditCardEntityFactory().getCardHolderId()));

        assertEquals("No credit card found, check the clientId", exception.getMessage());
    }
}