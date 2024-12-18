package com.marcesoft.traingo.aplicacion;

import org.slf4j.LoggerFactory;

/**
 * Clase que representa una estación
 *
 * @param ciudad Ciudad en la que se encuentra la estación
 */
public record Estacion(String ciudad) {

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

}
