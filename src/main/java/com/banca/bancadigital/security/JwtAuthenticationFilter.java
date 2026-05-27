package com.banca.bancadigital.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // Constructor para conectar con nuestra utilidad de tokens
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Extraemos la cabecera de autorización de la petición HTTP
        String authHeader = request.getHeader("Authorization");

        // 2. Si viene el token con el prefijo "Bearer ", lo procesamos
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Cortamos "Bearer " y dejamos el token puro
            String rut = jwtUtil.validarTokenAndObtenerRut(token); // Validamos

            // 3. Si el RUT es válido, le damos luz verde en la seguridad de Spring
            if (rut != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(rut, null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 4. Continuamos hacia el Controller
        filterChain.doFilter(request, response);
    }
}
