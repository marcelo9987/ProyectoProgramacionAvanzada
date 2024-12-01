package gui.modelos;

import modelo.Circulacion;
import org.intellij.lang.annotations.MagicConstant;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ModeloTablaCirculaciones extends AbstractTableModel {
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
    }

    public ModeloTablaCirculaciones() {
        super();
        this.listaCirculaciones = new ArrayList<>();
    }

    public void setListaCirculaciones(List<Circulacion> listaCirculaciones) {
        this.listaCirculaciones.clear();
        this.listaCirculaciones.addAll(listaCirculaciones);
        this.fireTableDataChanged();
    }

    public void addCirculacion(Circulacion circulacion) {
        this.listaCirculaciones.add(circulacion);
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


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Circulacion circulacionSeleccionada = this.listaCirculaciones.get(rowIndex);
        switch (columnIndex) {
            case ORIGEN -> {
                return circulacionSeleccionada.ciudadOrigen();
            }
            case DESTINO -> {
                return circulacionSeleccionada.ciudadDestino();
            }
            case HORA_SALIDA -> {
                return circulacionSeleccionada.getHoraSalida();
            }
            case PRECIO -> {
                return circulacionSeleccionada.getPrecioPorAsiento();
            }
            default -> throw new IllegalStateException("Unexpected value: " + columnIndex);
        }
    }
}
