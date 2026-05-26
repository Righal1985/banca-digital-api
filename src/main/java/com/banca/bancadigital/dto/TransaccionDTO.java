package com.banca.bancadigital.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransaccionDTO {
    private Long id;
    private String tipo;
    private BigDecimal monto;
    private LocalDateTime fechaHora;
    private String numeroCuentaOrigen; // Solo el número, no el objeto Cuenta completo
    private String numeroCuentaDestino; // Solo el número
}