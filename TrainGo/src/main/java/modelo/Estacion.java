package modelo;

import org.slf4j.LoggerFactory;

public record Estacion(String ciudad, String nombre) {

    public Estacion {
        if (ciudad == null || ciudad.isBlank()) {
            LoggerFactory.getLogger(this.getClass()).warn("La ciudad no puede ser nula o vacía");
            throw new IllegalArgumentException("La ciudad no puede ser nula o vacía");
        }
        if (nombre == null || nombre.isBlank()) {
            LoggerFactory.getLogger(this.getClass()).warn("El nombre no puede ser nulo o vacío");
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
    }
}
