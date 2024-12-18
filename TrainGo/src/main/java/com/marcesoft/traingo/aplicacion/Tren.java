package com.marcesoft.traingo.aplicacion;

import java.util.UUID;

/**
 * Clase que representa un tren
 *
 * @param id  Identificador del tren
 * @param num Número del tren
 */
public record Tren(UUID id, int num) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tren tren)) {
            return false;
        }

        return id.equals(tren.id());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
