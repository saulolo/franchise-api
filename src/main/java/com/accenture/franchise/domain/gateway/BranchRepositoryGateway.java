package com.accenture.franchise.domain.gateway;

import reactor.core.publisher.Mono;

public interface BranchRepositoryGateway {

    /**
     * Verifica si existe una sucursal por su identificador único.
     *
     * @param id el identificador de la sucursal
     * @return un Mono que emite true si la sucursal existe, o false en caso contrario
     */
    Mono<Boolean> existsById(Long id);
}
