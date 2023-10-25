package com.jazztech.cardholderapi.handler.exceptions;

public class InvalidCreditLimitException extends RuntimeException {
    public InvalidCreditLimitException(String message) {
        super(message);
    }
}
