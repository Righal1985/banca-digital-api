package com.banca.bancadigital.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/core/cuentas")
public class CuentaController {




    @GetMapping("/{id}/saldo")
    public ResponseEntity<?> obtenerSaldo(@PathVariable Long id) {
        // Simulamos la búsqueda en banca_db.
        // Si el ID es 2, inventamos un saldo para testear.
        if (id == 2) {
            return ResponseEntity.ok(Map.of(
                    "cuentaId", id,
                    "tipoCuenta", "Cuenta Corriente",
                    "saldo", 750000.00,
                    "divisa", "CLP"
            ));
        }

        return ResponseEntity.notFound().build();
    }
}
