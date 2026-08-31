package com.accenture.franchise.domain.model.franchise;

import com.accenture.franchise.domain.model.branch.Branch;
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
public class Franchise {

    Long id;
    Name name;
    List<Branch> branches;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
