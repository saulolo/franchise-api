package com.accenture.franchise.domain.gateway;

import reactor.core.publisher.Mono;

public interface FranchiseRepositoryGateway {

    /**
     * Verifica si existe una franquicia por su identificador único.
     *
     * @param id el identificador de la franquicia
     * @return un Mono que emite true si la franquicia existe, o false en caso contrario
     */
    Mono<Boolean> existsById(Long id);


}
