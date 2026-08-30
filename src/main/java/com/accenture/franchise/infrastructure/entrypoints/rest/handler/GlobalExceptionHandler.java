package com.accenture.franchise.infrastructure.entrypoints.rest.handler;

import com.accenture.franchise.domain.model.exception.BusinessRulesOnFieldsException;
import com.accenture.franchise.domain.model.exception.ResourceNotFoundException;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones cuando no se encuentra un recurso solicitado.
     *
     * @ex la excepción de recurso no encontrado capturada
     * @return una respuesta HTTP 404 con el detalle estandarizado del error
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.error(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    /**
     * Maneja las excepciones relacionadas con reglas de negocio o validaciones de campos.
     *
     * @ex la excepción de reglas de negocio capturada
     * @return una respuesta HTTP 400 con el detalle estandarizado del error
     */
    @ExceptionHandler(BusinessRulesOnFieldsException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleBusiness(BusinessRulesOnFieldsException ex) {
        log.warn("Error de regla de negocio: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleValidation(WebExchangeBindException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Error de validación DTO: {}", errorMsg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.error(errorMsg, HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Maneja cualquier excepción no controlada o error inesperado del servidor.
     *
     * @ex la excepción genérica capturada
     * @return una respuesta HTTP 500 con un mensaje genérico de error interno
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleInternal(Exception ex) {
        log.error("Error inesperado: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error("Ha ocurrido un error interno en el servidor", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

}
