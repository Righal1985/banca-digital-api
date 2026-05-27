package com.banca.bancadigital.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponseDTO {
    private String rut;
    private String nombre;
    private String email;
    private String numeroCuentaAsignada; // <-- Le añadimos este campo al contrato de salida
}
