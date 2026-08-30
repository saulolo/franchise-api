package com.accenture.franchise.domain.model.product;

import com.accenture.franchise.domain.model.vob.Name;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    Long id;
    Long branchId;
    Name name;
    Integer stock;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
