package aplicacion.excepciones;

public class LecturaSiguienteEventoException extends CargaArchivoFallidaException {
    public LecturaSiguienteEventoException(String message) {
        super(message);
    }

    public LecturaSiguienteEventoException() {
        super("Error al leer el siguiente evento del archivo");
    }
}
