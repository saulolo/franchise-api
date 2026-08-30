package com.accenture.franchise.infrastructure.adapter.postgres.repository;

import com.accenture.franchise.infrastructure.adapter.postgres.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface ProductR2dbcRepository extends R2dbcRepository<ProductEntity, Long> {


    /**
     * Busca todos los registros de productos asociados a un identificador de sucursal específico.
     *
     * @param branchId el identificador de la sucursal
     * @return un Flux que emite las entidades de productos pertenecientes a la sucursal
     */
    Flux<ProductEntity> findByBranchId(Long branchId);

}
