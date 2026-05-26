package com.banca.bancadigital.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private int codigoStatus;
    private String error;
    private String mensaje;
    private String ruta;

    public ErrorResponseDTO(int codigoStatus, String error, String mensaje, String ruta) {
        this.timestamp = LocalDateTime.now();
        this.codigoStatus = codigoStatus;
        this.error = error;
        this.mensaje = mensaje;
        this.ruta = ruta;
    }
}
