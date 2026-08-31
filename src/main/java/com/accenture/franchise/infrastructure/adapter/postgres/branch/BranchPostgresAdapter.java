package com.accenture.franchise.infrastructure.adapter.postgres.branch;

import com.accenture.franchise.domain.gateway.BranchRepositoryGateway;
import com.accenture.franchise.domain.model.branch.Branch;
import com.accenture.franchise.infrastructure.adapter.postgres.mapper.BranchMapper;
import com.accenture.franchise.infrastructure.adapter.postgres.repository.BranchR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BranchPostgresAdapter implements BranchRepositoryGateway {

    private final BranchR2dbcRepository branchR2dbcRepository;

    @Override
    public Mono<Boolean> existsById(Long id) {
        return branchR2dbcRepository.existsBranchById(id);
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        return branchR2dbcRepository.save(BranchMapper.toEntity(branch))
                .map(BranchMapper::toDomain);
    }

    @Override
    public Mono<Branch> findById(Long id) {
        return branchR2dbcRepository.findById(id)
                .map(BranchMapper::toDomain);
    }

    @Override
    public Mono<Branch> updateName(Long id, String newName) {
        return branchR2dbcRepository.findById(id)
                .flatMap(entity -> {
                    entity.setName(newName);
                    entity.setUpdatedAt(LocalDateTime.now());
                    return branchR2dbcRepository.save(entity);
                })
                .map(BranchMapper::toDomain);
    }

    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId) {
        return branchR2dbcRepository.findByFranchiseId(franchiseId)
                .map(BranchMapper::toDomain);
    }
}
