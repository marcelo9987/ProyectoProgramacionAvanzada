package gui.formularios;

import aplicacion.Circulacion;
import aplicacion.FachadaAplicacion;
import aplicacion.Ruta;
import aplicacion.enums.EnumCirculacion;
import gui.modelos.ModeloTablaCirculaciones;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

final class FormularioReservarTren extends JFrame {
    private static final Logger            logger = LoggerFactory.getLogger(FormularioReservarTren.class);
    private static       ResourceBundle    bundle;
    private final        FachadaAplicacion fa;
    private              JTable            tablaTrenesRuta;

    public FormularioReservarTren(@NotNull FachadaAplicacion fa, Ruta rutaEscogida, LocalDateTime fechaSalida) {
        super();

        this.fa = fa;
        bundle = fa.getBundleInstance();


        this.setTitle("Reservar tren");

        this.desplegarVentana(rutaEscogida, fechaSalida);

    }

    private void desplegarVentana(Ruta ruta, LocalDateTime fechaSaida) {
        JPanel panelPrincipal = new JPanel();

        panelPrincipal.setLayout(new BorderLayout());

        this.tablaTrenesRuta = new JTable();
        tablaTrenesRuta.setModel(new ModeloTablaCirculaciones(bundle));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(tablaTrenesRuta);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        this.establecerCirculaciones(ruta, fechaSaida);

        // Panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton btnCancelar   = new JButton(bundle.getString("cancelar"));
        JButton botonReservar = new JButton(bundle.getString("reservar"));

        botonReservar.addActionListener(e -> {
            ModeloTablaCirculaciones modelocircu = (ModeloTablaCirculaciones) this.tablaTrenesRuta.getModel();
            Circulacion circulacion = modelocircu.getCirculacion(tablaTrenesRuta.getSelectedRow());
            if (circulacion != null) {
                try {
                    LoggerFactory.getLogger(FormularioReservarTren.class).info("Reservando tren: {}", circulacion);
                    fa.reservarTren(circulacion);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, bundle.getString("reserva.ya.existe"));
                    return;
                }
                JOptionPane.showMessageDialog(this, bundle.getString("reserva.realizada.con.exito"));
            }
            else {
                JOptionPane.showMessageDialog(this, bundle.getString("seleccione.una.circulacion"));
            }
        });

        btnCancelar.addActionListener(e -> this.dispose());

        tablaTrenesRuta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tablaTrenesRuta.rowAtPoint(evt.getPoint());
                if (row >= 0) {
//                    tablaTrenesRuta.setRowSelectionInterval(row, row); // Se queda para futuras implementaciones
                    ModeloTablaCirculaciones modelocircu         = (ModeloTablaCirculaciones) tablaTrenesRuta.getModel();
                    Circulacion              circulacionEscogida = modelocircu.getCirculacionEnFila(row);
                    if (circulacionEscogida != null) {
                        botonReservar.setEnabled(circulacionEscogida.estado().equals(EnumCirculacion.PROGRAMADO));
                        return;
                    }
                    LoggerFactory.getLogger(FormularioReservarTren.class).warn("Circulación no encontrada en la fila {}", row);
                }
            }
        });

        panelBotones.add(btnCancelar);
        panelBotones.add(botonReservar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        this.add(panelPrincipal);
        this.setSize(600, 400);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);


    }

    private void establecerCirculaciones(Ruta rutaEscogida, @NotNull LocalDateTime fechaSalida) {
        ModeloTablaCirculaciones modelocircu = (ModeloTablaCirculaciones) this.tablaTrenesRuta.getModel();


        List<Circulacion> listaCirculaciones = fa.obtenerCirculacionesRuta(rutaEscogida, fechaSalida.toLocalDate());
//        listaCirculaciones.removeIf(circulacion -> !circulacion.estado().equals(EnumCirculacion.PROGRAMADO));
        modelocircu.setListaCirculaciones(listaCirculaciones);

    }


    public JTable tablaTrenesRuta() {
        return tablaTrenesRuta;
    }
}
