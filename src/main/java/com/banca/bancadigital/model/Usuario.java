package com.banca.bancadigital.model; // Verifica que este sea tu paquete real

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data // Esto de Lombok crea los Getters y Setters automáticamente
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rut; // Ejemplo: "12345678-9"

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    // Un usuario puede tener varias cuentas (Corriente, Ahorro, etc.)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Cuenta> cuentas;
}