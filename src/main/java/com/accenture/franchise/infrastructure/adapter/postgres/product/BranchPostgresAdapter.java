package com.accenture.franchise.infrastructure.adapter.postgres.product;

import com.accenture.franchise.domain.gateway.BranchRepositoryGateway;
import com.accenture.franchise.infrastructure.adapter.postgres.repository.BranchR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchPostgresAdapter implements BranchRepositoryGateway {

    private final BranchR2dbcRepository branchR2dbcRepository;

    @Override
    public Mono<Boolean> existsById(Long id) {
        return branchR2dbcRepository.existsBranchById(id);
    }
}
