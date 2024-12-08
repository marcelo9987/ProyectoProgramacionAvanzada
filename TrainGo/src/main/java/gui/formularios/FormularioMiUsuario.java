package gui.formularios;

import aplicacion.FachadaAplicacion;
import aplicacion.excepciones.UsuarioNoEncontradoException;
import org.intellij.lang.annotations.MagicConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Criptograficos;

import javax.swing.*;
import java.util.ResourceBundle;

final class FormularioMiUsuario extends JDialog {
    private static final int INICIO    = 0;
    private static final int MODIFICAR = 1;
    private static final int GUARDAR   = 2;

    private static final Logger            logger = LoggerFactory.getLogger(FormularioMiUsuario.class);
    private final        FachadaAplicacion fa;
    private final ResourceBundle bundle;

    private JTextField txtCorreo, txtTelefono, txtDireccion;
    private JButton btnGuardar;
    private JButton btnModificar;


    FormularioMiUsuario(JFrame parent, FachadaAplicacion fa) {
        super(parent, true);
        this.fa = fa;
        this.bundle = this.fa.getBundleInstance();
        inicializarFormularioMiUsuario();
    }

    private void inicializarFormularioMiUsuario() {
        JPanel panelPrincipal = new JPanel();


        JLabel lblNombre = new JLabel(fa.getUsrNombre());

//        lblNombre.setPreferredSize(new Dimension(300, 30));
        lblNombre.setMinimumSize(lblNombre.getPreferredSize());

        int i_dni = fa.getUsrDni();
        char c_dni = Criptograficos.calculateDniLetter(i_dni);
        String s_dni = Integer.toString(i_dni) + c_dni;
        JLabel lblDNI = new JLabel(s_dni);
        lblDNI.setMinimumSize(lblDNI.getPreferredSize());

        JLabel lblCorreo = new JLabel(bundle.getString("correo"));

        JLabel lblTelefono = new JLabel(bundle.getString("telefono"));

        JLabel lblDireccion = new JLabel(bundle.getString("direccion"));


        txtCorreo = new JTextField(fa.getUsrCorreo());

        txtTelefono = new JTextField(String.valueOf(fa.getUsrTelefono()));

        txtDireccion = new JTextField(fa.getUsrDireccion());


        JButton btnRegresar = new JButton(bundle.getString("regresar"));

        btnModificar = new JButton(bundle.getString("modificar"));

        btnGuardar = new JButton(bundle.getString("guardar"));

        gestionarModificarYGuardar(INICIO);

        GroupLayout layout = new GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(

                layout.createParallelGroup()
                        .addComponent(lblNombre, GroupLayout.Alignment.CENTER)
                        .addComponent(lblDNI, GroupLayout.Alignment.CENTER)

                        .addGroup(layout.createSequentialGroup()
                                .addComponent(btnModificar, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnGuardar, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnRegresar, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        )

                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblCorreo)
                                        .addComponent(lblTelefono)
                                        .addComponent(lblDireccion)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(txtCorreo, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTelefono, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtDireccion, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                )
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblNombre))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblDNI))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblCorreo)
                                .addComponent(txtCorreo))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblTelefono)
                                .addComponent(txtTelefono))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblDireccion)
                                .addComponent(txtDireccion))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(btnRegresar)
                                .addComponent(btnModificar)
                                .addComponent(btnGuardar))
        );


        this.add(panelPrincipal);
        this.pack();

        btnRegresar.addActionListener(e -> dispose());

        btnModificar.addActionListener(e ->
                gestionarModificarYGuardar(MODIFICAR)
        );

        btnGuardar.addActionListener(e ->
                gestionarModificarYGuardar(GUARDAR)
        );

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle(bundle.getString("mi_usuario"));
        this.setResizable(false);

        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }


    private void gestionarModificarYGuardar(@MagicConstant(intValues = {INICIO, MODIFICAR, GUARDAR}) int accion) {
        if (accion == MODIFICAR) {
            logger.trace("BOTÓN MODIFICAR PULSADO");
            if (!this.btnModificar.isEnabled()) {
                //No se debería poder pulsar el botón si no está habilitado
                logger.trace("BOTÓN MODIFICAR DESHABILITADO, acción cancelada");
                return;
            }
            this.btnModificar.setEnabled(false);
            this.btnGuardar.setEnabled(true);
            this.txtCorreo.setEditable(true);
            this.txtTelefono.setEditable(true);
            this.txtDireccion.setEditable(true);
            logger.trace("CAMPOS EDITABLES");
        }

        if (accion == GUARDAR) {
            logger.trace("BOTÓN GUARDAR PULSADO");
            if (!this.btnGuardar.isEnabled()) {
                // Lo mismo que en modificar, no debería poder pulsarse si no está habilitado
                logger.trace("BOTÓN GUARDAR DESHABILITADO, acción cancelada");
                return;
            }
            this.btnGuardar.setEnabled(false);
            this.btnModificar.setEnabled(true);
            this.txtCorreo.setEditable(false);
            this.txtTelefono.setEditable(false);
            this.txtDireccion.setEditable(false);
            logger.trace("CAMPOS DESHABILITADOS");
            try {
                this.fa.actualizarUsuario(fa.getUsrCorreo(), txtCorreo.getText(), txtDireccion.getText(), Integer.parseInt(txtTelefono.getText()));
            } catch (UsuarioNoEncontradoException e) {
                logger.error("Error al actualizar el usuario: El usuario no se ha encontrado", e);
            }
            actualizarComponentesObsoletos();
        }
        if (accion == INICIO) {
            this.btnGuardar.setEnabled(false);
            this.txtCorreo.setEditable(false);
            this.txtTelefono.setEditable(false);
            this.txtDireccion.setEditable(false);
        }
    }

    private void actualizarComponentesObsoletos() {
        this.txtCorreo.setText(fa.getUsrCorreo());
        this.txtTelefono.setText(Integer.toString(fa.getUsrTelefono()));
        this.txtDireccion.setText(fa.getUsrDireccion());
    }

}
