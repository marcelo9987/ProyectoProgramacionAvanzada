package gui.formularios;

import aplicacion.FachadaAplicacion;
import aplicacion.Reserva;
import gui.modelos.ModeloTablaReservas;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

public class FormularioMisReservas extends JFrame {
    private final JTable            tablaReservas;
    private final FachadaAplicacion fa;

    public FormularioMisReservas(@NotNull FachadaAplicacion fa) {
        super();

        this.fa = fa;

        this.setTitle("Mis reservas");

        this.tablaReservas = new JTable();

        ModeloTablaReservas modeloDeDatosTablaReservas = new ModeloTablaReservas();
        this.tablaReservas.setModel(modeloDeDatosTablaReservas);

        List<Reserva> reservas = fa.getReservasUsuario(fa.usuario());

        modeloDeDatosTablaReservas.setReservas(reservas);
        modeloDeDatosTablaReservas.fireTableDataChanged();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(this.tablaReservas);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.add(scrollPane);

        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

}
