package modelo;

import org.slf4j.LoggerFactory;

public record Estacion(String ciudad) {

    public Estacion {
        if (ciudad == null || ciudad.isBlank()) {
            LoggerFactory.getLogger(this.getClass()).warn("La ciudad no puede ser nula o vacía");
            throw new IllegalArgumentException("La ciudad no puede ser nula o vacía");
        }
    }

}
