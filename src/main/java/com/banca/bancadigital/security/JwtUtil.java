package com.banca.bancadigital.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // Una clave secreta larga y segura para firmar digitalmente los tokens del banco
    private final String SECRET_KEY_STRING = "B4nc0D1g1t4lS3cr3tK3y_2026_@F1nt3ch_Secure_Key_Long_Enough";
    private final long EXPIRATION_TIME = 900_000; // El token expira en 15 minutos (en milisegundos)


    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token firmado para el usuario usando su RUT
     */
    public String generarToken(String rut) {
        return Jwts.builder()
                .subject(rut)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Valida el token y extrae el RUT si la firma es auténtica y está vigente
     */
    public String validarTokenAndObtenerRut(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            // Si el token fue alterado, expiró o es inválido, devuelve null
            return null;
        }
    }
}