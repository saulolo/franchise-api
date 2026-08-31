package com.accenture.franchise.domain.usecase;

import com.accenture.franchise.domain.gateway.BranchRepositoryGateway;
import com.accenture.franchise.domain.gateway.FranchiseRepositoryGateway;
import com.accenture.franchise.domain.model.branch.Branch;
import com.accenture.franchise.domain.model.exception.ResourceNotFoundException;
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
class BranchUseCaseTest {

    @Mock
    private BranchRepositoryGateway branchRepositoryGateway;

    @Mock
    private FranchiseRepositoryGateway franchiseRepositoryGateway;

    private BranchUseCase branchUseCase;

    @BeforeEach
    void setUp() {
        branchUseCase = new BranchUseCase(branchRepositoryGateway, franchiseRepositoryGateway);
    }

    @Test
    @DisplayName("Debe crear una sucursal exitosamente cuando la franquicia existe y el nombre es válido")
    void shouldCreateBranchSuccessfully() {
        // Arrange
        Long franchiseId = 1L;
        String branchName = "Sucursal Poblado";
        Branch savedBranch = Branch.builder()
                .id(10L)
                .franchiseId(franchiseId)
                .name(new Name(branchName))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(franchiseRepositoryGateway.existsById(franchiseId)).thenReturn(Mono.just(true));
        when(branchRepositoryGateway.save(any(Branch.class))).thenReturn(Mono.just(savedBranch));

        // Act & Assert
        StepVerifier.create(branchUseCase.createBranch(franchiseId, branchName))
                .expectNextMatches(b -> b.getId().equals(10L) && b.getName().value().equals(branchName))
                .verifyComplete();

        verify(franchiseRepositoryGateway, times(1)).existsById(franchiseId);
        verify(branchRepositoryGateway, times(1)).save(any(Branch.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException al crear sucursal si la franquicia no existe")
    void shouldThrowExceptionWhenFranchiseDoesNotExistOnCreateBranch() {
        // Arrange
        Long nonExistentFranchiseId = 99L;
        when(franchiseRepositoryGateway.existsById(nonExistentFranchiseId)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(branchUseCase.createBranch(nonExistentFranchiseId, "Sucursal Centro"))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(branchRepositoryGateway, never()).save(any(Branch.class));
    }

    @Test
    @DisplayName("Debe actualizar el nombre de la sucursal exitosamente")
    void shouldUpdateBranchNameSuccessfully() {
        // Arrange
        Long branchId = 1L;
        String newName = "Sucursal Laureles";
        Branch existingBranch = Branch.builder()
                .id(branchId)
                .franchiseId(1L)
                .name(new Name("Sucursal Antigua"))
                .build();
        Branch updatedBranch = existingBranch.toBuilder()
                .name(new Name(newName))
                .build();

        when(branchRepositoryGateway.findById(branchId)).thenReturn(Mono.just(existingBranch));
        when(branchRepositoryGateway.updateName(eq(branchId), eq(newName))).thenReturn(Mono.just(updatedBranch));

        // Act & Assert
        StepVerifier.create(branchUseCase.updateBranchName(branchId, newName))
                .expectNextMatches(b -> b.getName().value().equals(newName))
                .verifyComplete();

        verify(branchRepositoryGateway, times(1)).findById(branchId);
        verify(branchRepositoryGateway, times(1)).updateName(branchId, newName);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException al actualizar nombre si la sucursal no existe")
    void shouldThrowExceptionWhenBranchNotFoundOnUpdateName() {
        // Arrange
        Long nonExistentBranchId = 999L;
        when(branchRepositoryGateway.findById(nonExistentBranchId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(branchUseCase.updateBranchName(nonExistentBranchId, "Nuevo Nombre"))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(branchRepositoryGateway, never()).updateName(any(), any());
    }
}