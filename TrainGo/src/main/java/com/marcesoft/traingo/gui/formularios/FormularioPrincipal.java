package com.marcesoft.traingo.gui.formularios;

import com.marcesoft.traingo.aplicacion.FachadaAplicacion;
import com.marcesoft.traingo.aplicacion.Ruta;
import com.marcesoft.traingo.aplicacion.excepciones.SituacionDeRutasInesperadaException;
import com.marcesoft.traingo.aplicacion.formatos.FormatedFecha;
import com.marcesoft.traingo.gui.modelos.ModeloDesplegableUbicacion;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Formulario (ventana) principal de la aplicación
 */
public final class FormularioPrincipal extends JFrame {
    private final ResourceBundle    bundle;
    private final FachadaAplicacion fa;


    /**
     * Constructor del formulario principal
     *
     * @param fa Fachada de la aplicación
     */
    public FormularioPrincipal(@NotNull FachadaAplicacion fa) {
        super();
        this.fa = fa;
        this.bundle = fa.getBundleInstance();
        lanzarFormulario();
    }


    private void lanzarFormulario() {

        JPanel              jPanel1                = new JPanel();
        JLabel              jLabel1                = new JLabel();
        JPanel              panelOpciones          = new JPanel();
        JComboBox<String>   comboOrigen            = new JComboBox<>();
        JComboBox<String>   comboDestino           = new JComboBox<>();
        JLabel              formatoFechaIncorrecto = new JLabel();
        JFormattedTextField txtformateado_fecha    = null;
        try {
            txtformateado_fecha = new JFormattedTextField(new FormatedFecha(formatoFechaIncorrecto));
        } catch (ParseException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error en el formato de la fecha");
        }
        JPanel   jPanel3           = new JPanel();
        JButton  btn_buscar        = new JButton(), btnSalir = new JButton();
        JMenuBar menu              = new JMenuBar();
        JMenu    menuConfiguracion = new JMenu(), menuViajes = new JMenu(), menuMiUsuario = new JMenu();

        BufferedImage myPicture = null;

        try {
            myPicture = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("images/tren.png")));
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen");
        }


        setDefaultCloseOperation(EXIT_ON_CLOSE);


        if (myPicture == null) {
            jLabel1.setText("Error al cargar la imagen");
        }
        else {
            jPanel1.add(new JLabel(new ImageIcon(myPicture)));
        }

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        panelOpciones.setLayout(new java.awt.GridLayout(2, 2));

        ModeloDesplegableUbicacion modeloOrigen = new ModeloDesplegableUbicacion(this.fa);

        modeloOrigen.addUbicacion(fa.getBundleInstance().getString("destino_sel1"));
        modeloOrigen.setSelectedItem(fa.getBundleInstance().getString("destino_sel1"));


        comboOrigen.setModel(modeloOrigen);

        panelOpciones.add(comboOrigen);

        ModeloDesplegableUbicacion modeloDestino = modeloOrigen.clone();

        modeloDestino.setSelectedItem(fa.getBundleInstance().getString("destino_sel1"));

        comboDestino.setModel(modeloDestino);
        panelOpciones.add(comboDestino);

        panelOpciones.add(txtformateado_fecha);

        panelOpciones.add(formatoFechaIncorrecto);

        getContentPane().add(panelOpciones, java.awt.BorderLayout.CENTER);

        btn_buscar.setText(bundle.getString("buscar"));

        btnSalir.setText(bundle.getString("salir"));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(279, 279, 279)
                                .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
                                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(15, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGap(25, 25, 25)
                                                .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_END);

        menuConfiguracion.setText(fa.getBundleInstance().getString("configuracion"));
        menu.add(menuConfiguracion);

        menuViajes.setText(bundle.getString("mis_viajes"));
        menu.add(menuViajes);


        menuMiUsuario.setText(bundle.getString("mi_usuario"));
        menuMiUsuario.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        menuMiUsuario.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menu.add(menuMiUsuario);

        setJMenuBar(menu);

        pack();

        JFormattedTextField txtformateado_aux = txtformateado_fecha;
        btn_buscar.addActionListener(_ -> _procesarCampoFechaViaje(comboOrigen, comboDestino, txtformateado_aux));

        // Evento en clic para salir
        btnSalir.addActionListener(_ -> System.exit(0));

        JFrame thisFrame = this;

        // Evento en clic para el menú de configuración
        menuConfiguracion.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                FormularioConfiguracion fc = new FormularioConfiguracion(fa, thisFrame, true);
                java.awt.EventQueue.invokeLater(() -> new FormularioConfiguracion(fa, thisFrame));
            }
        });


        menuMiUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                java.awt.EventQueue.invokeLater(() -> new FormularioMiUsuario(thisFrame, fa));
            }
        });

        menuViajes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                FormularioMisReservas fm = new FormularioMisReservas(fa);
                java.awt.EventQueue.invokeLater(() -> new FormularioMisReservas(fa));

            }
        });

        // Lo posiciono en el centro de la pantalla
        setLocationRelativeTo(null);

        this.setVisible(true);
    }

    private void _procesarCampoFechaViaje(@NotNull JComboBox<String> comboOrigen, @NotNull JComboBox<String> comboDestino, JFormattedTextField txtformateado_aux) {
        String origen  = (String) comboOrigen.getSelectedItem();
        String destino = (String) comboDestino.getSelectedItem();
        if (txtformateado_aux == null) {
            // Si no se ha podido cargar el campo de fecha, no se puede continuar
            JOptionPane.showMessageDialog(FormularioPrincipal.this, bundle.getString("fecha.invalida"));
            return;
        }
        String fecha = txtformateado_aux.getText();
        Ruta   ruta;
        try {
            ruta = fa.buscarRutaPorNombres(origen, destino);
        } catch (SituacionDeRutasInesperadaException e) {
            JOptionPane.showMessageDialog(FormularioPrincipal.this, bundle.getString("ruta.no.encontrada"));
            return;
        }
        java.awt.EventQueue.invokeLater(() -> new FormularioReservarTren(fa, ruta, LocalDateTime.of(Integer.parseInt(fecha.substring(6, 10)), Integer.parseInt(fecha.substring(3, 5)), Integer.parseInt(fecha.substring(0, 2)), 0, 0)));
    }

    private FormularioPrincipal() {
        super();

        this.fa = new FachadaAplicacion();

        this.bundle = fa.getBundleInstance();
        lanzarFormulario();
    }

    /**
     * Lanza el formulario principal
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try { // Pruebas de estilo
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("IBM".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormularioPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, "Ha habido un fallo estableciendo la estética del prograama", ex);
        }


        // Lanzo el formulario
        java.awt.EventQueue.invokeLater(() -> new FormularioPrincipal().setVisible(true));
    }


}
