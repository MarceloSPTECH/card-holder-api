package com.jazztech.cardholderapi.controller;

import com.jazztech.cardholderapi.controller.request.CardHolderRequest;
import com.jazztech.cardholderapi.controller.response.CardHolderResponse;
import com.jazztech.cardholderapi.service.CardHolderService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final CardHolderService cardHolderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardHolderResponse createCardHolder(@RequestBody CardHolderRequest cardHolderRequest) {
        return cardHolderService.createCardHolder(cardHolderRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CardHolderResponse> getAllCardHolders(@RequestParam(required = false) String status) {
        if (Objects.isNull(status)) {
            return cardHolderService.getAllCardholders();
        } else {
            return cardHolderService.getAllCardholdersByStatus(status);
        }
    }

}
