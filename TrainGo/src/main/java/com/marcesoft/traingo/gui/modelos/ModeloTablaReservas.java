package com.marcesoft.traingo.gui.modelos;

import com.marcesoft.traingo.aplicacion.Reserva;
import com.marcesoft.traingo.aplicacion.anotaciones.NoNegativo;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
// | TREN | ORIGEN | DESTINO | HORA |

/**
 * Modelo de tabla para las reservas de un usuario.
 */
public final class ModeloTablaReservas extends AbstractTableModel {


    private static final int TREN       = 0;
    private static final int ORIGEN     = 1;
    private static final int DESTINO    = 2;
    private static final int HORASALIDA = 3;
    private static final int ESTADO     = 4;

    private final List<Reserva>  reservas;
    private final ResourceBundle bundle;

    /**
     * Constructor de la clase ModeloTablaReservas
     * @param bu Bundle con la información de internacionalización
     */
    public ModeloTablaReservas(ResourceBundle bu) {
        super();
        this.reservas = new ArrayList<>();
        this.bundle = bu;
        this.fireTableDataChanged();
    }

    /**
     * Establece las reservas en la tabla en formato de List
     * @param reservas Lista de reservas
     */
    public void setReservas(List<Reserva> reservas) {
        this.reservas.clear();
        this.reservas.addAll(reservas);
        this.fireTableDataChanged();
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String getColumnName(@MagicConstant(intValues = {TREN, ORIGEN, DESTINO, HORASALIDA, ESTADO}) int column) {
        return switch (column) {
            case TREN -> bundle.getString("tren");
            case ORIGEN -> bundle.getString("origen");
            case DESTINO -> bundle.getString("destino");
            case HORASALIDA -> bundle.getString("hora.y.fecha.de.salida");
            case ESTADO -> bundle.getString("estado.trayecto");
            default -> "ERROR: COL._NO_VALIDA";
        };
    }

    @Override
    public int getRowCount() {
        return this.reservas.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(@NoNegativo @MagicConstant(intValues = {TREN, ORIGEN, DESTINO, HORASALIDA, ESTADO}) int rowIndex, @NoNegativo int columnIndex) {
        Reserva reserva = this.reservas.get(rowIndex);
        return switch (columnIndex) {
            case TREN -> reserva.numeroTren();
            case ORIGEN -> reserva.nombreOrigen();
            case DESTINO -> reserva.nombreDestino();
            case HORASALIDA -> reserva.fechaHoraSalidaImprimible();
            case ESTADO -> reserva.estado();
            default -> "ERROR: COL._NO_VALIDA";
        };
    }
}
