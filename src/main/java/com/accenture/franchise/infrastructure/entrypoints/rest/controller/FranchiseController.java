package com.accenture.franchise.infrastructure.entrypoints.rest.controller;

import com.accenture.franchise.domain.usecase.FranchiseUseCase;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.request.FranchiseCreateRequestDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.request.NameUpdateRequestDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.ApiResponseDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.FranchiseResponseDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.ProductMaxStockResponseDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.mapper.FranchiseMapper;
import com.accenture.franchise.shared.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/franchises")
@RequiredArgsConstructor
@Validated
public class FranchiseController {

    private final FranchiseUseCase franchiseUseCase;


    /**
     * Crea una nueva franquicia en el sistema.
     *
     * @param request el DTO con la información para la creación de la franquicia
     * @return un Mono que emite la respuesta estandarizada con el DTO de la franquicia creada y estado HTTP 201
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiResponseDTO<FranchiseResponseDTO>> createFranchise(
            @Valid @RequestBody FranchiseCreateRequestDTO request) {
        log.info("Creando franquicia con nombre: '{}'", request.getName());
        return franchiseUseCase.createFranchise(request.getName())
                .map(FranchiseMapper::toResponse)
                .map(res -> ApiResponseDTO.success(res, HttpStatus.CREATED.value(), Constants.SUCCESS_MSG));
    }

    /**
     * Actualiza el nombre de una franquicia existente.
     *
     * @param id el identificador de la franquicia
     * @param request el DTO con el nuevo nombre para la franquicia
     * @return un Mono que emite la respuesta estandarizada con el DTO de la franquicia actualizada y estado HTTP 200
     */
    @PatchMapping("/{id}/name")
    public Mono<ApiResponseDTO<FranchiseResponseDTO>> updateName(
            @PathVariable Long id,
            @Valid @RequestBody NameUpdateRequestDTO request) {
        log.info("Actualizando nombre de franquicia ID {} a '{}'", id, request.getName());
        return franchiseUseCase.updateFranchiseName(id, request.getName())
                .map(FranchiseMapper::toResponse)
                .map(res -> ApiResponseDTO.success(res, HttpStatus.OK.value(), Constants.SUCCESS_MSG));
    }

    /**
     * Consulta el producto con mayor stock por cada sucursal asociada a una franquicia.
     *
     * @param id el identificador de la franquicia
     * @return un Mono que emite la respuesta estandarizada con la lista de productos de mayor stock por sucursal y estado HTTP 200
     */
    @GetMapping("/{id}/max-stock-products")
    public Mono<ApiResponseDTO<List<ProductMaxStockResponseDTO>>> getMaxStockProducts(
            @PathVariable Long id) {
        log.info("Consultando productos de mayor stock por sucursal para la franquicia ID {}", id);
        return franchiseUseCase.getMaxStockProducts(id)
                .map(FranchiseMapper::toResponse)
                .collectList()
                .map(res -> ApiResponseDTO.success(res, HttpStatus.OK.value(), Constants.SUCCESS_MSG));
    }

}
