package dao.constantes;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ConstantesGeneral {
    public static final String FICHERO_CIRCULACION = "circulaciones";
    public static final String FICHERO_ESTACION    = "estaciones";
    public static final String FICHERO_RESERVA     = "reservas";
    public static final String FICHERO_RUTA        = "rutas";
    public static final String FICHERO_TREN        = "trenes";
    public static final String FICHERO_USUARIO     = "usuarios";

    private ConstantesGeneral() {
        super();
    }

    @Contract(pure = true)
    public static boolean tipoValido(@NotNull String tipo) {
        return tipo.equals(FICHERO_CIRCULACION) || tipo.equals(FICHERO_ESTACION) || tipo.equals(FICHERO_RESERVA) || tipo.equals(FICHERO_RUTA) || tipo.equals(FICHERO_TREN) || tipo.equals(FICHERO_USUARIO);
    }
}
