package aplicacion.excepciones;

public class EventoNuloException extends CargaArchivoFallidaException {
    public EventoNuloException(String message) {
        super("Error al cargar el evento: " + message);
    }

    public EventoNuloException() {
        super("Error al cargar el evento: el evento es nulo");
    }
}
