package com.marcesoft.traingo.gui.formularios;

import com.marcesoft.traingo.aplicacion.FachadaAplicacion;
import com.marcesoft.traingo.aplicacion.Reserva;
import com.marcesoft.traingo.gui.modelos.ModeloTablaReservas;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Formulario que muestra las reservas realizadas por el usuario.
 */
final class FormularioMisReservas extends JFrame {

    FormularioMisReservas(@NotNull FachadaAplicacion fa) {
        super();

        ResourceBundle bundle = fa.getBundleInstance();

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
        this.setLocationRelativeTo(null);
    }

}
