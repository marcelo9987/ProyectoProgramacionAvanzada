package com.marcesoft.traingo.aplicacion.excepciones;

/**
 * Excepción lanzada cuando se detecta una situación inesperada en la gestión de rutas
 */
public class SituacionDeRutasInesperadaException extends IllegalStateException {
    /**
     * Constructor de la excepción
     * @param mensaje mensaje de error
     */
    public SituacionDeRutasInesperadaException(String mensaje) {
        super(mensaje);
    }
}
