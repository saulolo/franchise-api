package com.accenture.franchise.infrastructure.entrypoints.rest.dto.request;

import com.accenture.franchise.shared.utils.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NameUpdateRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    @Pattern(regexp = Constants.NAME_REGEX, message = Constants.NAME_VALIDATION_ERROR)
    String name;
}
