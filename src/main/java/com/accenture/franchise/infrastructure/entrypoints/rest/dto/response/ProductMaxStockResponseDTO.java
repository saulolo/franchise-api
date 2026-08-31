package com.accenture.franchise.infrastructure.entrypoints.rest.dto.response;

import lombok.Builder;

@Builder
public record ProductMaxStockResponseDTO(
        Long branchId,
        String branchName,
        Long productId,
        String productName,
        Integer stock
) {
}
