package com.accenture.franchise.domain.gateway;

import com.accenture.franchise.domain.model.franchise.Franchise;
import com.accenture.franchise.domain.model.franchise.ProductMaxStock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepositoryGateway {

    /**
     * Verifica si existe una franquicia por su identificador único.
     *
     * @param id el identificador de la franquicia
     * @return un Mono que emite true si la franquicia existe, o false en caso contrario
     */
    Mono<Boolean> existsById(Long id);

    /**
     * Guarda una nueva franquicia en el repositorio.
     *
     * @param franchise la entidad de dominio de franquicia a guardar
     * @return un Mono que emite la franquicia guardada
     */
    Mono<Franchise> save(Franchise franchise);

    /**
     * Busca una franquicia por su identificador único.
     *
     * @param id el identificador de la franquicia
     * @return un Mono que emite la franquicia encontrada o vacío
     */
    Mono<Franchise> findById(Long id);

    /**
     * Actualiza el nombre de una franquicia específica.
     *
     * @param id el identificador de la franquicia
     * @param newName el nuevo nombre a establecer
     * @return un Mono que emite la franquicia actualizada
     */
    Mono<Franchise> updateName(Long id, String newName);

    /**
     * Obtiene el producto con mayor stock por cada sucursal de una franquicia específica.
     *
     * @param franchiseId el identificador de la franquicia
     * @return un Flux con el listado de productos de mayor stock indicando su sucursal
     */
    Flux<ProductMaxStock> findMaxStockProductsByFranchiseId(Long franchiseId);


}
