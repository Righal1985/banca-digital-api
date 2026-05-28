package com.banca.bancadigital.controller;

import com.banca.bancadigital.dto.LoginRequest;
import com.banca.bancadigital.model.Usuario;
import com.banca.bancadigital.repository.UsuarioRepository;
import com.banca.bancadigital.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Inyección por constructor
    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody Usuario usuario) {
        // 1. Verificar si el RUT ya está registrado para no duplicar
        if (usuarioRepository.findByRut(usuario.getRut()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El RUT ya se encuentra registrado"));
        }

        // 2. ENCRIPTAR la contraseña en texto plano antes de guardarla
        String passwordEncriptado = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptado);

        // 3. Guardar el nuevo usuario en la base de datos
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // 4. Responder que fue creado con éxito
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Usuario registrado con éxito",
                "rut", usuarioGuardado.getRut()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        // 1. Buscar al usuario por su RUT en la base de datos
        Optional<Usuario> usuarioOpt = usuarioRepository.findByRut(loginRequest.getRut());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas (RUT no encontrado)"));
        }

        Usuario usuario = usuarioOpt.get();

        // 2. Comparar la contraseña ingresada con el hash encriptado de la BD
        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas (Contraseña incorrecta)"));
        }

        // 3. Generar el token JWT usando el RUT
        String token = jwtUtil.generarToken(usuario.getRut());

        // 4. Responderle al cliente con el Token listo para usar
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("rut", usuario.getRut());
        response.put("nombre", usuario.getNombre());

        return ResponseEntity.ok(response);
    }
}
