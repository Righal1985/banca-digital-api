package com.banca.bancadigital.repository;

import com.banca.bancadigital.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método personalizado para buscar un cliente por su RUT
    Optional<Usuario> findByRut(String rut);
}