package com.banca.bancadigital.service;

import com.banca.bancadigital.model.Cuenta;
import com.banca.bancadigital.model.Usuario;
import com.banca.bancadigital.repository.CuentaRepository;
import com.banca.bancadigital.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class BancoService {

    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;

    // Constructor para Inyección de Dependencias
    public BancoService(UsuarioRepository usuarioRepository, CuentaRepository cuentaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
    }

    // REGLA 1: Crear un nuevo usuario en el sistema
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        // Validar si el RUT ya existe para no duplicar clientes
        if (usuarioRepository.findByRut(usuario.getRut()).isPresent()) {
            throw new RuntimeException("El RUT ya se encuentra registrado en el banco.");
        }
        return usuarioRepository.save(usuario);
    }

    // REGLA 2: Abrir una cuenta nueva para un usuario existente
    @Transactional
    public Cuenta abrirCuenta(String rutUsuario, String tipoCuenta) {
        // 1. Buscar que el usuario realmente exista en el banco
        Usuario usuario = usuarioRepository.findByRut(rutUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el RUT: " + rutUsuario));

        // 2. Crear la nueva cuenta
        Cuenta nuevaCuenta = new Cuenta();
        nuevaCuenta.setTipo(tipoCuenta.toUpperCase());
        nuevaCuenta.setSaldo(BigDecimal.ZERO); // Toda cuenta bancaria inicia en $0
        nuevaCuenta.setNumeroCuenta(generarNumeroCuentaUnico());
        nuevaCuenta.setUsuario(usuario); // Asociamos la cuenta al usuario

        return cuentaRepository.save(nuevaCuenta);
    }

    // Método auxiliar para simular un número de cuenta corriente/ahorro
    private String generarNumeroCuentaUnico() {
        Random random = new Random();
        int numero = 10000000 + random.nextInt(90000000); // Genera un número de 8 dígitos
        return String.valueOf(numero);
    }
}
