package modelo.excepciones;

/**
 * Excepción lanzada cuando se intenta acceder a un usuario que no existe.
 */
public final class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(String message) {
        super(message);
    }
}
