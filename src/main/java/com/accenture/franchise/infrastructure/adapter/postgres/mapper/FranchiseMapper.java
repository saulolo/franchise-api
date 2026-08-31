package com.accenture.franchise.infrastructure.adapter.postgres.mapper;

import com.accenture.franchise.domain.model.franchise.Franchise;
import com.accenture.franchise.domain.model.vob.Name;
import com.accenture.franchise.infrastructure.adapter.postgres.entity.FranchiseEntity;

public final class FranchiseMapper {

    private FranchiseMapper() {}

    /**
     * Convierte una entidad de base de datos en un modelo de dominio de franquicia.
     *
     * @param entity la entidad de base de datos
     * @return el modelo de dominio de franquicia
     * @throws IllegalArgumentException si la entidad es nula
     */
    public static Franchise toDomain(FranchiseEntity entity) {
        if (entity == null) throw new IllegalArgumentException("No se puede mapear una entidad nula");
        return Franchise.builder()
                .id(entity.getId())
                .name(new Name(entity.getName()))
                .branches(null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convierte un modelo de dominio de franquicia en una entidad de base de datos.
     *
     * @param domain el modelo de dominio de franquicia
     * @return la entidad de franquicia para persistencia
     * @throws IllegalArgumentException si el dominio es nulo
     */
    public static FranchiseEntity toEntity(Franchise domain) {
        if (domain == null) throw new IllegalArgumentException("No se puede mapear un dominio nulo");
        return FranchiseEntity.builder()
                .id(domain.getId())
                .name(domain.getName().value())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

}
