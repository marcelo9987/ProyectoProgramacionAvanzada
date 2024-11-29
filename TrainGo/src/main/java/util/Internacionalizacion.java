package util;

import modelo.Enums.EnumIdioma;

import java.util.Locale;
import java.util.ResourceBundle;

public class Internacionalizacion {
    private ResourceBundle bundle;

    private Internacionalizacion() {

        Locale.setDefault(Locale.of("es", "ES"));

        this.bundle = ResourceBundle.getBundle("gui", Locale.getDefault());

    }

    public static Internacionalizacion getInstance() {
        return new Internacionalizacion();
    }


    public void cambiarIdioma(EnumIdioma idioma) {
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

    public ResourceBundle getBundle() {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("gui", Locale.getDefault());
        }
        return bundle;
    }


}
