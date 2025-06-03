package com.mjvera.nisumtechtest.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PhoneRequestDTO {
    @NotBlank(message = "Número de teléfono es requerido.")
    private String number;
    @NotBlank(message = "Código de ciudad es requerido.")
    private String cityCode;
    @NotBlank(message = "Código del país es requerido.")
    private String countryCode;
}
