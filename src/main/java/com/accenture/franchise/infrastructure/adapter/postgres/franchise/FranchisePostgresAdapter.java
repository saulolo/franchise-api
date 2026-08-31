package com.accenture.franchise.infrastructure.adapter.postgres.franchise;

import com.accenture.franchise.domain.gateway.FranchiseRepositoryGateway;
import com.accenture.franchise.domain.model.franchise.Franchise;
import com.accenture.franchise.domain.model.franchise.ProductMaxStock;
import com.accenture.franchise.infrastructure.adapter.postgres.mapper.FranchiseMapper;
import com.accenture.franchise.infrastructure.adapter.postgres.repository.FranchiseR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FranchisePostgresAdapter implements FranchiseRepositoryGateway {

    private final FranchiseR2dbcRepository franchiseR2dbcRepository;


    @Override
    public Mono<Boolean> existsById(Long id) {
        return franchiseR2dbcRepository.existsFranchiseById(id);
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return franchiseR2dbcRepository.save(FranchiseMapper.toEntity(franchise))
                .map(FranchiseMapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return franchiseR2dbcRepository.findById(id)
                .map(FranchiseMapper::toDomain);
    }

    @Override
    public Mono<Franchise> updateName(Long id, String newName) {
        return franchiseR2dbcRepository.findById(id)
                .flatMap(entity -> {
                    entity.setName(newName);
                    entity.setUpdatedAt(LocalDateTime.now());
                    return franchiseR2dbcRepository.save(entity);
                })
                .map(FranchiseMapper::toDomain);
    }

    @Override
    public Flux<ProductMaxStock> findMaxStockProductsByFranchiseId(Long franchiseId) {
        return franchiseR2dbcRepository.findMaxStockProductsByFranchiseId(franchiseId);
    }

}
