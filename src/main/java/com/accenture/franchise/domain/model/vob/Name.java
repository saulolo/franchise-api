package com.accenture.franchise.domain.model.vob;

import com.accenture.franchise.domain.model.exception.BusinessRulesOnFieldsException;
import com.accenture.franchise.shared.utils.Constants;

/**
 * Value Object para validar que los nombres tengan entre 3 y 30 caracteres
 * y contengan únicamente texto sin caracteres numéricos.
 */
public record Name(String value) {

    public Name {
        if (value == null || !value.trim().matches(Constants.NAME_REGEX)) {
            throw new BusinessRulesOnFieldsException(Constants.NAME_VALIDATION_ERROR);
        }
        value = value.trim();
    }
}
