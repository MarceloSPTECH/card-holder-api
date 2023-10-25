package com.jazztech.cardholderapi.handler.exceptions;

public class CreditCardNotFoundException extends RuntimeException {
    public CreditCardNotFoundException(String message) {
        super(message);
    }
}
