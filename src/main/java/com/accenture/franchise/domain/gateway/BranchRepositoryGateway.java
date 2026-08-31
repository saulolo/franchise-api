package com.accenture.franchise.domain.gateway;

import com.accenture.franchise.domain.model.branch.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepositoryGateway {

    /**
     * Verifica si existe una sucursal por su identificador único.
     *
     * @param id el identificador de la sucursal
     * @return un Mono que emite true si la sucursal existe, o false en caso contrario
     */
    Mono<Boolean> existsById(Long id);

    /**
     * Guarda una nueva sucursal en el repositorio.
     *
     * @param branch la entidad de dominio de sucursal a guardar
     * @return un Mono que emite la sucursal persistida
     */
    Mono<Branch> save(Branch branch);

    /**
     * Busca una sucursal por su identificador único.
     *
     * @param id el identificador de la sucursal
     * @return un Mono que emite la sucursal si existe, o vacío en caso contrario
     */
    Mono<Branch> findById(Long id);

    /**
     * Actualiza el nombre de una sucursal específica.
     *
     * @param id el identificador de la sucursal
     * @param newName el nuevo nombre a establecer
     * @return un Mono que emite la sucursal actualizada
     */
    Mono<Branch> updateName(Long id, String newName);

    /**
     * Obtiene todas las sucursales pertenecientes a una franquicia.
     *
     * @param franchiseId el identificador de la franquicia
     * @return un Flux con las sucursales asociadas
     */
    Flux<Branch> findByFranchiseId(Long franchiseId);

}
