package com.jazztech.cardholderapi.service.cardholder;

import static com.jazztech.cardholderapi.service.cardholder.CardHolderFactory.cardHolderEntityFactory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.jazztech.cardholderapi.controller.response.CardHolderResponse;
import com.jazztech.cardholderapi.handler.exceptions.InvalidCardHolderStatusException;
import com.jazztech.cardholderapi.mapper.CardHolderMapperImpl;
import com.jazztech.cardholderapi.repository.CardHolderRepository;
import com.jazztech.cardholderapi.repository.entity.cardholder.CardHolderEntity;
import com.jazztech.cardholderapi.service.ServiceVerifications;
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
class SearchCardHolderServiceTest {

    @Mock
    private ServiceVerifications serviceVerifications;
    @Mock
    private CardHolderRepository cardHolderRepository;

    @Spy
    private CardHolderMapperImpl cardHolderMapper;

    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;
    @Captor
    private ArgumentCaptor<CardHolderEntity.Status> cardHolderStatus;

    @InjectMocks
    private SearchCardHolderService searchCardHolderService;

    @Test
    void should_return_all_card_holders() {
        final List<CardHolderEntity> cardHolderEntities = Collections.singletonList(cardHolderEntityFactory());
        when(cardHolderRepository.findAll()).thenReturn(cardHolderEntities);

        final List<CardHolderResponse> cardHolderResponses = searchCardHolderService.getAllCardholders();
        assertNotNull(cardHolderResponses);
        assertEquals(cardHolderEntities.size(), cardHolderResponses.size());
        assertEquals(CardHolderResponse.Status.ACTIVE, cardHolderResponses.get(0).status());
        assertEquals(cardHolderEntities.get(0).getId(), cardHolderResponses.get(0).id());
        assertEquals(cardHolderEntities.get(0).getCreditLimit(), cardHolderResponses.get(0).creditLimit());
    }

    @Test
    void should_return_all_card_holders_by_status() {
        when(cardHolderRepository.findAllByStatus(cardHolderStatus.capture())).thenReturn(Collections.singletonList(cardHolderEntityFactory()));
        final List<CardHolderResponse> cardHolderResponses = searchCardHolderService.getAllCardholdersByStatus("active");
        assertNotNull(cardHolderResponses);
        assertEquals(CardHolderResponse.Status.ACTIVE, cardHolderResponses.get(0).status());
    }

    @Test
    void should_throw_InvalidCardHolderStatusException() {
//        when(cardHolderRepository.findAllByStatus(cardHolderStatus.capture())).thenThrow(IllegalArgumentException.class);
        final InvalidCardHolderStatusException exception =
                assertThrows(InvalidCardHolderStatusException.class, () -> searchCardHolderService.getAllCardholdersByStatus("dsadsa"));
        assertEquals("The informed card holder status is invalid.", exception.getMessage());

    }

    @Test
    void should_return_a_card_holder_if_it_exists_by_id() {
        when(serviceVerifications.getCardHolderById(uuidCaptor.capture())).thenReturn(cardHolderEntityFactory());
        final CardHolderResponse cardHolderResponse = searchCardHolderService.getCardHolderById(cardHolderEntityFactory().getId());
        assertEquals(cardHolderResponse.id(), uuidCaptor.getValue());
        assertEquals(cardHolderEntityFactory().getCreditLimit(), cardHolderResponse.creditLimit());
    }


}