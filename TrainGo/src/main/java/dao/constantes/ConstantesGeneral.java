package dao.constantes;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Clase que contiene las constantes generales de la aplicación para los ficheros XML.
 */
public class ConstantesGeneral {
    /**
     * Cabecera del fichero de circulaciones
     */
    public static final String FICHERO_CIRCULACION = "circulaciones";
    /**
     * Cabecera del fichero de estaciones
     */
    public static final String FICHERO_ESTACION    = "estaciones";
    /**
     * Cabecera del fichero de reservas
     */
    public static final String FICHERO_RESERVA     = "reservas";
    /**
     * Cabecera del fichero de rutas
     */
    public static final String FICHERO_RUTA        = "rutas";
    /**
     * Cabecera del fichero de trenes
     */
    public static final String FICHERO_TREN        = "trenes";
    /**
     * Cabecera del fichero de usuarios
     */
    public static final String FICHERO_USUARIO     = "usuarios";

    private ConstantesGeneral() {
        super();
    }

    /**
     * Método que comprueba si el tipo de fichero es válido.
     *
     * @param tipo tipo de fichero
     * @return true si el tipo es válido, false en caso contrario
     */
    @Contract(pure = true)
    public static boolean tipoValido(@NotNull String tipo) {
        return tipo.equals(FICHERO_CIRCULACION) || tipo.equals(FICHERO_ESTACION) || tipo.equals(FICHERO_RESERVA) || tipo.equals(FICHERO_RUTA) || tipo.equals(FICHERO_TREN) || tipo.equals(FICHERO_USUARIO);
    }
}
