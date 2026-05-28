package com.banca.bancadigital.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "El RUT es obligatorio")
    private String rut;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
