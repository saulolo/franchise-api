package com.accenture.franchise.domain.model.exception;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
