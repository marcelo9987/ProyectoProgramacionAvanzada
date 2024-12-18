package com.marcesoft.traingo.aplicacion.excepciones;

/**
 * Excepción que se lanza cuando la carga de un archivo falla.
 */
public abstract class CargaArchivoFallidaException extends RuntimeException {
    /**
     * Constructor de la excepción que recibe un mensaje
     * @param mensaje Mensaje que se mostrará al lanzar la excepción
     */
    CargaArchivoFallidaException(String mensaje) {
        super(mensaje);
    }

}
