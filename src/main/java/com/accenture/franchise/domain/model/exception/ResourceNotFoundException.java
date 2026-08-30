package com.accenture.franchise.domain.model.exception;

public class ResourceNotFoundException extends DomainException {

    public ResourceNotFoundException(String resourceName) {
        super("No se encontró el recurso: " + resourceName);
    }
}

