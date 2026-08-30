package com.accenture.franchise.infrastructure.adapter.postgres.product;

import com.accenture.franchise.domain.gateway.ProductRepositoryGateway;
import com.accenture.franchise.domain.model.product.Product;
import com.accenture.franchise.infrastructure.adapter.postgres.mapper.ProductMapper;
import com.accenture.franchise.infrastructure.adapter.postgres.repository.ProductR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ProductPostgresAdapter implements ProductRepositoryGateway {

    private final ProductR2dbcRepository productR2dbcRepository;

    @Override
    public Mono<Product> save(Product product) {
        return productR2dbcRepository.save(ProductMapper.toEntity(product))
                .map(ProductMapper::toDomain);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return productR2dbcRepository.findById(id)
                .map(ProductMapper::toDomain);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return productR2dbcRepository.deleteById(id);
    }

    @Override
    public Mono<Product> updateStock(Long id, Integer newStock) {
        return productR2dbcRepository.findById(id)
                .flatMap(entity -> {
                    entity.setStock(newStock);
                    entity.setUpdatedAt(LocalDateTime.now());
                    return productR2dbcRepository.save(entity);
                })
                .map(ProductMapper::toDomain);
    }

    @Override
    public Mono<Product> updateName(Long id, String newName) {
        return productR2dbcRepository.findById(id)
                .flatMap(entity -> {
                    entity.setName(newName);
                    entity.setUpdatedAt(LocalDateTime.now());
                    return productR2dbcRepository.save(entity);
                })
                .map(ProductMapper::toDomain);
    }

    @Override
    public Flux<Product> findByBranchId(Long branchId) {
        return productR2dbcRepository.findByBranchId(branchId)
                .map(ProductMapper::toDomain);
    }
}
