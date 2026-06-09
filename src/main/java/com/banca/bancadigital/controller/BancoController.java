package com.banca.bancadigital.controller;

import com.banca.bancadigital.dto.TransferenciaRequestDTO;
import com.banca.bancadigital.dto.UsuarioRequestDTO;
import com.banca.bancadigital.dto.UsuarioResponseDTO;
import com.banca.bancadigital.dto.TransaccionDTO;
import com.banca.bancadigital.model.Cuenta;
import com.banca.bancadigital.model.Usuario;
import com.banca.bancadigital.repository.CuentaRepository; // 👈 Importamos repositorios
import com.banca.bancadigital.repository.TransaccionRepository;
import com.banca.bancadigital.service.BancoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/banco")
public class BancoController {

    private final BancoService bancoService;
    private final CuentaRepository cuentaRepository;       // 1. Declaramos CuentaRepository
    private final TransaccionRepository transaccionRepository; //  2. Declaramos TransaccionRepository

    // 3. Inyección de todo por constructor
    public BancoController(BancoService bancoService,
                           CuentaRepository cuentaRepository,
                           TransaccionRepository transaccionRepository) {
        this.bancoService = bancoService;
        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    // ==========================================
    // ENDPOINTS ORIGINALES DE TU SISTEMA
    // ==========================================

    @PostMapping("/usuarios/registrar")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        try {
            Usuario usuarioEntity = new Usuario();
            usuarioEntity.setRut(dto.getRut());
            usuarioEntity.setNombre(dto.getNombre());
            usuarioEntity.setEmail(dto.getEmail());

            Usuario usuarioCreado = bancoService.registrarUsuario(usuarioEntity);

            UsuarioResponseDTO response = new UsuarioResponseDTO();
            response.setRut(usuarioCreado.getRut());
            response.setNombre(usuarioCreado.getNombre());
            response.setEmail(usuarioCreado.getEmail());

            if (usuarioCreado.getCuentas() != null && !usuarioCreado.getCuentas().isEmpty()) {
                response.setNumeroCuentaAsignada(usuarioCreado.getCuentas().get(0).getNumeroCuenta());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/cuentas")
    public ResponseEntity<?> abrirCuenta(@RequestParam String rut, @RequestParam String tipo) {
        try {
            Cuenta nuevaCuenta = bancoService.abrirCuenta(rut, tipo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuenta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/cuentas/depositar")
    public ResponseEntity<?> depositar(@RequestParam String numeroCuenta, @RequestParam BigDecimal monto) {
        try {
            Cuenta cuentaActualizada = bancoService.depositar(numeroCuenta, monto);
            return ResponseEntity.ok(cuentaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/cuentas/transferir")
    public ResponseEntity<?> transferir(@Valid @RequestBody TransferenciaRequestDTO dto) {
        try {
            bancoService.transferir(dto.getCuentaOrigen(), dto.getCuentaDestino(), dto.getMonto());
            return ResponseEntity.ok().body("{\"mensaje\": \"Transferencia realizada con éxito\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/usuarios/cliente")
    public ResponseEntity<?> obtenerEstadoCliente(@RequestParam String rut) {
        try {
            Usuario usuario = bancoService.obtenerEstadoCliente(rut);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/cuentas/detalle")
    public ResponseEntity<?> obtenerDetalleCuenta(@RequestParam String numeroCuenta) {
        try {
            Cuenta cuenta = bancoService.obtenerDetalleCuenta(numeroCuenta);
            return ResponseEntity.ok(cuenta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/cuentas/historial")
    public ResponseEntity<?> obtenerHistorial(@RequestParam String numeroCuenta) {
        try {
            List<TransaccionDTO> historial = bancoService.obtenerHistorialCuenta(numeroCuenta);
            return ResponseEntity.ok(historial);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ==========================================
    // 🚀 ENDPOINTS DE INTEGRACIÓN OPEN BANKING (Búsqueda por ID)
    // ==========================================

    // URL: GET http://localhost:8080/api/banco/cuentas/3/saldo
    @GetMapping("/cuentas/{id}/saldo")
    public ResponseEntity<?> obtenerSaldoPorId(@PathVariable Long id) {
        return cuentaRepository.findById(id)
                .map(cuenta -> ResponseEntity.ok(cuenta))
                .orElse(ResponseEntity.notFound().build());
    }

    // URL: GET http://localhost:8080/api/banco/cuentas/3/transacciones
    @GetMapping("/cuentas/{id}/transacciones")
    public ResponseEntity<?> obtenerHistorialPorId(@PathVariable Long id) {
        return cuentaRepository.findById(id)
                .map(cuenta -> {
                    // Usamos tu query personalizada de TransaccionRepository
                    var transacciones = transaccionRepository.findByCuenta(cuenta);
                    return ResponseEntity.ok(transacciones);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}