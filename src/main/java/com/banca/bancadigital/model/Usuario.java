package com.banca.bancadigital.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter // Reemplazamos @Data por Getter y Setter explícitos
@Setter
@ToString(exclude = "cuentas") // Evita que Lombok genere un bucle en el toString
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rut;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER) //
    @JsonIgnoreProperties("usuario") // Mantenemos esto para evitar bucles infinitos
    private List<Cuenta> cuentas;
}