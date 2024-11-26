package gui.formularios;

import gui.modelos.ModeloDesplegableUbicacion;

import javax.swing.*;

public class FormularioPrincipal extends JFrame {


    public FormularioPrincipal() {
        lanzarFormulario();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormularioPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FormularioPrincipal().setVisible(true));
    }

    private void lanzarFormulario() {

        JPanel jPanel1 = new JPanel();
        JLabel jLabel1 = new JLabel();
        JPanel jPanel2 = new JPanel();
        JComboBox<String> jComboBox1 = new JComboBox<>();
        JComboBox<String> jComboBox2 = new JComboBox<>();
        JFormattedTextField jFormattedTextField1 = new JFormattedTextField();
        JPanel jPanel3 = new JPanel();
        JButton jButton5 = new JButton();
        JButton jButton6 = new JButton();
        JMenuBar jMenuBar1 = new JMenuBar();
        JMenu jMenu1 = new JMenu();
        JMenu jMenu2 = new JMenu();
        JMenu jMenu3 = new JMenu();
        JMenu jMenu4 = new JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);


        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("jLabel1");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setMaximumSize(null);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.GridLayout(2, 2));

        ModeloDesplegableUbicacion modeloOrigen = new ModeloDesplegableUbicacion();

        modeloOrigen.addUbicacion("Seleccione una ubicación");
        modeloOrigen.addUbicacion("Madrid");
        modeloOrigen.addUbicacion("Barcelona");
        modeloOrigen.addUbicacion("Sevilla");
        modeloOrigen.addUbicacion("A Coruña");
        modeloOrigen.setSelectedItem("Seleccione una ubicación");

        jComboBox1.setModel(modeloOrigen);

        jPanel2.add(jComboBox1);

        ModeloDesplegableUbicacion modeloDestino = modeloOrigen.clone();

        modeloDestino.setSelectedItem("Seleccione una ubicación");

        jComboBox2.setModel(modeloDestino);
        jPanel2.add(jComboBox2);

        jFormattedTextField1.setText("jFormattedTextField1");
        jPanel2.add(jFormattedTextField1);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jButton5.setText("jButton5");

        jButton6.setText("jButton6");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(279, 279, 279)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(15, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGap(25, 25, 25)
                                                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jMenu1.setText("Ajustes");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Mis Viajes");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("COMPRAR");
        jMenuBar1.add(jMenu3);

        jMenu4.setText("Mi usuario");
        jMenu4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jMenu4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();

        // Lo posiciono en el centro de la pantalla
        setLocationRelativeTo(null);
    }


}
