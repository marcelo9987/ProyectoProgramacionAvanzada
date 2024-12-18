package com.marcesoft.traingo.aplicacion.excepciones;

import org.jetbrains.annotations.NotNull;

/**
 * Excepción que se lanza cuando el procesado de un evento falla y se debe procesar el siguiente.
 */
public class ProcesadoSiguienteEventoException extends CargaArchivoFallidaException {

    /**
     * @param cause excepción original
     */
    public ProcesadoSiguienteEventoException(@NotNull Exception cause) {
        super(cause.toString());
    }
}
