package aplicacion.formatos;


import aplicacion.anotaciones.NoNegativo;
import org.jetbrains.annotations.Contract;
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
public final class FormatedFecha extends javax.swing.text.MaskFormatter {

    private static final int    NUMERO_MINIMO_MESES_EN_ANHO = 1;
    private static final int    NUMERO_MAX_MESES_POR_ANHO   = 12;
    /**
     * Año mínimo para la fecha de búsqueda. Esta es la del primer viaje en tren en la red ferroviaria española (Barcelona-Mataró).
     *
     * @see <a href="https://www.transportes.gob.es/el-ministerio/campanas-de-publicidad/2021-anio-europeo-del-ferrocarril/conociendo-el-ferrocarril/12-hitos">Historia del ferrocarril</a>
     */
    private static final int    ANHO_MINIMO_FECHA_BUSQUEDA  = 1848;
    private static final Logger logger                      = LoggerFactory.getLogger(FormatedFecha.class);

    private final JLabel etiquetaError;

    /**
     * Constructor de la clase FormatedFecha
     *
     * @param etiquetaError Etiqueta donde se mostrará el error en el panel principal
     * @throws java.text.ParseException si la fecha no es válida
     */
    public FormatedFecha(JLabel etiquetaError) throws java.text.ParseException {
        super("##/##/####"); // Formato de la fecha
        setAllowsInvalid(false);
        setOverwriteMode(true);
        this.etiquetaError = etiquetaError; // Etiqueta donde se mostrará el error en el panel principal
    }

    @Contract(pure = true, value = "null -> null")
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
            logger.error("Formato de fecha incorrecto");
            this.etiquetaError.setText("Formato de fecha incorrecto");
            throw new java.text.ParseException("Formato de fecha incorrecto", 0);
        }
        int dia = Integer.parseInt(fecha[0]);
        int mes = Integer.parseInt(fecha[1]);
        int anho = Integer.parseInt(fecha[2]);
        if (comprobarFechaBienCodificada(dia, mes, anho)) {
            logger.error("Fecha incorrecta");
            this.etiquetaError.setText("Fecha incorrecta");
            this.etiquetaError.setVisible(true);
            throw new java.text.ParseException("Fecha incorrecta", -1); // Aún no está del todo hecho, pero va por buen camino
        }
        this.etiquetaError.setVisible(false);
        return string;
    }

    /**
     * Comprueba si la fecha está bien codificada. Se considera que una fecha está bien codificada si el día se encuentra dentro de los límites del mes, y el mes se encuentra dentro de los límites del año.
     * Además, se comprueban los años bisiestos y que el año sea mayor que el año mínimo para la fecha de búsqueda.
     *
     * @param dia  Día de la fecha
     * @param mes  Mes de la fecha
     * @param anho Año de la fecha
     * @return true si la fecha está bien codificada, false en caso contrario
     * @apiNote este método usa como inspiración el código de la función day_of_year (Brian W. Kernighan y Dennis M. Ritchie) para gestionar los bisiestos y los días de los meses.
     * @see #ANHO_MINIMO_FECHA_BUSQUEDA
     */
    @Contract(pure = true)
    private static boolean comprobarFechaBienCodificada(@NoNegativo int dia, @NoNegativo int mes, @NoNegativo int anho) {
        //                   Ene Feb Mar Abr May Jun Jul Ago Sep Oct Nov Dic
        // array sacado de: Brian W. Kernighan y Dennis M. Ritchie, "The C Programming Language", 2nd ed., 1988, p. 142
        char[][] daytab = {
                {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
                {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}
        };
        boolean esBisiesto = anho % 4 == 0 && (anho % 100 != 0 || anho % 400 == 0);

        if (anho < ANHO_MINIMO_FECHA_BUSQUEDA) {
            return false;
        }

        if (mes < NUMERO_MINIMO_MESES_EN_ANHO || mes > NUMERO_MAX_MESES_POR_ANHO) {
            return true;
        }

        if (dia < 1) {
            return false;
        }

        return dia > daytab[esBisiesto ? 1 : 0][mes];
    }
}
