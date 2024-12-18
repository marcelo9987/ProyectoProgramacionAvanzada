package com.marcesoft.traingo.aplicacion.enums;

/**
 * Enumerado que representa los estados de una circulación.
 * Una circulación es el viaje de un tren por una ruta
 */
public enum EnumCirculacion {
    /**
     * La circulación está en curso
     */
    EN_TRANSITO,
    /**
     * La circulación está programada (aún no ha comenzado)
     */
    PROGRAMADO,
    /**
     * La circulación ha finalizado
     */
    FINALIZADO,
    /**
     * La circulación ha sido cancelada
     */
    CANCELADO
}
