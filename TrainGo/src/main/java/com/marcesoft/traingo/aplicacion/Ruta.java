package com.marcesoft.traingo.aplicacion;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

/**
 * Clase que representa una ruta
 *
 * @param origen    Estación de origen de la ruta
 * @param destino   Estación de destino de la ruta
 * @param distancia Distancia entre las dos estaciones
 */
public record Ruta(Estacion origen, Estacion destino, int distancia) implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * @return Ciudad de origen de la ruta
     */
    @Contract(pure = true)
    public String ciudadOrigen() {
        return origen.ciudad();
    }

    /**
     * @return Ciudad de destino de la ruta
     */
    @Contract(pure = true)
    public String ciudadDestino() {
        return destino.ciudad();
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Ruta{"
                + "origen=" + origen
                + ", destino=" + destino
                + ", distancia=" + distancia
                + '}';
    }
}

