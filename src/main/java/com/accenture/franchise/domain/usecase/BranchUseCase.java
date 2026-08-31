package com.accenture.franchise.domain.usecase;

import com.accenture.franchise.domain.gateway.BranchRepositoryGateway;
import com.accenture.franchise.domain.gateway.FranchiseRepositoryGateway;
import com.accenture.franchise.domain.gateway.ProductRepositoryGateway;
import com.accenture.franchise.domain.model.branch.Branch;
import com.accenture.franchise.domain.model.exception.BusinessRulesOnFieldsException;
import com.accenture.franchise.domain.model.exception.ResourceNotFoundException;
import com.accenture.franchise.domain.model.product.Product;
import com.accenture.franchise.domain.model.vob.Name;
import com.accenture.franchise.shared.utils.Constants;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class BranchUseCase {

    private final BranchRepositoryGateway branchRepositoryGateway;
    private final FranchiseRepositoryGateway franchiseRepositoryGateway;

    public BranchUseCase(BranchRepositoryGateway branchRepositoryGateway, FranchiseRepositoryGateway franchiseRepositoryGateway) {
        this.branchRepositoryGateway = branchRepositoryGateway;
        this.franchiseRepositoryGateway = franchiseRepositoryGateway;
    }

    /**
     * Crea una nueva sucursal asociada a una franquicia existente.
     *
     * @param franchiseId el identificador de la franquicia a la que pertenecerá la sucursal
     * @param name el nombre de la nueva sucursal
     * @return un Mono que emite la sucursal recién creada
     * @throws ResourceNotFoundException si la franquicia no existe
     */
    public Mono<Branch> createBranch(Long franchiseId, String name) {
        Name nameVo = new Name(name);

        return franchiseRepositoryGateway.existsById(franchiseId)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(Constants.NOT_FOUND_FRANCHISE + franchiseId)))
                .flatMap(exists -> {
                    LocalDateTime now = LocalDateTime.now();
                    Branch branch = Branch.builder()
                            .id(null)
                            .franchiseId(franchiseId)
                            .name(nameVo)
                            .products(null)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();
                    return branchRepositoryGateway.save(branch);
                });
    }

    /**
     * Actualiza el nombre de una sucursal existente.
     *
     * @param id el identificador de la sucursal
     * @param newName el nuevo nombre para la sucursal
     * @return un Mono que emite la sucursal con su nombre actualizado
     * @throws ResourceNotFoundException si la sucursal no existe
     */
    public Mono<Branch> updateBranchName(Long id, String newName) {
        Name nameVo = new Name(newName);
        return branchRepositoryGateway.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(Constants.NOT_FOUND_BRANCH + id)))
                .flatMap(b -> branchRepositoryGateway.updateName(id, nameVo.value()));
    }

}
