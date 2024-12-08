package gui.formularios;

import aplicacion.FachadaAplicacion;
import aplicacion.Reserva;
import gui.modelos.ModeloTablaReservas;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.ResourceBundle;

class FormularioMisReservas extends JFrame {

    private ResourceBundle bundle;

    FormularioMisReservas(@NotNull FachadaAplicacion fa) {
        super();

        this.bundle = fa.getBundleInstance();

        this.setTitle(bundle.getString("mis.reservas"));

        JTable tablaReservas = new JTable();

        ModeloTablaReservas modeloDeDatosTablaReservas = new ModeloTablaReservas(bundle);
        tablaReservas.setModel(modeloDeDatosTablaReservas);

        List<Reserva> reservas = fa.getReservasUsuario(fa.usuario());

        modeloDeDatosTablaReservas.setReservas(reservas);
        modeloDeDatosTablaReservas.fireTableDataChanged();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(tablaReservas);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.add(scrollPane);

        this.setSize(800, 600);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

}
