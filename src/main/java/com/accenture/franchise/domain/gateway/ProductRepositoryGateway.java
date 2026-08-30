package com.accenture.franchise.domain.gateway;

import com.accenture.franchise.domain.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepositoryGateway {

    /**
     * Guarda o actualiza un producto en el repositorio.
     *
     * @param product la entidad de producto a guardar
     * @return un Mono que emite el producto guardado
     */
    Mono<Product> save(Product product);

    /**
     * Busca un producto por su identificador único.
     *
     * @param id el identificador del producto
     * @return un Mono que emite el producto si se encuentra, o vacío en caso contrario
     */
    Mono<Product> findById(Long id);

    /**
     * Elimina un producto por su identificador único.
     *
     * @param id el identificador del producto
     * @return un Mono que indica la finalización cuando termina el borrado
     */
    Mono<Void> delete(Long id);

    /**
     * Actualiza la cantidad de stock de un producto específico.
     *
     * @param id el identificador del producto
     * @param newStock la nueva cantidad de stock a establecer
     * @return un Mono que emite el producto actualizado
     */
    Mono<Product> updateStock(Long id, Integer newStock);

    /**
     * Actualiza el nombre de un producto específico.
     *
     * @param id el identificador del producto
     * @param newName el nuevo nombre a establecer
     * @return un Mono que emite el producto actualizado
     */
    Mono<Product> updateName(Long id, String newName);

    /**
     * Obtiene todos los productos pertenecientes a una sucursal específica.
     *
     * @param branchId el identificador de la sucursal
     * @return un Flux que emite los productos asociados a la sucursal
     */
    Flux<Product> findByBranchId(Long branchId);
}
