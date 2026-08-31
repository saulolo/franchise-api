package com.accenture.franchise.infrastructure.adapter.postgres.mapper;

import com.accenture.franchise.domain.model.branch.Branch;
import com.accenture.franchise.domain.model.vob.Name;
import com.accenture.franchise.infrastructure.adapter.postgres.entity.BranchEntity;

public final class BranchMapper {

    private BranchMapper() {}

    /**
     * Convierte una entidad de base de datos en un modelo de dominio de sucursal.
     *
     * @param entity la entidad de sucursal de base de datos
     * @return el modelo de dominio de la sucursal
     * @throws IllegalArgumentException si la entidad es nula
     */
    public static Branch toDomain(BranchEntity entity) {
        if (entity == null) throw new IllegalArgumentException("No se puede mapear una entidad nula");
        return Branch.builder()
                .id(entity.getId())
                .franchiseId(entity.getFranchiseId())
                .name(new Name(entity.getName()))
                .products(null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convierte un modelo de dominio de sucursal en una entidad de base de datos.
     *
     * @param domain el modelo de dominio de la sucursal
     * @return la entidad de sucursal para la base de datos
     * @throws IllegalArgumentException si el dominio es nulo
     */
    public static BranchEntity toEntity(Branch domain) {
        if (domain == null) throw new IllegalArgumentException("No se puede mapear un dominio nulo");
        return BranchEntity.builder()
                .id(domain.getId())
                .franchiseId(domain.getFranchiseId())
                .name(domain.getName().value())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

}
