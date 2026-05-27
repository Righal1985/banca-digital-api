package com.banca.bancadigital.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;

@Entity
@Table(name = "cuentas")
@Getter
@Setter
@ToString(exclude = "usuario") // Evita que Lombok genere bucles al imprimir la entidad
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroCuenta;

    @Column(nullable = false)
    private String tipo; // "CORRIENTE" o "AHORRO"

    @Column(nullable = false)
    private BigDecimal saldo;

    @Column(nullable = false)
    private boolean activa = true;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties("cuentas") // <-- CORTE AQUÍ: Al serializar la cuenta, no traerá la lista de cuentas del usuario
    private Usuario usuario;
}