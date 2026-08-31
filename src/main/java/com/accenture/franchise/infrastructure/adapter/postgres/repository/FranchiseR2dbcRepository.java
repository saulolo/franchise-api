package com.accenture.franchise.infrastructure.adapter.postgres.repository;

import com.accenture.franchise.infrastructure.adapter.postgres.entity.BranchEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FranchiseR2dbcRepository extends ReactiveCrudRepository<BranchEntity, Long> {


    @Query("SELECT COUNT(1) > 0 FROM franchises WHERE id = :id")
    Mono<Boolean> existsFranchiseById(Long id);

}
