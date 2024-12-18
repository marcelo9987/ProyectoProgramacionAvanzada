package com.marcesoft.traingo.aplicacion.excepciones;

/**
 * Excepción que se lanza cuando la carga de un archivo falla.
 */
public abstract class CargaArchivoFallidaException extends RuntimeException {
    CargaArchivoFallidaException(String mensaje) {
        super(mensaje);
    }

}
