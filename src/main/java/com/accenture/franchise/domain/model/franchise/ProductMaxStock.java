package com.accenture.franchise.domain.model.franchise;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductMaxStock {

    Long branchId;
    String branchName;
    Long productId;
    String productName;
    Integer stock;

}
