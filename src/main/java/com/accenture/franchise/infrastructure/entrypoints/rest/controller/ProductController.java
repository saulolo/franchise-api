package com.accenture.franchise.infrastructure.entrypoints.rest.controller;

import com.accenture.franchise.domain.usecase.ProductUseCase;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.request.NameUpdateRequestDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.request.ProductCreateRequestDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.request.StockUpdateRequestDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.ApiResponseDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.ProductResponseDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.mapper.DtoMapper;
import com.accenture.franchise.shared.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductUseCase productUseCase;

    @PostMapping("/branches/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiResponseDTO<ProductResponseDTO>> createProduct(
            @PathVariable Long branchId,
            @Valid @RequestBody ProductCreateRequestDTO request) {
        log.info("Creando producto '{}' con stock {} en sucursal {}", request.getName(), request.getStock(), branchId);
        return productUseCase.createProduct(branchId, request.getName(), request.getStock())
                .map(DtoMapper::toResponse)
                .map(res -> ApiResponseDTO.success(res, HttpStatus.CREATED.value(), Constants.SUCCESS_MSG));
    }

    @DeleteMapping("/branches/{branchId}/products/{productId}")
    public Mono<ApiResponseDTO<Void>> deleteProduct(
            @PathVariable Long branchId,
            @PathVariable Long productId) {
        log.info("Eliminando producto {} de la sucursal {}", productId, branchId);
        return productUseCase.deleteProduct(branchId, productId)
                .thenReturn(ApiResponseDTO.success(null, HttpStatus.OK.value(), "Producto eliminado exitosamente"));
    }

    @PatchMapping("/products/{id}/stock")
    public Mono<ApiResponseDTO<ProductResponseDTO>> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequestDTO request) {
        log.info("Actualizando stock del producto {} a {}", id, request.getStock());
        return productUseCase.updateStock(id, request.getStock())
                .map(DtoMapper::toResponse)
                .map(res -> ApiResponseDTO.success(res, HttpStatus.OK.value(), Constants.SUCCESS_MSG));
    }

    @PatchMapping("/products/{id}/name")
    public Mono<ApiResponseDTO<ProductResponseDTO>> updateName(
            @PathVariable Long id,
            @Valid @RequestBody NameUpdateRequestDTO request) {
        log.info("Actualizando nombre del producto {} a '{}'", id, request.getName());
        return productUseCase.updateProductName(id, request.getName())
                .map(DtoMapper::toResponse)
                .map(res -> ApiResponseDTO.success(res, HttpStatus.OK.value(), Constants.SUCCESS_MSG));
    }

}
