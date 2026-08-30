package com.accenture.franchise.infrastructure.adapter.postgres.mapper;

import com.accenture.franchise.domain.model.product.Product;
import com.accenture.franchise.domain.model.vob.Name;
import com.accenture.franchise.infrastructure.adapter.postgres.entity.ProductEntity;

public final class ProductMapper {

    private ProductMapper() {}

    /**
     * Convierte una entidad de base de datos en un modelo de dominio de producto.
     *
     * @param entity la entidad de producto de base de datos
     * @return el modelo de dominio del producto
     * @throws IllegalArgumentException si la entidad es nula
     */
    public static Product toDomain(ProductEntity entity) {
        if (entity == null) throw new IllegalArgumentException("No se puede mapear una entidad nula");
        return Product.builder()
                .id(entity.getId())
                .branchId(entity.getBranchId())
                .name(new Name(entity.getName()))
                .stock(entity.getStock())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convierte un modelo de dominio de producto en una entidad de base de datos.
     *
     * @param product el modelo de dominio del producto
     * @return la entidad de producto para la base de datos
     * @throws IllegalArgumentException si el producto de dominio es nulo
     */
    public static ProductEntity toEntity(Product product) {
        if (product == null) throw new IllegalArgumentException("No se puede mapear un dominio nulo");
        return ProductEntity.builder()
                .id(product.getId())
                .branchId(product.getBranchId())
                .name(product.getName().value())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
