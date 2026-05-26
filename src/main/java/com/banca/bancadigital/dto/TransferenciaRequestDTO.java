package com.banca.bancadigital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class TransferenciaRequestDTO {
    @NotBlank(message = "La cuenta de origen es obligatoria")
    private String cuentaOrigen;

    @NotBlank(message = "La cuenta de destino es obligatoria")
    private String cuentaDestino;

    @NotNull(message = "El monto no puede ser nulo")
    @Positive(message = "El monto a transferir debe ser mayor a cero") // ¡Validación automática de monto!
    private BigDecimal monto;
}
