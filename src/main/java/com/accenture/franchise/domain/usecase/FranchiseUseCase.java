package com.accenture.franchise.domain.usecase;

import com.accenture.franchise.domain.gateway.FranchiseRepositoryGateway;
import com.accenture.franchise.domain.model.exception.ResourceNotFoundException;
import com.accenture.franchise.domain.model.franchise.Franchise;
import com.accenture.franchise.domain.model.franchise.ProductMaxStock;
import com.accenture.franchise.domain.model.vob.Name;
import com.accenture.franchise.shared.utils.Constants;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class FranchiseUseCase {


    private final FranchiseRepositoryGateway franchiseRepositoryGateway;

    public FranchiseUseCase(FranchiseRepositoryGateway franchiseRepositoryGateway) {
        this.franchiseRepositoryGateway = franchiseRepositoryGateway;
    }

    /**
     * Crea una nueva franquicia con el nombre especificado.
     *
     * @param name el nombre de la franquicia
     * @return un Mono que emite la franquicia recién creada
     */
    public Mono<Franchise> createFranchise(String name) {
        Name nameVo = new Name(name);
        LocalDateTime now = LocalDateTime.now();
        Franchise franchise = Franchise.builder()
                .id(null)
                .name(nameVo)
                .branches(null)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return franchiseRepositoryGateway.save(franchise);
    }

    /**
     * Actualiza el nombre de una franquicia existente.
     *
     * @param id el identificador de la franquicia
     * @param newName el nuevo nombre para la franquicia
     * @return un Mono que emite la franquicia con su nombre actualizado
     * @throws ResourceNotFoundException si la franquicia no existe
     */
    public Mono<Franchise> updateFranchiseName(Long id, String newName) {
        Name nameVo = new Name(newName);
        return franchiseRepositoryGateway.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(Constants.NOT_FOUND_FRANCHISE + id)))
                .flatMap(f -> franchiseRepositoryGateway.updateName(id, nameVo.value()));
    }

    /**
     * Obtiene el producto con mayor stock por cada sucursal de una franquicia específica.
     *
     * @param franchiseId el identificador de la franquicia
     * @return un Flux que emite los productos con mayor stock de cada sucursal
     * @throws ResourceNotFoundException si la franquicia no existe
     */
    public Flux<ProductMaxStock> getMaxStockProducts(Long franchiseId) {
        return franchiseRepositoryGateway.existsById(franchiseId)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(Constants.NOT_FOUND_FRANCHISE + franchiseId)))
                .flatMapMany(exists -> franchiseRepositoryGateway.findMaxStockProductsByFranchiseId(franchiseId));
    }


}
