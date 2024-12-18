package com.marcesoft.traingo.aplicacion.excepciones;

/**
 * Excepción lanzada cuando se produce un error al leer el siguiente evento del archivo
 */
public class LecturaSiguienteEventoException extends CargaArchivoFallidaException {

    /**
     * Constructor sin parámetros
     */
    public LecturaSiguienteEventoException() {
        super("Error al leer el siguiente evento del archivo");
    }
}
