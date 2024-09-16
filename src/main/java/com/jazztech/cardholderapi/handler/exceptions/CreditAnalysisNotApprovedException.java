package com.jazztech.cardholderapi.handler.exceptions;

public class CreditAnalysisNotApprovedException extends RuntimeException {
    public CreditAnalysisNotApprovedException(String message) {
        super(message);
    }
}
