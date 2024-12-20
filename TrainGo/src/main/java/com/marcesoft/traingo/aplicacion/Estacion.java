package com.marcesoft.traingo.aplicacion;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;

/**
 * Clase que representa una estación
 *
 * @param ciudad Ciudad en la que se encuentra la estación
 */
public record Estacion(String ciudad) implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * Constructor del record Estacion
     *
     * @param ciudad Ciudad en la que se encuentra la estación
     */
    public Estacion {
        if (ciudad == null || ciudad.isBlank()) {
            LoggerFactory.getLogger(this.getClass()).warn("La ciudad no puede ser nula o vacía");
            throw new IllegalArgumentException("La ciudad no puede ser nula o vacía");
        }
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Estacion{"
                + "ciudad='" + ciudad + '\''
                + '}';
    }
}
