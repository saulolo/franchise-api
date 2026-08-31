package com.accenture.franchise.infrastructure.entrypoints.rest.controller;

import com.accenture.franchise.domain.usecase.BranchUseCase;
import com.accenture.franchise.domain.usecase.ProductUseCase;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.request.BranchCreateRequestDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.request.NameUpdateRequestDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.request.ProductCreateRequestDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.request.StockUpdateRequestDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.ApiResponseDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.BranchResponseDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.dto.response.ProductResponseDTO;
import com.accenture.franchise.infrastructure.entrypoints.rest.mapper.BranchMapper;
import com.accenture.franchise.infrastructure.entrypoints.rest.mapper.ProductMapper;
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
public class BranchController {

    private final BranchUseCase branchUseCase;


    /**
     * Crea una nueva sucursal asociada a una franquicia específica.
     *
     * @param franchiseId el identificador de la franquicia
     * @param request el DTO con la información para la creación de la sucursal
     * @return un Mono que emite la respuesta estandarizada con el DTO de la sucursal creada y estado HTTP 201
     */
    @PostMapping("/franchises/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiResponseDTO<BranchResponseDTO>> createBranch(
            @PathVariable Long franchiseId,
            @Valid @RequestBody BranchCreateRequestDTO request) {
        log.info("Creando sucursal '{}' para la franquicia ID {}", request.getName(), franchiseId);
        return branchUseCase.createBranch(franchiseId, request.getName())
                .map(BranchMapper::toResponse)
                .map(res -> ApiResponseDTO.success(res, HttpStatus.CREATED.value(), Constants.SUCCESS_MSG));
    }


    /**
     * Actualiza el nombre de una sucursal existente.
     *
     * @param id el identificador de la sucursal
     * @param request el DTO con el nuevo nombre para la sucursal
     * @return un Mono que emite la respuesta estandarizada con el DTO de la sucursal actualizada y estado HTTP 200
     */
    @PatchMapping("/branches/{id}/name")
    public Mono<ApiResponseDTO<BranchResponseDTO>> updateName(
            @PathVariable Long id,
            @Valid @RequestBody NameUpdateRequestDTO request) {
        log.info("Actualizando nombre de sucursal ID {} a '{}'", id, request.getName());
        return branchUseCase.updateBranchName(id, request.getName())
                .map(BranchMapper::toResponse)
                .map(res -> ApiResponseDTO.success(res, HttpStatus.OK.value(), Constants.SUCCESS_MSG));
    }

}
