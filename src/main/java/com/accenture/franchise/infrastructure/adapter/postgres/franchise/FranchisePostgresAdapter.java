package com.accenture.franchise.infrastructure.adapter.postgres.franchise;

import com.accenture.franchise.domain.gateway.FranchiseRepositoryGateway;
import com.accenture.franchise.infrastructure.adapter.postgres.repository.FranchiseR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchisePostgresAdapter implements FranchiseRepositoryGateway {

    private final FranchiseR2dbcRepository franchiseR2dbcRepository;


    @Override
    public Mono<Boolean> existsById(Long id) {
        return franchiseR2dbcRepository.existsFranchiseById(id);
    }

}
