package com.marcesoft.traingo;
import com.marcesoft.traingo.aplicacion.FachadaAplicacion;

/**
 * Clase principal de la aplicación
 */
public class Main {
    /**
     * Método main de la aplicación
     * @param args Argumentos de la aplicación (no se usan)
     */
    public static void main(String[] args) {
        FachadaAplicacion fa = new FachadaAplicacion();
        fa.ejecutar();
    }

}
