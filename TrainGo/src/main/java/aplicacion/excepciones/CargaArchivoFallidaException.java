package aplicacion.excepciones;

public abstract class CargaArchivoFallidaException extends RuntimeException {
    public CargaArchivoFallidaException(String mensaje) {
        super(mensaje);
    }

}
