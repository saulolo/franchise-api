package com.accenture.franchise.infrastructure.entrypoints.rest.mapper;

import com.accenture.franchise.domain.model.franchise.Franchise;
import com.accenture.franchise.domain.model.franchise.ProductMaxStock;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.FranchiseResponseDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.ProductMaxStockResponseDTO;
import com.accenture.franchise.shared.utils.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FranchiseMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);


    private FranchiseMapper() {}

    private static String formatDate(LocalDateTime date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }

    /**
     * Convierte un modelo de dominio de franquicia en un DTO de respuesta para la API.
     *
     * @param domain el modelo de dominio de la franquicia
     * @return el DTO de respuesta de la franquicia
     * @throws IllegalArgumentException si el dominio es nulo
     */
    public static FranchiseResponseDTO toResponse(Franchise domain) {
        if (domain == null) throw new IllegalArgumentException("No se puede mapear un dominio nulo");
        return FranchiseResponseDTO.builder()
                .id(domain.getId())
                .name(domain.getName().value())
                .createdAt(formatDate(domain.getCreatedAt()))
                .updatedAt(formatDate(domain.getUpdatedAt()))
                .build();
    }

    /**
     * Convierte un modelo de dominio de producto con stock máximo en un DTO de respuesta para la API.
     *
     * @param domain el modelo de dominio del producto con stock máximo
     * @return el DTO de respuesta del producto con stock máximo
     * @throws IllegalArgumentException si el dominio es nulo
     */
    public static ProductMaxStockResponseDTO toResponse(ProductMaxStock domain) {
        if (domain == null) throw new IllegalArgumentException("No se puede mapear un dominio nulo");
        return ProductMaxStockResponseDTO.builder()
                .branchId(domain.getBranchId())
                .branchName(domain.getBranchName())
                .productId(domain.getProductId())
                .productName(domain.getProductName())
                .stock(domain.getStock())
                .build();
    }


}
