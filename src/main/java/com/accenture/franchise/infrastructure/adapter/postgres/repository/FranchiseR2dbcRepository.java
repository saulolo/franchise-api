package com.accenture.franchise.infrastructure.adapter.postgres.repository;

import com.accenture.franchise.domain.model.franchise.ProductMaxStock;
import com.accenture.franchise.infrastructure.adapter.postgres.entity.FranchiseEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseR2dbcRepository extends ReactiveCrudRepository<FranchiseEntity, Long> {

    /**
     * Verifica mediante una consulta personalizada si existe una franquicia con el identificador dado.
     *
     * @param id el identificador de la franquicia
     * @return un Mono que emite true si la franquicia existe, o false en caso contrario
     */
    @Query("SELECT COUNT(1) > 0 FROM franchises WHERE id = :id")
    Mono<Boolean> existsFranchiseById(Long id);

    /**
     * Consulta el producto con mayor stock por cada sucursal de una franquicia dada.
     * Utiliza DISTINCT ON para obtener el registro con mayor stock por id de sucursal.
     *
     * @param franchiseId el identificador de la franquicia
     * @return un Flux con el producto de mayor stock por sucursal
     */
    @Query("""
        SELECT DISTINCT ON (b.id)
            b.id AS branch_id,
            b.name AS branch_name,
            p.id AS product_id,
            p.name AS product_name,
            p.stock AS stock
        FROM branches b
        JOIN products p ON b.id = p.branch_id
        WHERE b.franchise_id = :franchiseId
        ORDER BY b.id, p.stock DESC, p.id ASC
    """)
    Flux<ProductMaxStock> findMaxStockProductsByFranchiseId(Long franchiseId);

}
