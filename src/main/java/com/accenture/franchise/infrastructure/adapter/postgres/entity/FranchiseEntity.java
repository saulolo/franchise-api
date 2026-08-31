package com.accenture.franchise.infrastructure.adapter.postgres.entity;

import com.accenture.franchise.domain.model.branch.Branch;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FranchiseEntity {

    @Id
    Long id;

    @Size(min = 3, max = 30)
    String name;

    @Column("created_at")
    LocalDateTime createdAt;

    @Column("updated_at")
    LocalDateTime updatedAt;

}
