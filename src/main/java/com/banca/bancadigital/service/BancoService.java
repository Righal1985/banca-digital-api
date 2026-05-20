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
    // 1. Lógica para Depositar Dinero
    @Transactional
    public Cuenta depositar(String numeroCuenta, BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto a depositar debe ser mayor a cero");
        }

        // Buscamos la cuenta en la base de datos a través de su repositorio
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("La cuenta número " + numeroCuenta + " no existe"));

        // Sumamos el saldo
        cuenta.setSaldo(cuenta.getSaldo().add(monto));

        // Guardamos los cambios
        return cuentaRepository.save(cuenta);
    }

    // 2. Lógica para Transferir entre Cuentas
    @Transactional
    public void transferir(String cuentaOrigen, String cuentaDestino, BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto a transferir debe ser mayor a cero");
        }

        // Buscar y validar cuenta de origen
        Cuenta origen = cuentaRepository.findByNumeroCuenta(cuentaOrigen)
                .orElseThrow(() -> new RuntimeException("La cuenta de origen no existe"));

        // Buscar y validar cuenta de destino
        Cuenta destino = cuentaRepository.findByNumeroCuenta(cuentaDestino)
                .orElseThrow(() -> new RuntimeException("La cuenta de destino no existe"));

        // Verificar si hay fondos suficientes
        if (origen.getSaldo().compareTo(monto) < 0) {
            throw new RuntimeException("Fondos insuficientes en la cuenta de origen");
        }

        // Restar de una y sumar en la otra
        origen.setSaldo(origen.getSaldo().subtract(monto));
        destino.setSaldo(destino.getSaldo().add(monto));

        // Guardar ambas cuentas actualizadas
        cuentaRepository.save(origen);
        cuentaRepository.save(destino);
    }
    // ... dentro de tu BancoService.java

    // 5. Consultar un usuario con todas sus cuentas mediante su RUT
    @Transactional(readOnly = true)
    public Usuario obtenerEstadoCliente(String rut) {
        return usuarioRepository.findByRut(rut)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con el RUT: " + rut));
    }

    // 6. Consultar los datos de una cuenta específica por su número
    @Transactional(readOnly = true)
    public Cuenta obtenerDetalleCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("La cuenta número " + numeroCuenta + " no existe"));
    }
}
