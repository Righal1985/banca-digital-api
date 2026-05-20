package com.banca.bancadigital.controller;

import com.banca.bancadigital.model.Cuenta;
import com.banca.bancadigital.model.Usuario;
import com.banca.bancadigital.service.BancoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/banco")
public class BancoController {

    private final BancoService bancoService;

    // Inyección por constructor
    public BancoController(BancoService bancoService) {
        this.bancoService = bancoService;
    }

    // 1. Endpoint para Registrar un Usuario
    // URL: POST http://localhost:8080/api/banco/usuarios
    @PostMapping("/usuarios")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = bancoService.registrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (RuntimeException e) {
            // Si el RUT ya existe, atrapamos el error del Service y devolvemos un 400 (Bad Request)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 2. Endpoint para Abrir una Cuenta
    // URL: POST http://localhost:8080/api/banco/cuentas?rut=12345678-9&tipo=CORRIENTE
    @PostMapping("/cuentas")
    public ResponseEntity<?> abrirCuenta(
            @RequestParam String rut,
            @RequestParam String tipo) {
        try {
            Cuenta nuevaCuenta = bancoService.abrirCuenta(rut, tipo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuenta);
        } catch (RuntimeException e) {
            // Si el usuario no existe, devolvemos un 400 con el mensaje de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    // 3. Endpoint para Depositar Dinero
// URL: POST http://localhost:8080/api/banco/cuentas/depositar?numeroCuenta=78826524&monto=50000
    @PostMapping("/cuentas/depositar")
    public ResponseEntity<?> depositar(
            @RequestParam String numeroCuenta,
            @RequestParam BigDecimal monto) {
        try {
            Cuenta cuentaActualizada = bancoService.depositar(numeroCuenta, monto);
            return ResponseEntity.ok(cuentaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 4. Endpoint para Transferir entre Cuentas
    // URL: POST http://localhost:8080/api/banco/cuentas/transferir?origen=78826524&destino=11223344&monto=20000
    @PostMapping("/cuentas/transferir")
    public ResponseEntity<?> transferir(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam BigDecimal monto) {
        try {
            bancoService.transferir(origen, destino, monto);
            return ResponseEntity.ok("Transferencia realizada con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 5. Endpoint para obtener el estado completo de un cliente (Usuario + sus cuentas)
// URL: GET http://localhost:8080/api/banco/usuarios/cliente?rut=12345678-9
    @GetMapping("/usuarios/cliente")
    public ResponseEntity<?> obtenerEstadoCliente(@RequestParam String rut) {
        try {
            Usuario usuario = bancoService.obtenerEstadoCliente(rut);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 6. Endpoint para ver el detalle de una sola cuenta específica
// URL: GET http://localhost:8080/api/banco/cuentas/detalle?numeroCuenta=78826524
    @GetMapping("/cuentas/detalle")
    public ResponseEntity<?> obtenerDetalleCuenta(@RequestParam String numeroCuenta) {
        try {
            Cuenta cuenta = bancoService.obtenerDetalleCuenta(numeroCuenta);
            return ResponseEntity.ok(cuenta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
