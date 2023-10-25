package com.jazztech.cardholderapi.handler.exceptions;

public class CardHolderDoesNotMatchCreditCardException extends RuntimeException {
    public CardHolderDoesNotMatchCreditCardException(String message) {
        super(message);
    }
}
