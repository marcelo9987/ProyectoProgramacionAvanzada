package gui.formularios;

import aplicacion.FachadaAplicacion;
import aplicacion.enums.EnumIdioma;

import javax.swing.*;
import java.util.Objects;
import java.util.ResourceBundle;

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

        btnAceptar.addActionListener(e -> {
            String idiomaSeleccionado = Objects.requireNonNull(comboIdioma.getSelectedItem()).toString().equals("Español") ? "ESPANHOL" : comboIdioma.getSelectedItem().toString().toUpperCase().replace('É', 'E');
            fa.cambiarIdioma(EnumIdioma.valueOf(idiomaSeleccionado));
            fa.relanzarGUI();
            FormularioConfiguracion.this.dispose();
        });

        btnCancelar.addActionListener(e -> FormularioConfiguracion.this.dispose());

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

}
