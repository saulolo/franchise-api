package com.accenture.franchise.infrastructure.entrypoints.rest.dto.response;

import lombok.Builder;

@Builder
public record FranchiseResponseDTO(
        Long id,
        String name,
        String createdAt,
        String updatedAt
) {
}
