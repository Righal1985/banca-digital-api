package com.banca.bancadigital.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "transacciones")
@Getter
@Setter
@ToString
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo; // "DEPOSITO" o "TRANSFERENCIA"

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    // La cuenta que realiza la acción o se ve afectada principalmente
    @ManyToOne
    @JoinColumn(name = "cuenta_origen_id", nullable = false)
    @JsonIgnoreProperties({"usuario", "hibernateLazyInitializer", "handler"})
    private Cuenta cuentaOrigen;

    @ManyToOne
    @JoinColumn(name = "cuenta_destino_id")
    @JsonIgnoreProperties({"usuario", "hibernateLazyInitializer", "handler"})
    private Cuenta cuentaDestino;
}
