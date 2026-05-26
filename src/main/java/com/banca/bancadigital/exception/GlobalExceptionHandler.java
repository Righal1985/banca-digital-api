package com.banca.bancadigital.exception;

import com.banca.bancadigital.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Le dice a Spring que esta clase intercepta todos los errores de los controladores
public class GlobalExceptionHandler {

    // Este método se activa específicamente cuando ocurre un RuntimeException (nuestros "throw new RuntimeException")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> manejarRuntimeException(RuntimeException ex, HttpServletRequest request) {

        // Creamos el JSON de error con el formato del banco
        ErrorResponseDTO errorBancario = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),               // Código 400 (Petición incorrecta)
                "ERROR_DE_NEGOCIO_BANCARIO",                  // Categoría del error
                ex.getMessage(),                              // El mensaje personalizado que pusimos en el Service
                request.getRequestURI()                       // La URL que falló
        );

        // Devolvemos el DTO con un estado HTTP 400 en vez del  Error 500
        return new ResponseEntity<>(errorBancario, HttpStatus.BAD_REQUEST);
    }
}
