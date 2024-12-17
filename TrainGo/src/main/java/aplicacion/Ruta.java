package aplicacion;

import org.jetbrains.annotations.Contract;

/**
 * Clase que representa una ruta
 *
 * @param origen    Estación de origen de la ruta
 * @param destino   Estación de destino de la ruta
 * @param distancia Distancia entre las dos estaciones
 */
public record Ruta(Estacion origen, Estacion destino, int distancia) {


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
}

