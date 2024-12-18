package com.marcesoft.traingo.dao.constantes;

import org.jetbrains.annotations.NonNls;

/**
 * Clase que contiene las constantes de las etiquetas XML de las circulaciones
 */
public class TagsXMLCirculacion {
    /**
     * Etiqueta para la circulación
     */
    @NonNls
    public static final String CIRCULACION        = "circulacion";
    /**
     * Etiqueta para el UUID
     */
    @NonNls
    public static final String UUID               = "uuid";
    /**
     * Etiqueta para el tren
     */
    @NonNls
    public static final String TREN               = "tren_id";
    /**
     * Etiqueta para la ruta
     */
    @NonNls
    public static final String RUTA               = "ruta";
    /**
     * Etiqueta para el Estado
     */
    @NonNls
    public static final String ESTADO             = "estado";
    /**
     * Etiqueta para la fecha de salida
     */
    @NonNls
    public static final String HORA_SALIDA        = "hora_salida";
    /**
     * Etiqueta para la fecha de llegada
     */
    @NonNls
    public static final String HORA_LLEGADA_REAL  = "hora_llegada";
    /**
     * Etiqueta para el precio por asiento
     */
    @NonNls
    public static final String PRECIO_POR_ASIENTO = "precio_por_asiento";

    private TagsXMLCirculacion() {
        super();
    }
}
