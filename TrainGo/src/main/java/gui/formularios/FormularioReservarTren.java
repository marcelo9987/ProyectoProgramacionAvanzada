package gui.formularios;

import aplicacion.Circulacion;
import aplicacion.Estacion;
import aplicacion.FachadaAplicacion;
import aplicacion.Ruta;
import dao.FachadaDAO;
import gui.modelos.ModeloTablaCirculaciones;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class FormularioReservarTren extends JFrame {
    private static final Logger         logger = LoggerFactory.getLogger(FormularioReservarTren.class);
    private static       ResourceBundle bundle;
    private final        Ruta           rutaEscogida;
    private final        LocalDateTime  fechaSalida;
    FachadaAplicacion fa;
    private JPanel panelPrincipal;
    private JTable tablaTrenesRuta;
    private JButton btnCancelar;
    private JButton botonReservar;
    public FormularioReservarTren(@NotNull FachadaAplicacion fa, Ruta rutaEscogida, LocalDateTime fechaSalida) {
        super();

        this.fa = fa;
        bundle = fa.getBundleInstance();

        this.rutaEscogida = rutaEscogida;
        this.fechaSalida = fechaSalida;


        this.setTitle("Reservar tren");

        this.desplegarVentana(rutaEscogida, fechaSalida);

    }

    private void desplegarVentana(Ruta ruta, LocalDateTime fechaSaida) {
        this.panelPrincipal = new JPanel();

        this.panelPrincipal.setLayout(new BorderLayout());

        this.tablaTrenesRuta = new JTable();
        tablaTrenesRuta.setModel(new ModeloTablaCirculaciones());

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(tablaTrenesRuta);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        this.establecerCirculaciones(ruta, fechaSaida);

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

    private void establecerCirculaciones(Ruta rutaEscogida, LocalDateTime fechaSalida) {
        FachadaDAO               fd          = FachadaDAO.getInstance();
        ModeloTablaCirculaciones modelocircu = (ModeloTablaCirculaciones) this.tablaTrenesRuta.getModel();

        modelocircu.setListaCirculaciones(fd.obtenerCirculacionesRutaEnFecha(rutaEscogida, fechaSalida.toLocalDate()));
    }

    public static void main(String[] args) {
        FachadaAplicacion fa = new FachadaAplicacion();
        FachadaDAO        fd = FachadaDAO.getInstance();
        fd.cargaloTodo();
        FormularioReservarTren   frt         = new FormularioReservarTren(fa, new Ruta(new Estacion("Vigo"), new Estacion("A Coruña"), 444), LocalDateTime.now());
        ModeloTablaCirculaciones modelocircu = (ModeloTablaCirculaciones) frt.tablaTrenesRuta().getModel();

        modelocircu.setListaCirculaciones(fd.__dbg_obtenerTodasLasCirculaciones());

        for (Circulacion circulacion : fd.__dbg_obtenerTodasLasCirculaciones()) {
            logger.info(circulacion.toString());
        }
    }

    public JTable tablaTrenesRuta() {
        return tablaTrenesRuta;
    }
}
