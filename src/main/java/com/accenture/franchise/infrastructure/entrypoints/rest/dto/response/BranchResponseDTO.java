package com.accenture.franchise.infrastructure.entrypoints.rest.dto.response;

import lombok.Builder;

@Builder
public record BranchResponseDTO(
        Long id,
        Long franchiseId,
        String name,
        String createdAt,
        String updatedAt
) {
}
