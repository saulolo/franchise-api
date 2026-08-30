package com.accenture.franchise.domain.usecase;

import com.accenture.franchise.domain.gateway.BranchRepositoryGateway;
import com.accenture.franchise.domain.gateway.ProductRepositoryGateway;
import com.accenture.franchise.domain.model.exception.BusinessRulesOnFieldsException;
import com.accenture.franchise.domain.model.exception.ResourceNotFoundException;
import com.accenture.franchise.domain.model.product.Product;
import com.accenture.franchise.domain.model.vob.Name;
import com.accenture.franchise.shared.utils.Constants;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class ProductUseCase {

    private final ProductRepositoryGateway productRepositoryGateway;
    private final BranchRepositoryGateway branchRepositoryGateway;


    public ProductUseCase(ProductRepositoryGateway productRepositoryGateway, BranchRepositoryGateway branchRepositoryGateway) {
        this.productRepositoryGateway = productRepositoryGateway;
        this.branchRepositoryGateway = branchRepositoryGateway;
    }

    public Mono<Product> createProduct(Long branchId, String name, Integer stock) {
        if (stock == null || stock < 0) {
            return Mono.error(new BusinessRulesOnFieldsException("Stock"));
        }
        Name nameVo = new Name(name);

        return branchRepositoryGateway.existsById(branchId)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(Constants.NOT_FOUND_BRANCH + branchId)))
                .flatMap(exists -> {
                    LocalDateTime now = LocalDateTime.now();
                    Product product = Product.builder()
                            .id(null)
                            .branchId(branchId)
                            .name(nameVo)
                            .stock(stock)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();
                    return productRepositoryGateway.save(product);
                });
    }

    public Mono<Void> deleteProduct(Long branchId, Long productId) {
        return productRepositoryGateway.findById(productId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(Constants.NOT_FOUND_PRODUCT + productId)))
                .filter(p -> p.getBranchId().equals(branchId))
                .switchIfEmpty(Mono.error(new BusinessRulesOnFieldsException("El producto ID " + productId + " no pertenece a la sucursal " + branchId)))
                .flatMap(p -> productRepositoryGateway.delete(productId));
    }

    public Mono<Product> updateStock(Long id, Integer newStock) {
        if (newStock == null || newStock < 0) {
            return Mono.error(new BusinessRulesOnFieldsException("El stock debe ser un valor entero mayor o igual a 0"));
        }
        return productRepositoryGateway.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(Constants.NOT_FOUND_PRODUCT + id)))
                .flatMap(p -> productRepositoryGateway.updateStock(id, newStock));
    }

    public Mono<Product> updateProductName(Long id, String newName) {
        Name nameVo = new Name(newName);
        return productRepositoryGateway.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(Constants.NOT_FOUND_PRODUCT + id)))
                .flatMap(p -> productRepositoryGateway.updateName(id, nameVo.value()));
    }

}
