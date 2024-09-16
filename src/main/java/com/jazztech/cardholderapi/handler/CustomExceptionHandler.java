package com.jazztech.cardholderapi.handler;

import com.jazztech.cardholderapi.handler.exceptions.CardHolderAlreadyRegisteredException;
import com.jazztech.cardholderapi.handler.exceptions.ClientDoesNotCorrespondToCreditAnalysisException;
import com.jazztech.cardholderapi.handler.exceptions.CreditAnalysisNotFoundException;
import com.jazztech.cardholderapi.handler.exceptions.InvalidCardHolderStatusException;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final URI NOT_FOUND_URI = URI.create("http://jazztech.com/not-found");

    @ExceptionHandler(CreditAnalysisNotFoundException.class)
    public ProblemDetail creditAnalysisNotFoundExceptionHandler(CreditAnalysisNotFoundException e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        problemDetail.setType(NOT_FOUND_URI);
        problemDetail.setTitle("Credit Analysis Not Found");
        return problemDetail;
    }

    @ExceptionHandler(ClientDoesNotCorrespondToCreditAnalysisException.class)
    public ProblemDetail clientDoesNotCorrespondToCreditAnalysisExceptionHandler(ClientDoesNotCorrespondToCreditAnalysisException e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        problemDetail.setType(URI.create("http://jazztech.com/client-does-not-correspond-credit-analysis"));
        problemDetail.setTitle("Client Doesn't Correspond To Credit Analysis");
        return problemDetail;
    }

    @ExceptionHandler(CardHolderAlreadyRegisteredException.class)
    public ProblemDetail cardHolderAlreadyRegisteredExceptionHandler(CardHolderAlreadyRegisteredException e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getLocalizedMessage());
        problemDetail.setType(URI.create("http://jazztech.com/already-exists"));
        problemDetail.setTitle("Card Holder Already Exists");
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail constraintViolationExceptionHandler(ConstraintViolationException e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        problemDetail.setType(URI.create("http://jazztech.com/invalid-argument"));
        problemDetail.setTitle("Invalid Fields");
        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        problemDetail.setType(URI.create("http://jazztech.com/invalid-argument"));
        problemDetail.setTitle("Invalid Fields");
        return problemDetail;
    }

    @ExceptionHandler(InvalidCardHolderStatusException.class)
    public ProblemDetail invalidStatusExceptionHandler(InvalidCardHolderStatusException e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        problemDetail.setType(URI.create("http://jazztech.com/invalid-status"));
        problemDetail.setTitle("Invalid Status");
        return problemDetail;
    }
}
