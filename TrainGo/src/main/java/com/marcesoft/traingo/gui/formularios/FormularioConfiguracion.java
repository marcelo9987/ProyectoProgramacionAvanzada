package com.marcesoft.traingo.gui.formularios;

import com.marcesoft.traingo.aplicacion.FachadaAplicacion;
import com.marcesoft.traingo.aplicacion.enums.EnumIdioma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Formulario de configuración de la aplicación que permite cambiar el idioma de la misma.
 */
final class FormularioConfiguracion extends JDialog {
    private final FachadaAplicacion fa;

    private FormularioConfiguracion(FachadaAplicacion fa) {
        super();
        this.fa = fa;
        inicializarFormularioConfiguracion();
    }

    private void inicializarFormularioConfiguracion() {
        ResourceBundle bundle = fa.getBundleInstance();

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel idioma = new JLabel(bundle.getString("idioma"));
        JComboBox<String> comboIdioma = new JComboBox<>();
        comboIdioma.addItem("Español");
        comboIdioma.addItem("Inglés");
        comboIdioma.addItem("Galego");

        JButton btnAceptar = new JButton(bundle.getString("aceptar"));
        JButton btnCancelar = new JButton(bundle.getString("cancelar"));

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(idioma))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(comboIdioma)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnAceptar)
                                        .addComponent(btnCancelar)))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(idioma)
                                .addComponent(comboIdioma))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(btnAceptar)
                                .addComponent(btnCancelar))
        );


        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle(bundle.getString("configuracion"));
        this.setResizable(false);
        this.add(panel);

        btnAceptar.addActionListener(new btnAceptarPulsadoActionListener(comboIdioma));

        btnCancelar.addActionListener(_ -> FormularioConfiguracion.this.dispose());

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FormularioConfiguracion(new FachadaAplicacion()).setVisible(true));
    }

    FormularioConfiguracion(FachadaAplicacion fa, JFrame parent) {
        super(parent, true);
        this.fa = fa;
        inicializarFormularioConfiguracion();
    }

    private class btnAceptarPulsadoActionListener implements ActionListener {
        private final JComboBox<String> comboIdioma;

        btnAceptarPulsadoActionListener(JComboBox<String> comboIdioma) {
            super();
            this.comboIdioma = comboIdioma;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String idiomaSeleccionado = Objects.requireNonNull(comboIdioma.getSelectedItem()).toString().equals("Español") ? "ESPANHOL" : comboIdioma.getSelectedItem().toString().toUpperCase().replace('É', 'E');
            fa.cambiarIdioma(EnumIdioma.valueOf(idiomaSeleccionado));
            fa.relanzarGUI();
            FormularioConfiguracion.this.dispose();
        }
    }
}
