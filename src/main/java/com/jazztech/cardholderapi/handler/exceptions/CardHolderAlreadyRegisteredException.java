package com.jazztech.cardholderapi.handler.exceptions;

public class CardHolderAlreadyRegisteredException extends RuntimeException {
    public CardHolderAlreadyRegisteredException(String message) {
        super(message);
    }
}
