package com.jazztech.cardholderapi.handler.exceptions;

public class CardHolderNotFoundException extends RuntimeException {
    public CardHolderNotFoundException(String message) {
        super(message);
    }
}
