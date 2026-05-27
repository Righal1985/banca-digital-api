package com.banca.bancadigital.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RutValidator implements ConstraintValidator<ValidRut, String> {

    @Override
    public boolean isValid(String rut, ConstraintValidatorContext context) {
        //  Si el RUT viene vacío, dejamos que @NotBlank se encargue, no rompemos aquí
        if (rut == null || rut.trim().isEmpty()) {
            return true;
        }

        try {
            // Limpiar el RUT: quitar puntos, guiones y espacios, y pasarlo a mayúsculas
            String rutLimpio = rut.replace(".", "")
                    .replace("-", "")
                    .trim()
                    .toUpperCase();

            // Un RUT chileno válido tras limpiarlo debe tener entre 8 y 9 caracteres (ej: 12345678K o 98765432)
            if (rutLimpio.length() < 8 || rutLimpio.length() > 9) {
                return false;
            }


            char dvEntrante = rutLimpio.charAt(rutLimpio.length() - 1);
            String cuerpoRut = rutLimpio.substring(0, rutLimpio.length() - 1);


            int numeroRut = Integer.parseInt(cuerpoRut);

            // 4. ALGORITMO MÓDULO 11 REAL
            int suma = 0;
            int multiplicador = 2;

            // Recorrer el número de derecha a izquierda
            while (numeroRut > 0) {
                int digito = numeroRut % 10;
                suma += digito * multiplicador;
                numeroRut /= 10;

                multiplicador++;
                if (multiplicador > 7) {
                    multiplicador = 2;
                }
            }

            int resto = suma % 11;
            int resultadoDiferencia = 11 - resto;

            // Determinar cuál debería ser el DV correcto matemáticamente
            char dvCalculado;
            if (resultadoDiferencia == 11) {
                dvCalculado = '0';
            } else if (resultadoDiferencia == 10) {
                dvCalculado = 'K';
            } else {
                dvCalculado = Character.forDigit(resultadoDiferencia, 10);
            }

            //  Comparar si el DV que mandó el usuario es igual al que calculó el banco
            return dvEntrante == dvCalculado;

        } catch (Exception e) {
            // Si ocurre cualquier error de conversión (le metieron letras al cuerpo, etc.), el RUT es inválido
            return false;
        }
    }
}
