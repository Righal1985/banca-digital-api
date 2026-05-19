package com.banca.bancadigital.repository;

import com.banca.bancadigital.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    // Clave para el banco: Buscar una cuenta por su número único (ej: 55001234)
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
}
