package util;

import modelo.Enums.EnumIdioma;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.ResourceBundle;

public class Internacionalizacion {
    private ResourceBundle bundle;

    private static Internacionalizacion instance;

    private Internacionalizacion() {

        Locale.setDefault(Locale.of("es", "ES"));

        this.bundle = ResourceBundle.getBundle("gui", Locale.getDefault());

    }

    /**
     * Devuelve la instancia de la clase Internacionalizacion.
     *
     * @return Devuelve la instancia de la clase Internacionalizacion.
     * @implNote Patrón Singleton: Este método es necesario para poder utilizar la clase Internacionalizacion en el resto del código.
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
