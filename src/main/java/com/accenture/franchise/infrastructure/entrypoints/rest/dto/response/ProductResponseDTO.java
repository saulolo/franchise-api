package com.accenture.franchise.infrastructure.entrypoints.rest.dto.response;

import lombok.Builder;

@Builder
public record ProductResponseDTO(
        Long id,
        Long branchId,
        String name,
        Integer stock,
        String createdAt,
        String updatedAt
) {
}
