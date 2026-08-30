package com.accenture.franchise.infrastructure.entrypoints.rest.mapper;

import com.accenture.franchise.domain.model.product.Product;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.ProductResponseDTO;
import com.accenture.franchise.shared.utils.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DtoMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);


    private DtoMapper() {}

    private static String formatDate(LocalDateTime date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }

    /**
     * Convierte un modelo de dominio de producto en un DTO de respuesta.
     *
     * @param domain el modelo de dominio del producto
     * @return el DTO de respuesta del producto
     * @throws IllegalArgumentException si el dominio es nulo
     */
    public static ProductResponseDTO toResponse(Product domain) {
        if (domain == null) throw new IllegalArgumentException("No se puede mapear un dominio nulo");
        return ProductResponseDTO.builder()
                .id(domain.getId())
                .branchId(domain.getBranchId())
                .name(domain.getName().value())
                .stock(domain.getStock())
                .createdAt(formatDate(domain.getCreatedAt()))
                .updatedAt(formatDate(domain.getUpdatedAt()))
                .build();

    }


}
