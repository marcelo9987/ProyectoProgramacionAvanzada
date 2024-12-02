package gui.formularios;

import gui.modelos.ModeloTablaCirculaciones;
import modelo.*;
import modelo.Enums.EnumCirculacion;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.UUID;

public class FormularioReservarTren extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(FormularioReservarTren.class);
    private static ResourceBundle bundle;

    FachadaAplicacion fa;

    private JPanel panelPrincipal;
    private JTable tablaTrenesRuta;
    private JButton btnCancelar;
    private JButton botonReservar;

    private final Ruta rutaEscogida;
    private final LocalDateTime fechaSalida;

    public FormularioReservarTren(@NotNull FachadaAplicacion fa, Ruta rutaEscogida, LocalDateTime fechaSalida) {
        super();

        this.fa = fa;
        bundle = fa.getBundleInstance();

        this.rutaEscogida = rutaEscogida;
        this.fechaSalida = fechaSalida;

        this.setTitle("Reservar tren");

        this.desplegarVentana();
    }

    public static void main(String[] args) {
        FachadaAplicacion fa = new FachadaAplicacion();
        FormularioReservarTren frt = new FormularioReservarTren(fa, new Ruta(new Estacion("Madrid"), new Estacion("Barcelona"), 444), LocalDateTime.now());
        ModeloTablaCirculaciones modelocircu = (ModeloTablaCirculaciones) frt.tablaTrenesRuta.getModel();
        modelocircu.addCirculacion(
                new Circulacion
                        (
                                UUID.randomUUID() //UUID
                                , new Tren
                                (
                                        UUID.randomUUID()
                                        , 22
                                )
                                , new Ruta
                                (
                                        new Estacion
                                                (
                                                        "Madrid"
                                                )
                                        , new Estacion
                                        (
                                                "Barcelona"
                                        )
                                        , 444
                                )
                                , EnumCirculacion.PROGRAMADO
                                , LocalDateTime.parse("2025-06-01T08:00:00")
                                , BigDecimal.valueOf(58.22)
                        )
        );
    }

    private void desplegarVentana() {
        this.panelPrincipal = new JPanel();

        this.panelPrincipal.setLayout(new BorderLayout());

        this.tablaTrenesRuta = new JTable();
        tablaTrenesRuta.setModel(new ModeloTablaCirculaciones());

        JScrollPane scrollPane = new JScrollPane(tablaTrenesRuta);

        this.panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.btnCancelar = new JButton(bundle.getString("cancelar"));
        this.botonReservar = new JButton(bundle.getString("reservar"));

        panelBotones.add(btnCancelar);
        panelBotones.add(botonReservar);

        this.panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        this.add(panelPrincipal);
        this.setSize(600, 400);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);


    }

}
