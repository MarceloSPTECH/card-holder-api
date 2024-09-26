package com.jazztech.cardholderapi.handler.exceptions;

public class ClientDoesNotCorrespondToCreditAnalysisException extends RuntimeException {
    public ClientDoesNotCorrespondToCreditAnalysisException(String message) {
        super(message);
    }
}
