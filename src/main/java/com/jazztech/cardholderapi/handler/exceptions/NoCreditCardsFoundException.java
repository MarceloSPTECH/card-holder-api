package com.jazztech.cardholderapi.handler.exceptions;

public class NoCreditCardsFoundException extends RuntimeException {
    public NoCreditCardsFoundException(String message) {
        super(message);
    }
}
