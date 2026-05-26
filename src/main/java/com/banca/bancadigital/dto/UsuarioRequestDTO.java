package com.banca.bancadigital.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.banca.bancadigital.validation.ValidRut;

@Getter
@Setter
public class UsuarioRequestDTO {
    @NotBlank(message = "El RUT es obligatorio")
    @ValidRut(message = "El RUT ingresado no es válido en el territorio nacional")
    private String rut;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del correo electrónico no es válido")
    private String email;


}
