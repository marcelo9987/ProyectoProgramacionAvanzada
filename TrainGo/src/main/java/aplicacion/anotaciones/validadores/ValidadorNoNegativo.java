package aplicacion.anotaciones.validadores;


import aplicacion.anotaciones.NoNegativo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.math.BigInteger;

public class ValidadorNoNegativo implements ConstraintValidator<NoNegativo, BigInteger> {
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

    @Override
    public void initialize(NoNegativo constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(@NotNull BigInteger bigInteger, ConstraintValidatorContext constraintValidatorContext) {
        return bigInteger.compareTo(BigInteger.ZERO) >= 0;
    }


}
