package com.mjvera.nisumtechtest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRequestDTO {
    @NotBlank(message = "El nombre es requerido.")
    private String name;

    @Email(
        regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
        flags = Pattern.Flag.CASE_INSENSITIVE,
        message = "Por favor entra un email válido.")
    @NotBlank(message = "El email es requerido.")
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z0-9]*$",
            message = "La contraseña debe contener al menos una letra mayúscula y un número.")
    @NotBlank(message = "La contraseña es requerida.")
    private String password;

    @NotNull(message = "The field 'phones' is required.")
    @Valid
    private List<@Valid PhoneRequestDTO> phones;
}
