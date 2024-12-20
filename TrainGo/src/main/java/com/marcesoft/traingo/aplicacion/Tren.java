package com.marcesoft.traingo.aplicacion;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * Clase que representa un tren
 *
 * @param id  Identificador del tren
 * @param num Número del tren
 */
public record Tren(UUID id, int num) implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

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

    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Tren{"
                + "id=" + id
                + ", num=" + num
                + '}';
    }
}
