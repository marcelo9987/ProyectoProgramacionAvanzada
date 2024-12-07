package gui.modelos;

import aplicacion.Circulacion;
import aplicacion.anotaciones.NoNegativo;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public final class ModeloTablaCirculaciones extends AbstractTableModel {
    private static final int ORIGEN = 0;
    private static final int DESTINO = 1;
    private static final int HORA_SALIDA = 2;
    private static final int PRECIO = 3;
    private final List<Circulacion> listaCirculaciones;
    @MagicConstant(intValues = {ORIGEN, DESTINO, HORA_SALIDA, PRECIO})
    public int PRIMERA_COLUMNA = ORIGEN;

    public ModeloTablaCirculaciones(List<Circulacion> listaCirculaciones) {
        super();
        this.listaCirculaciones = listaCirculaciones;
        this.listaCirculaciones.sort(Circulacion::compareTo);
        this.fireTableDataChanged();
    }

    public ModeloTablaCirculaciones() {
        super();
        this.listaCirculaciones = new ArrayList<>();
    }

    public void setListaCirculaciones(List<Circulacion> listaCirculaciones) {
        this.listaCirculaciones.clear();
        this.listaCirculaciones.addAll(listaCirculaciones);
        this.listaCirculaciones.sort(Circulacion::compareTo);
        this.fireTableDataChanged();
    }

    public void addCirculacion(Circulacion circulacion) {
        this.listaCirculaciones.add(circulacion);
        this.listaCirculaciones.sort(Circulacion::compareTo);
        this.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return this.listaCirculaciones.size();
    }


    @Override
    public int getColumnCount() {
        return 4;
    }

    public Circulacion getCirculacion(int fila) {
        return this.listaCirculaciones.get(fila);
    }


    @Override
    public Object getValueAt(@NoNegativo int rowIndex, @NoNegativo int columnIndex) {
        Circulacion circulacionSeleccionada = this.listaCirculaciones.get(rowIndex);
        switch (columnIndex) {
            case ORIGEN -> {
                return circulacionSeleccionada.ciudadOrigen();
            }
            case DESTINO -> {
                return circulacionSeleccionada.ciudadDestino();
            }
            case HORA_SALIDA -> {
                return circulacionSeleccionada.getCadenaHoraFechaSalida();
            }
            case PRECIO -> {
                return circulacionSeleccionada.getPrecioPorAsiento() + "€";
            }
            default -> throw new IllegalStateException("Unexpected value: " + columnIndex);
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case ORIGEN -> {
                return "Origen";
            }
            case DESTINO -> {
                return "Destino";
            }
            case HORA_SALIDA -> {
                return "Hora de salida";
            }
            case PRECIO -> {
                return "Precio";
            }
            default -> throw new IllegalStateException("Unexpected value: " + column);
        }

    }

    @Override
    public int findColumn(@NotNull String columnName) {
        switch (columnName) {
            case "Origen" -> {
                return ORIGEN;
            }
            case "Destino" -> {
                return DESTINO;
            }
            case "Hora de salida" -> {
                return HORA_SALIDA;
            }
            case "Precio" -> {
                return PRECIO;
            }
            default -> throw new IllegalStateException("Unexpected value: " + columnName);
        }
    }
}