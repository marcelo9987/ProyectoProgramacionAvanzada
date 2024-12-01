package modelo.formatos;


import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Clase que extiende de MaskFormatter para formatear fechas. Además, comprueba que la fecha sea válida.
 * Si la fecha no es válida, lanza una excepción.
 *
 * @see javax.swing.text.MaskFormatter
 */
public class FormatedFecha extends javax.swing.text.MaskFormatter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JLabel etiquetaError;

    public FormatedFecha(JLabel etiquetaError) throws java.text.ParseException {
        super("##/##/####"); // Formato de la fecha
        setAllowsInvalid(false);
        setOverwriteMode(true);
        this.etiquetaError = etiquetaError; // Etiqueta donde se mostrará el error en el panel principal
    }

    private static boolean comprobarFechaBienCodificada(int dia, int mes, int anho) {
        return (dia < 0) || (dia > 31) || (mes < 0) || (mes > 12) || (anho < 1900) || (anho > 2099);
    }

    @Nullable
    @Override
    public Object stringToValue(String string) throws java.text.ParseException {
        if (string == null) {
            return null;
        }
        if (string.equals("  /  /    ")) {
            return null;
        }
        String[] fecha = string.split("/");
        if (fecha.length != 3) {
            this.logger.error("Formato de fecha incorrecto");
            this.etiquetaError.setText("Formato de fecha incorrecto");
            throw new java.text.ParseException("Formato de fecha incorrecto", 0);
        }
        int dia = Integer.parseInt(fecha[0]);
        int mes = Integer.parseInt(fecha[1]);
        int anho = Integer.parseInt(fecha[2]);
        if (comprobarFechaBienCodificada(dia, mes, anho)) {
            this.logger.error("Fecha incorrecta");
            this.etiquetaError.setText("Fecha incorrecta");
            this.etiquetaError.setVisible(true);
            throw new java.text.ParseException("Fecha incorrecta", -1); // Aún no está del todo hecho, pero va por buen camino
        }
        this.etiquetaError.setVisible(false);
        return string;
    }
}
