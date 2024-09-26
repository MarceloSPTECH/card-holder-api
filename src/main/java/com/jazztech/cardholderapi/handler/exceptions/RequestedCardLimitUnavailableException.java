package com.jazztech.cardholderapi.handler.exceptions;

public class RequestedCardLimitUnavailableException extends RuntimeException {
    public RequestedCardLimitUnavailableException(String message) {
        super(message);
    }
}
