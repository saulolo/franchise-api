package com.accenture.franchise.domain.model.branch;

import com.accenture.franchise.domain.model.product.Product;
import com.accenture.franchise.domain.model.vob.Name;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Branch {

    Long id;
    Long franchiseId;
    Name name;
    List<Product> products;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
