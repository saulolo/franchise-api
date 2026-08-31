package com.accenture.franchise.domain.usecase;

import com.accenture.franchise.domain.gateway.FranchiseRepositoryGateway;
import com.accenture.franchise.domain.model.exception.ResourceNotFoundException;
import com.accenture.franchise.domain.model.franchise.Franchise;
import com.accenture.franchise.domain.model.franchise.ProductMaxStock;
import com.accenture.franchise.domain.model.vob.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
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
class FranchiseUseCaseTest {

    @Mock
    private FranchiseRepositoryGateway franchiseRepositoryGateway;

    private FranchiseUseCase franchiseUseCase;

    @BeforeEach
    void setUp() {
        franchiseUseCase = new FranchiseUseCase(franchiseRepositoryGateway);
    }

    @Test
    @DisplayName("Debe crear una franquicia exitosamente")
    void shouldCreateFranchiseSuccessfully() {
        // Arrange
        String franchiseName = "KFC Colombia";
        Franchise savedFranchise = Franchise.builder()
                .id(1L)
                .name(new Name(franchiseName))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(franchiseRepositoryGateway.save(any(Franchise.class))).thenReturn(Mono.just(savedFranchise));

        // Act & Assert
        StepVerifier.create(franchiseUseCase.createFranchise(franchiseName))
                .expectNextMatches(f -> f.getId().equals(1L) && f.getName().value().equals(franchiseName))
                .verifyComplete();

        verify(franchiseRepositoryGateway, times(1)).save(any(Franchise.class));
    }

    @Test
    @DisplayName("Debe actualizar el nombre de la franquicia exitosamente")
    void shouldUpdateFranchiseNameSuccessfully() {
        // Arrange
        Long franchiseId = 1L;
        String newName = "KFC Internacional";
        Franchise existing = Franchise.builder().id(franchiseId).name(new Name("KFC")).build();
        Franchise updated = existing.toBuilder().name(new Name(newName)).build();

        when(franchiseRepositoryGateway.findById(franchiseId)).thenReturn(Mono.just(existing));
        when(franchiseRepositoryGateway.updateName(eq(franchiseId), eq(newName))).thenReturn(Mono.just(updated));

        // Act & Assert
        StepVerifier.create(franchiseUseCase.updateFranchiseName(franchiseId, newName))
                .expectNextMatches(f -> f.getName().value().equals(newName))
                .verifyComplete();

        verify(franchiseRepositoryGateway, times(1)).findById(franchiseId);
        verify(franchiseRepositoryGateway, times(1)).updateName(franchiseId, newName);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException al actualizar nombre si la franquicia no existe")
    void shouldThrowExceptionWhenFranchiseNotFoundOnUpdate() {
        // Arrange
        Long nonExistentId = 999L;
        when(franchiseRepositoryGateway.findById(nonExistentId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(franchiseUseCase.updateFranchiseName(nonExistentId, "Nuevo Nombre"))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(franchiseRepositoryGateway, never()).updateName(any(), any());
    }

    @Test
    @DisplayName("Debe obtener el listado de productos de mayor stock por sucursal para una franquicia existente")
    void shouldGetMaxStockProductsSuccessfully() {
        // Arrange
        Long franchiseId = 1L;
        ProductMaxStock item1 = ProductMaxStock.builder()
                .branchId(1L)
                .branchName("Sucursal Norte")
                .productId(10L)
                .productName("Pollo Crispy")
                .stock(150)
                .build();
        ProductMaxStock item2 = ProductMaxStock.builder()
                .branchId(2L)
                .branchName("Sucursal Sur")
                .productId(25L)
                .productName("Combo Familiar")
                .stock(300)
                .build();

        when(franchiseRepositoryGateway.existsById(franchiseId)).thenReturn(Mono.just(true));
        when(franchiseRepositoryGateway.findMaxStockProductsByFranchiseId(franchiseId)).thenReturn(Flux.just(item1, item2));

        // Act & Assert
        StepVerifier.create(franchiseUseCase.getMaxStockProducts(franchiseId))
                .expectNext(item1)
                .expectNext(item2)
                .verifyComplete();

        verify(franchiseRepositoryGateway, times(1)).existsById(franchiseId);
        verify(franchiseRepositoryGateway, times(1)).findMaxStockProductsByFranchiseId(franchiseId);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException al consultar mayor stock si la franquicia no existe")
    void shouldThrowExceptionWhenFranchiseDoesNotExistOnMaxStockQuery() {
        // Arrange
        Long nonExistentId = 999L;
        when(franchiseRepositoryGateway.existsById(nonExistentId)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(franchiseUseCase.getMaxStockProducts(nonExistentId))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(franchiseRepositoryGateway, never()).findMaxStockProductsByFranchiseId(any());
    }
}