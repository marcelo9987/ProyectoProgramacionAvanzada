package gui.modelos;

import aplicacion.Circulacion;
import aplicacion.anotaciones.NoNegativo;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
//| ORIGEN | DESTINO | HORA_SALIDA | PRECIO |

/**
 * Modelo de tabla para las circulaciones
 */
public final class ModeloTablaCirculaciones extends AbstractTableModel {
    private static final int               ORIGEN      = 0;
    private static final int               DESTINO     = 1;
    private static final int               HORA_SALIDA = 2;
    private static final int               PRECIO      = 3;
    private static final int               ESTADO      = 4;
    private final List<Circulacion> listaCirculaciones;
    private final ResourceBundle    bundle;

    /**
     * @param bundle Bundle con la información de internacionalización
     */
    public ModeloTablaCirculaciones(ResourceBundle bundle) {
        super();
        this.bundle = bundle;
        this.listaCirculaciones = new ArrayList<>();
    }

    /**
     * @param listaCirculaciones Lista de circulaciones
     */
    public void setListaCirculaciones(List<Circulacion> listaCirculaciones) {
        this.listaCirculaciones.clear();
        this.listaCirculaciones.addAll(listaCirculaciones);
        this.listaCirculaciones.sort(Circulacion::compareTo);
        this.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return this.listaCirculaciones.size();
    }


    /**
     * @param fila Fila de la que se quiere obtener la circulación
     * @return Circulación en la fila indicada
     */
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
                return circulacionSeleccionada.precioPorAsiento() + "€";
            }
            case ESTADO -> {
                return circulacionSeleccionada.estado();
            }
            default -> throw new IllegalStateException("Unexpected value: " + columnIndex);
        }
    }

    /**
     * @param fila Fila de la que se quiere obtener la circulación
     * @return Circulación en la fila indicada
     */
    @Contract(pure = true)
    public Circulacion getCirculacion(int fila) {
        return this.listaCirculaciones.get(fila);
    }

    @NotNull
    @Override
    public String getColumnName(@NoNegativo @MagicConstant(intValues = {ORIGEN, DESTINO, HORA_SALIDA, PRECIO, ESTADO}) int column) {
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