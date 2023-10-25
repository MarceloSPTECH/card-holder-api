package com.jazztech.cardholderapi.handler.exceptions;

public class InvalidCardHolderStatusException extends RuntimeException {
    public InvalidCardHolderStatusException(String message) {
        super(message);
    }
}
