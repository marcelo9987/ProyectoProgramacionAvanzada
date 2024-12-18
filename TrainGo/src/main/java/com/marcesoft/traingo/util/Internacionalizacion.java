package com.marcesoft.traingo.util;

import com.marcesoft.traingo.aplicacion.FachadaAplicacion;
import com.marcesoft.traingo.aplicacion.enums.EnumIdioma;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Clase que se encarga de la internacionalización de la aplicación.
 */
public final class Internacionalizacion {
    private ResourceBundle bundle;

    private static Internacionalizacion instance;

    private Internacionalizacion() {
        super();

        Locale.setDefault(Locale.of("es", "ES"));

        this.bundle = ResourceBundle.getBundle("gui", Locale.getDefault());

    }

    /**
     * Devuelve la instancia de la clase Internacionalizacion.
     *
     * @return Devuelve la instancia de la clase Internacionalizacion.
     */
    public static Internacionalizacion getInstance() {
        if (instance == null) {
            instance = new Internacionalizacion();
        }
        return instance;
    }

    /**
     * Cambia el idioma de la aplicación.
     *
     * @param idioma Idioma al que se quiere cambiar.
     *               Opciones:
     *               ESPAÑOL,
     *               GALEGO,
     *               INGLES.
     */
    public void cambiarIdioma(@NotNull EnumIdioma idioma) {
        switch (idioma) {
            case ESPANHOL:
                bundle = ResourceBundle.getBundle("gui", Locale.of("es", "ES"));
                break;

            case GALEGO:
                bundle = ResourceBundle.getBundle("gui", Locale.of("gl", "ES"));
                break;

            case INGLES:
                bundle = ResourceBundle.getBundle("gui", Locale.of("en", "US"));
                break;
        }
        ResourceBundle.clearCache();
    }

    /**
     * Devuelve el ResourceBundle actual.
     * @return Devuelve el ResourceBundle actual.
     */
    public ResourceBundle getBundle() {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("gui", Locale.getDefault());
        }
        return bundle;
    }


}
