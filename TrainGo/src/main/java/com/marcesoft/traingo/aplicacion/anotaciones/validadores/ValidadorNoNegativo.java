package com.marcesoft.traingo.aplicacion.anotaciones.validadores;


import com.marcesoft.traingo.aplicacion.anotaciones.NoNegativo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.math.BigInteger;

/**
 * Validador de la anotación @NoNegativo
 */
public class ValidadorNoNegativo implements ConstraintValidator<NoNegativo, BigInteger> {

    /**
     * Valida que los campos marcados con la anotación @NoNegativo no sean negativos
     *
     * @param obj Objeto a validar
     * @throws IllegalArgumentException Si algún campo marcado con @NoNegativo es negativo
     */
    @Contract(pure = true)
    public static void validate(@NotNull Object obj) throws IllegalArgumentException {
        Field[] campos = obj.getClass().getDeclaredFields();

        for (Field campo : campos) {
            Object valorCampo = null;
            if (campo.isAnnotationPresent(NoNegativo.class)) {
                campo.setAccessible(true);

                try {
                    valorCampo = campo.get(obj);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Error al acceder al campo");
                }

            }

            if (valorCampo instanceof BigInteger) {
                if (((BigInteger) valorCampo).compareTo(BigInteger.ZERO) < 0) {
                    throw new IllegalArgumentException("El valor del campo " + campo.getName() + " no puede ser negativo");
                }
            }
        }
    }


    /**
     * Inicializa el validador
     * @param constraintAnnotation Anotación a inicializar
     */
    @Override
    public void initialize(NoNegativo constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Valida que un objeto de tipo BigInteger no sea negativo
     * @param bigInteger objeto a validar
     * @param constraintValidatorContext contexto de validación
     *
     * @return true si el objeto no es negativo, false en caso contrario
     */
    @Contract(pure = true)
    @Override
    public boolean isValid(@NotNull BigInteger bigInteger, ConstraintValidatorContext constraintValidatorContext) {
        return bigInteger.compareTo(BigInteger.ZERO) >= 0;
    }


}
