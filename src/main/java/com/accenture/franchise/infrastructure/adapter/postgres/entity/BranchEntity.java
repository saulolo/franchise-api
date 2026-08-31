package com.accenture.franchise.infrastructure.adapter.postgres.entity;

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

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("branches")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchEntity {

    @Id
    Long id;

    @Column("franchise_id")
    Long franchiseId;

    @Size(min = 3, max = 30)
    String name;

    @Column("created_at")
    LocalDateTime createdAt;

    @Column("updated_at")
    LocalDateTime updatedAt;

}
