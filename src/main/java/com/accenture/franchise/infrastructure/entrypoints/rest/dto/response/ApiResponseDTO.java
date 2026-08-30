package com.accenture.franchise.infrastructure.entrypoints.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"data", "status", "message"})
public record ApiResponseDTO<T>(
        T data,
        int status,
        String message) {

    /**
     * Crea una respuesta exitosa con datos, código de estado y mensaje.
     *
     * @param data los datos de la respuesta
     * @param status el código de estado
     * @param message el mensaje descriptivo
     * @param <T> el tipo de los datos
     * @return una instancia de ApiResponseDTO configurada como éxito
     */
    public static <T> ApiResponseDTO<T> success(T data, int status, String message) {
        return ApiResponseDTO.<T>builder()
                .data(data)
                .status(status)
                .message(message)
                .build();
    }

    /**
     * Crea una respuesta de error con un mensaje y código de estado.
     *
     * @param message el mensaje descriptivo del error
     * @param status el código de estado del error
     * @param <T> el tipo de los datos
     * @return una instancia de ApiResponseDTO configurada como error
     */
    public static <T> ApiResponseDTO<T> error(String message, int status) {
        return ApiResponseDTO.<T>builder()
                .message(message)
                .status(status)
                .build();
    }

}
