package com.accenture.franchise.infrastructure.adapter.postgres.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface BranchR2dbcRepository extends ReactiveCrudRepository<Object, Long> {

    /**
     * Verifica mediante una consulta personalizada si existe una sucursal con el identificador dado.
     *
     * @param id el identificador de la sucursal
     * @return un Mono que emite true si la sucursal existe, o false en caso contrario
     */
    @Query("SELECT COUNT(1) > 0 FROM branches WHERE id = :id")
    Mono<Boolean> existsBranchById(Long id);
}
