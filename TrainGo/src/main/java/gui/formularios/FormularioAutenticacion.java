package gui.formularios;

import modelo.FachadaAplicacion;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FormularioAutenticacion extends JDialog {
    private final JLabel lblUsuario;
    private final JLabel lblContrasena;
    private final JTextField txtUsuario;
    private final JPasswordField txtContrasena;
    private final JButton btnAceptar;
    private final JButton btnCancelar;

    private FachadaAplicacion fa;

    public FormularioAutenticacion(FachadaAplicacion fa, Frame parent, boolean modal) {
        super(parent, modal);

        this.lblUsuario = new JLabel("Usuario:");
        this.lblContrasena = new JLabel("Contraseña:");
        this.txtUsuario = new JTextField(20);
        this.txtContrasena = new JPasswordField(20);
        this.btnAceptar = new JButton("Aceptar");
        this.btnCancelar = new JButton("Cancelar");

        this.fa = fa;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Autenticación");
        setResizable(false);

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

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

        btnCancelar.addActionListener(e -> {
            System.exit(0);
        });

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

        btnAceptar.addActionListener(e ->
        {
            comprobarAutenticacion();
        });

        this.getContentPane().add(panel);
        this.pack();
        this.setLocationRelativeTo(null);


    }

    private boolean comprobarTeclaPulsada(KeyEvent e) {
        return e.getKeyCode() == '\r' || e.getKeyCode() == '\n';
    }

    private void comprobarAutenticacion() {
        Usuario usuario = null;
        if (fa.autenticar(usuario, txtUsuario.getText(), txtContrasena.getText())) {
            JOptionPane.showMessageDialog(this, "Autenticación correcta");
            this.dispose();
        }
        else {
            JOptionPane.showMessageDialog(this, "Autenticación incorrecta");
        }
    }
}