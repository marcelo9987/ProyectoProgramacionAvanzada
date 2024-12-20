package com.marcesoft.traingo.aplicacion.anotaciones;

import com.marcesoft.traingo.aplicacion.anotaciones.validadores.ValidadorNoNegativo;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

/**
 * Anotación que indica que un campo no puede ser negativo
 */
@Documented
@Constraint(validatedBy = ValidadorNoNegativo.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface NoNegativo {
    /**
     * Mensaje de error que se mostrará si el campo es negativo
     * @return Mensaje de error que se mostrará si el campo es negativo
     */
    String message() default "";//"El valor del campo no puede ser negativo";

}
