package gui.modelos;

import aplicacion.Reserva;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
// | TREN | ORIGEN | DESTINO | HORA |

public class ModeloTablaReservas extends AbstractTableModel {

    List<Reserva> reservas;

    public ModeloTablaReservas(List<Reserva> reservas) {
        super();
        this.reservas = reservas;
        this.fireTableDataChanged();
    }

    public ModeloTablaReservas() {
        super();
        this.reservas = new ArrayList<>();
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
        this.fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Tren";
            case 1:
                return "Origen";
            case 2:
                return "Destino";
            case 3:
                return "Hora";
            default:
                return "ERROR: COL._NO_VALIDA";
        }
    }

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Reserva reserva = this.reservas.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return reserva.numeroTren();
            case 1:
                return reserva.nombreOrigen();
            case 2:
                return reserva.nombreDestino();
            case 3:
                return reserva.fechaHoraSalidaImprimible();
            default:
                return "ERROR: COL._NO_VALIDA";
        }
    }
}
