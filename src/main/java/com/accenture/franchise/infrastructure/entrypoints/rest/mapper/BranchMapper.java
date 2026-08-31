package com.accenture.franchise.infrastructure.entrypoints.rest.mapper;

import com.accenture.franchise.domain.model.branch.Branch;
import com.accenture.franchise.domain.model.product.Product;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.BranchResponseDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.ProductResponseDTO;
import com.accenture.franchise.shared.utils.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class BranchMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);


    private BranchMapper() {}

    private static String formatDate(LocalDateTime date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }

    /**
     * Convierte un modelo de dominio de sucursal en un DTO de respuesta para la API.
     *
     * @param domain el modelo de dominio de la sucursal
     * @return el DTO de respuesta de la sucursal
     * @throws IllegalArgumentException si el dominio es nulo
     */
    public static BranchResponseDTO toResponse(Branch domain) {
        if (domain == null) throw new IllegalArgumentException("No se puede mapear un dominio nulo");
        return BranchResponseDTO.builder()
                .id(domain.getId())
                .franchiseId(domain.getFranchiseId())
                .name(domain.getName().value())
                .createdAt(formatDate(domain.getCreatedAt()))
                .updatedAt(formatDate(domain.getUpdatedAt()))
                .build();
    }




}
