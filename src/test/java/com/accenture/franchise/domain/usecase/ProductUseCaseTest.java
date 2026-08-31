package com.accenture.franchise.domain.usecase;

import com.accenture.franchise.domain.gateway.BranchRepositoryGateway;
import com.accenture.franchise.domain.gateway.ProductRepositoryGateway;
import com.accenture.franchise.domain.model.exception.BusinessRulesOnFieldsException;
import com.accenture.franchise.domain.model.exception.ResourceNotFoundException;
import com.accenture.franchise.domain.model.product.Product;
import com.accenture.franchise.domain.model.vob.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private ProductRepositoryGateway productRepositoryGateway;

    @Mock
    private BranchRepositoryGateway branchRepositoryGateway;

    private ProductUseCase productUseCase;

    @BeforeEach
    void setUp() {
        productUseCase = new ProductUseCase(productRepositoryGateway, branchRepositoryGateway);
    }

    @Test
    @DisplayName("Debe crear un producto exitosamente cuando la sucursal existe y el stock es válido")
    void shouldCreateProductSuccessfully() {
        // Arrange
        Long branchId = 1L;
        String productName = "Hamburguesa Doble";
        Integer stock = 50;
        Product savedProduct = Product.builder()
                .id(100L)
                .branchId(branchId)
                .name(new Name(productName))
                .stock(stock)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(branchRepositoryGateway.existsById(branchId)).thenReturn(Mono.just(true));
        when(productRepositoryGateway.save(any(Product.class))).thenReturn(Mono.just(savedProduct));

        // Act & Assert
        StepVerifier.create(productUseCase.createProduct(branchId, productName, stock))
                .expectNextMatches(p -> p.getId().equals(100L) && p.getStock().equals(50))
                .verifyComplete();

        verify(branchRepositoryGateway, times(1)).existsById(branchId);
        verify(productRepositoryGateway, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe lanzar BusinessRulesOnFieldsException si el stock es negativo")
    void shouldThrowExceptionWhenStockIsNegative() {
        // Act & Assert
        StepVerifier.create(productUseCase.createProduct(1L, "Papas Fritas", -10))
                .expectError(BusinessRulesOnFieldsException.class)
                .verify();

        verify(branchRepositoryGateway, never()).existsById(any());
        verify(productRepositoryGateway, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si la sucursal no existe al crear producto")
    void shouldThrowExceptionWhenBranchDoesNotExistOnCreateProduct() {
        // Arrange
        Long nonExistentBranchId = 999L;
        when(branchRepositoryGateway.existsById(nonExistentBranchId)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(productUseCase.createProduct(nonExistentBranchId, "Gaseosa Cola", 20))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(productRepositoryGateway, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar un producto exitosamente si pertenece a la sucursal indicada")
    void shouldDeleteProductSuccessfully() {
        // Arrange
        Long branchId = 1L;
        Long productId = 100L;
        Product existingProduct = Product.builder()
                .id(productId)
                .branchId(branchId)
                .name(new Name("Helado Vainilla"))
                .build();

        when(productRepositoryGateway.findById(productId)).thenReturn(Mono.just(existingProduct));
        when(productRepositoryGateway.delete(productId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(productUseCase.deleteProduct(branchId, productId))
                .verifyComplete();

        verify(productRepositoryGateway, times(1)).findById(productId);
        verify(productRepositoryGateway, times(1)).delete(productId);
    }

    @Test
    @DisplayName("Debe lanzar BusinessRulesOnFieldsException al eliminar si el producto no pertenece a la sucursal")
    void shouldThrowExceptionWhenProductDoesNotBelongToBranchOnDelete() {
        // Arrange
        Long branchId = 1L;
        Long otherBranchId = 2L;
        Long productId = 100L;
        Product existingProduct = Product.builder()
                .id(productId)
                .branchId(otherBranchId) // Sucursal diferente
                .name(new Name("Helado Vainilla"))
                .build();

        when(productRepositoryGateway.findById(productId)).thenReturn(Mono.just(existingProduct));

        // Act & Assert
        StepVerifier.create(productUseCase.deleteProduct(branchId, productId))
                .expectError(BusinessRulesOnFieldsException.class)
                .verify();

        verify(productRepositoryGateway, never()).delete(productId);
    }

    @Test
    @DisplayName("Debe actualizar el stock de un producto exitosamente")
    void shouldUpdateStockSuccessfully() {
        // Arrange
        Long productId = 10L;
        Integer newStock = 200;
        Product existing = Product.builder().id(productId).stock(50).name(new Name("Nuggets")).build();
        Product updated = existing.toBuilder().stock(newStock).build();

        when(productRepositoryGateway.findById(productId)).thenReturn(Mono.just(existing));
        when(productRepositoryGateway.updateStock(eq(productId), eq(newStock))).thenReturn(Mono.just(updated));

        // Act & Assert
        StepVerifier.create(productUseCase.updateStock(productId, newStock))
                .expectNextMatches(p -> p.getStock().equals(newStock))
                .verifyComplete();

        verify(productRepositoryGateway, times(1)).findById(productId);
        verify(productRepositoryGateway, times(1)).updateStock(productId, newStock);
    }

    @Test
    @DisplayName("Debe actualizar el nombre de un producto exitosamente")
    void shouldUpdateProductNameSuccessfully() {
        // Arrange
        Long productId = 10L;
        String newName = "Nuggets Mega Pack";
        Product existing = Product.builder().id(productId).name(new Name("Nuggets")).build();
        Product updated = existing.toBuilder().name(new Name(newName)).build();

        when(productRepositoryGateway.findById(productId)).thenReturn(Mono.just(existing));
        when(productRepositoryGateway.updateName(eq(productId), eq(newName))).thenReturn(Mono.just(updated));

        // Act & Assert
        StepVerifier.create(productUseCase.updateProductName(productId, newName))
                .expectNextMatches(p -> p.getName().value().equals(newName))
                .verifyComplete();

        verify(productRepositoryGateway, times(1)).findById(productId);
        verify(productRepositoryGateway, times(1)).updateName(productId, newName);
    }
}