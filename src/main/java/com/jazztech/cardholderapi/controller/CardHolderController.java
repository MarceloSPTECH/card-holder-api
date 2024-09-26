package com.jazztech.cardholderapi.controller;

import com.jazztech.cardholderapi.controller.request.CardHolderRequest;
import com.jazztech.cardholderapi.controller.request.CreditCardRequest;
import com.jazztech.cardholderapi.controller.response.CardHolderResponse;
import com.jazztech.cardholderapi.controller.response.CreditCardResponse;
import com.jazztech.cardholderapi.service.cardholder.CreateCardHolderService;
import com.jazztech.cardholderapi.service.cardholder.SearchCardHolderService;
import com.jazztech.cardholderapi.service.creditcard.CreateCreditCardService;
import com.jazztech.cardholderapi.service.creditcard.SearchCreditCardService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1.0/card-holders")
@RequiredArgsConstructor
public class CardHolderController {
    private final CreateCardHolderService createCardHolderService;
    private final SearchCardHolderService searchCardHolderService;
    private final CreateCreditCardService createCreditCardService;
    private final SearchCreditCardService searchCreditCardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardHolderResponse createCardHolder(@RequestBody CardHolderRequest cardHolderRequest) {
        return createCardHolderService.createCardHolder(cardHolderRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CardHolderResponse> getAllCardHolders(@RequestParam(required = false) String status) {
        if (!Objects.isNull(status)) {
            return searchCardHolderService.getAllCardholdersByStatus(status);
        }
        return searchCardHolderService.getAllCardholders();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CardHolderResponse getCardHolderById(@PathVariable UUID id) {
        return searchCardHolderService.getCardHolderById(id);
    }

    @PostMapping("/{cardHolderId}/cards")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCardResponse createCreditCard(@PathVariable UUID cardHolderId, @RequestBody CreditCardRequest creditCardRequest) {
        return createCreditCardService.createCreditCard(cardHolderId, creditCardRequest);
    }

    @GetMapping("/{cardHolderId}/cards")
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCardResponse> creditCardResponses(@PathVariable UUID cardHolderId) {
        return searchCreditCardService.getAllCardsByCardHolderId(cardHolderId);
    }
}
