package aplicacion.anotaciones;

import aplicacion.anotaciones.validadores.ValidadorNoNegativo;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidadorNoNegativo.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface NoNegativo {
    String message() default "";//"El valor del campo no puede ser negativo";

}
