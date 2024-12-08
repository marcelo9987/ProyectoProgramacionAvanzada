package aplicacion.excepciones;

import org.jetbrains.annotations.NotNull;

public class ProcesadoSiguienteEventoException extends CargaArchivoFallidaException {
    public ProcesadoSiguienteEventoException(String message) {
        super(message);
    }

    public ProcesadoSiguienteEventoException(@NotNull Exception cause) {
        super(cause.toString());
    }
}
