package util;

import aplicacion.Estacion;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Ortograficos {

    private Ortograficos() {
        super();
    }

    /**
     * Compara dos cadenas alfabéticamente.
     *
     * @param s1 Primera cadena a comparar.
     *           No puede ser nula.
     *           No puede ser vacía.
     * @param s2 Segunda cadena a comparar.
     * @return Devuelve un número negativo si s1 es menor que s2, 0 si son iguales y un número positivo si s1 es mayor que s2.
     */
    @Contract(pure = true)
    private static int compararAlfabeticamente(@NotNull String s1, String s2) {
        return s1.compareTo(s2);
    }


    /**
     * Envoltorio para facilitar la comparación alfabética de dos estaciones.
     *
     * @param e1 Primera estación a comparar.
     * @param e2 Segunda estación a comparar.
     * @return Devuelve un número negativo si e1 es menor que e2, 0 si son iguales y un número positivo si e1 es mayor que e2.
     * @see #compararAlfabeticamente(String, String)
     */
    @Contract(pure = true)
    public static int Wrap2EstacionCompararAlfabeticamente(@NotNull Estacion e1, @NotNull Estacion e2) {
        return compararAlfabeticamente(e1.ciudad(), e2.ciudad());
    }
}
