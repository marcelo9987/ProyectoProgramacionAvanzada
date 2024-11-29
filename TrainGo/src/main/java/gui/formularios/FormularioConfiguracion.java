package gui.formularios;

import modelo.Enums.EnumIdioma;
import modelo.FachadaAplicacion;

import javax.swing.*;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class FormularioConfiguracion extends javax.swing.JDialog {
    private FachadaAplicacion fa;

    public FormularioConfiguracion(FachadaAplicacion fa) {
        this.fa = fa;
        inicializarFormularioConfiguracion();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FormularioConfiguracion(new FachadaAplicacion()).setVisible(true));
    }

    private void inicializarFormularioConfiguracion() {
        ResourceBundle bundle = ResourceBundle.getBundle("gui", Locale.getDefault());

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

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

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


        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Configuración");
        this.setResizable(false);
        this.add(panel);

        btnAceptar.addActionListener(e -> {
            String idiomaSeleccionado = Objects.requireNonNull(comboIdioma.getSelectedItem()).toString().equals("Español") ? "ESPANHOL" : comboIdioma.getSelectedItem().toString().toLowerCase();
            fa.cambiarIdioma(EnumIdioma.valueOf(comboIdioma.getSelectedItem().toString().toUpperCase()));
            fa.relanzarGUI();
            this.dispose();
        });


        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

}
