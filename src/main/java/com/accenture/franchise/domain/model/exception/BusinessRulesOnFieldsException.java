package com.accenture.franchise.domain.model.exception;

public class BusinessRulesOnFieldsException extends DomainException {
    public BusinessRulesOnFieldsException(String fieldName) {
        super("El siguiente campo no cumple con las reglas de negocio: " + fieldName);
    }
}
