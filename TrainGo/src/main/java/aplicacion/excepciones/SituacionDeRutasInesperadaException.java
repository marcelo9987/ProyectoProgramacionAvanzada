package aplicacion.excepciones;

/**
 * Excepción lanzada cuando se detecta una situación inesperada en la gestión de rutas
 */
public class SituacionDeRutasInesperadaException extends IllegalStateException {
    public SituacionDeRutasInesperadaException(String mensaje) {
        super(mensaje);
    }
}
