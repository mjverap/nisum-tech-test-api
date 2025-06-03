package com.mjvera.nisumtechtest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    @Email(
            regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Por favor entra un email válido.")
    @NotBlank(message = "El email es requerido.")
    private String email;

    @NotBlank(message = "La contraseña es requerida.")
    private String password;
}
