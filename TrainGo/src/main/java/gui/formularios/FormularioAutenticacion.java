package gui.formularios;

import modelo.FachadaAplicacion;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public final class FormularioAutenticacion extends JDialog {
    private final JTextField txtUsuario;
    private final JPasswordField txtContrasena;

    private final FachadaAplicacion fa;

    public FormularioAutenticacion(FachadaAplicacion fa, Frame parent, boolean modal) {
        super(parent, modal);

        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblContrasena = new JLabel("Contraseña:");
        this.txtUsuario = new JTextField(20);
        this.txtContrasena = new JPasswordField(20);
        JButton btnAceptar = new JButton("aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        this.fa = fa;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Autenticación");
        setResizable(false);

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        /*
         * Nivel 1: lblUsuario, lblContrasena
         * Nivel 2: txtUsuario, txtContrasena
         * Nivel 3: btnAceptar, btnCancelar
         */
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lblUsuario)
                                .addComponent(lblContrasena))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(txtUsuario)
                                .addComponent(txtContrasena)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnAceptar)
                                        .addComponent(btnCancelar)))
        );

        /*
         * lblUsuario, txtUsuario | lblContrasena, txtContrasena | btnAceptar, btnCancelar
         */
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblUsuario)
                                .addComponent(txtUsuario))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblContrasena)
                                .addComponent(txtContrasena))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(btnAceptar)
                                .addComponent(btnCancelar))
        );


        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        txtUsuario.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (comprobarTeclaPulsada(e)) {
                            comprobarAutenticacion();
                        }
                    }
                }
        );

        btnCancelar.addActionListener(_ -> System.exit(0));

        txtContrasena.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (comprobarTeclaPulsada(e)) {
                            comprobarAutenticacion();
                        }
                    }
                }
        );

        btnAceptar.addActionListener(_ -> comprobarAutenticacion());

        this.getContentPane().add(panel);
        this.pack();
        this.setLocationRelativeTo(null);


    }

    /**
     * Comprueba si la tecla pulsada es la tecla de intro
     *
     * @param e Evento de teclado
     * @return True si la tecla pulsada es la tecla de intro, false en caso contrario
     */
    private boolean comprobarTeclaPulsada(@NotNull KeyEvent e) {
        return e.getKeyCode() == '\r' || e.getKeyCode() == '\n';
    }

    /**
     * Comprueba si la autenticación es correcta
     *
     * @see FachadaAplicacion#autenticar(String, String)
     */
    private void comprobarAutenticacion() {
        if (fa.autenticar(txtUsuario.getText(), Arrays.toString(txtContrasena.getPassword()).replaceAll("[\\[\\], ]", ""))) {
            JOptionPane.showMessageDialog(this, fa.getBundleInstance().getString("autenticacion.correcta"));
            this.dispose();
        }
        else {
            JOptionPane.showMessageDialog(this, "Autenticación incorrecta");
        }
    }
}