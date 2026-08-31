package com.accenture.franchise.domain.model.vob;

import com.accenture.franchise.domain.model.exception.BusinessRulesOnFieldsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class NameTest {

    @Test
    @DisplayName("Debe crear un Name exitosamente cuando el valor es válido")
    void shouldCreateNameSuccessfullyWhenValueIsValid() {
        // Arrange & Act
        Name name = new Name("Franquicia Colombia");

        // Assert
        assertNotNull(name);
        assertEquals("Franquicia Colombia", name.value());
    }

    @Test
    @DisplayName("Debe aplicar trim y eliminar espacios en blanco al inicio y al final")
    void shouldTrimValueSuccessfully() {
        // Arrange & Act
        Name name = new Name("   Restaurante Central   ");

        // Assert
        assertEquals("Restaurante Central", name.value());
    }

    @Test
    @DisplayName("Debe lanzar BusinessRulesOnFieldsException cuando el nombre es nulo")
    void shouldThrowExceptionWhenNameIsNull() {
        // Act & Assert
        assertThrows(BusinessRulesOnFieldsException.class, () -> new Name(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "AB",                                      // Menos de 3 caracteres
            "EstaEsUnaCadenaQueTieneMasDeTreintaCaracteresPermitidos", // Más de 30 caracteres
            "Franquicia123",                           // Contiene números
            "Tech #1",                                 // Contiene caracteres especiales no permitidos
            "   "                                      // Solo espacios en blanco
    })


    @DisplayName("Debe lanzar BusinessRulesOnFieldsException cuando el nombre no cumple las reglas de validación")
    void shouldThrowExceptionWhenNameIsInvalid(String invalidName) {
        // Act & Assert
        assertThrows(BusinessRulesOnFieldsException.class, () -> new Name(invalidName));
    }

}