package com.marcesoft.traingo.dao.constantes;

import org.jetbrains.annotations.NonNls;

/**
 * Clase que contiene las constantes de las etiquetas XML de las rutas
 */
public class TagsXMLRuta {
    /**
     * Etiqueta para la ruta
     */
    @NonNls
    public static final String XML_TAG_RUTA      = "ruta";
    /**
     * Etiqueta para el origen
     */
    @NonNls
    public static final String XML_TAG_ORIGEN    = "origen";
    /**
     * Etiqueta para el destino
     */
    @NonNls
    public static final String XML_TAG_DESTINO   = "destino";
    /**
     * Etiqueta para la distancia
     */
    @NonNls
    public static final String XML_TAG_DISTANCIA = "distancia";

    private TagsXMLRuta() {
        super();
    }
}
