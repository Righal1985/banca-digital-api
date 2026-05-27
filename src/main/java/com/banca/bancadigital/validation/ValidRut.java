package com.banca.bancadigital.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RutValidator.class) // amarramos la anotación con la lógica real
@Target({ElementType.FIELD, ElementType.PARAMETER}) // Indica que se puede usar en atributos y parámetros
@Retention(RetentionPolicy.RUNTIME) // Se ejecuta en tiempo de ejecución
public @interface ValidRut {
    String message() default "El RUT ingresado no es válido"; // Mensaje por defecto
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
