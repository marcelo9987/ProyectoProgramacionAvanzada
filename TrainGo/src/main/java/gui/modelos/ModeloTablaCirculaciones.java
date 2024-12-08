package gui.modelos;

import aplicacion.Circulacion;
import aplicacion.anotaciones.NoNegativo;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
//| ORIGEN | DESTINO | HORA_SALIDA | PRECIO |

public final class ModeloTablaCirculaciones extends AbstractTableModel {
    private static final int               ORIGEN      = 0;
    private static final int               DESTINO     = 1;
    private static final int               HORA_SALIDA = 2;
    private static final int               PRECIO      = 3;
    private static final int               ESTADO      = 4;
    private final        List<Circulacion> listaCirculaciones;
    private              ResourceBundle    bundle;

    public ModeloTablaCirculaciones(List<Circulacion> listaCirculaciones) {
        super();
        this.listaCirculaciones = listaCirculaciones;
        this.listaCirculaciones.sort(Circulacion::compareTo);
        this.fireTableDataChanged();
    }

    public ModeloTablaCirculaciones(ResourceBundle bundle) {
        super();
        this.bundle = bundle;
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


    @Nullable
    public Circulacion getCirculacionEnFila(int fila) {
        if (fila < 0 || fila >= this.listaCirculaciones.size()) {
            return null;
        }
        return this.listaCirculaciones.get(fila);
    }

    @Override
    public int getColumnCount() {
        return 5;
    }


    @Override
    public Object getValueAt(@NoNegativo @MagicConstant(intValues = {ORIGEN, DESTINO, HORA_SALIDA, PRECIO}) int rowIndex, @NoNegativo int columnIndex) {
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
            case ESTADO -> {
                return circulacionSeleccionada.estado();
            }
            default -> throw new IllegalStateException("Unexpected value: " + columnIndex);
        }
    }

    public Circulacion getCirculacion(int fila) {
        return this.listaCirculaciones.get(fila);
    }

    @NotNull
    @Override
    public String getColumnName(@NoNegativo @MagicConstant(intValues = {ORIGEN, DESTINO, HORA_SALIDA, PRECIO}) int column) {
        switch (column) {
            case ORIGEN -> {
                return bundle.getString("origen");
            }
            case DESTINO -> {
                return bundle.getString("destino");
            }
            case HORA_SALIDA -> {
                return "Hora de salida";
            }
            case PRECIO -> {
                return bundle.getString("precio");
            }
            case ESTADO -> {
                return bundle.getString("estado");
            }
            default -> throw new IllegalStateException("Unexpected value: " + column);
        }

    }

}