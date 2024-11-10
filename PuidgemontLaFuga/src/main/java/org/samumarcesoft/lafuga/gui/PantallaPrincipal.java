package org.samumarcesoft.lafuga.gui;


import javax.swing.*;
import java.awt.*;


public class PantallaPrincipal extends JFrame {
    private JPanel panel;

    public PantallaPrincipal() {
//        panel = new JPanel();
//        GridLayout layout = new GridLayout(5, 5);
//        this.panel.setLayout(layout);
//
//        JButton button = new JButton("0-0");
//        JButton button1 = new JButton("0-1");
//        JButton button2 = new JButton("0-2");
//        JButton button3 = new JButton("0-3");
//        JButton button4 = new JButton("0-4");
//        JButton button5 = new JButton("1-0");
//        JButton button6 = new JButton("1-1");
//        JButton button7 = new JButton("1-2");
//        JButton button8 = new JButton("1-3");
//        JButton button9 = new JButton("1-4");
//        JButton button10 = new JButton("2-0");
//        JButton button11 = new JButton("2-1");
//        JButton button12 = new JButton("2-2");
//        JButton button13 = new JButton("2-3");
//        JButton button14 = new JButton("2-4");
//        JButton button15 = new JButton("3-0");
//        JButton button16 = new JButton("3-1");
//        JButton button17 = new JButton("3-2");
//        JButton button18 = new JButton("3-3");
//        JButton button19 = new JButton("3-4");
//        JButton button20 = new JButton("4-0");
//        JButton button21 = new JButton("4-1");
//        JButton button22 = new JButton("4-2");
//        JButton button23 = new JButton("4-3");
//        JButton button24 = new JButton("4-4");
//
//
//        this.panel.add(button);
//        this.panel.add(button1);
//        this.panel.add(button2);
//        this.panel.add(button3);
//        this.panel.add(button4);
//        this.panel.add(button5);
//        this.panel.add(button6);
//        this.panel.add(button7);
//        this.panel.add(button8);
//        this.panel.add(button9);
//        this.panel.add(button9);
//        this.panel.add(button10);
//        this.panel.add(button11);
//        this.panel.add(button12);
//        this.panel.add(button13);
//        this.panel.add(button14);
//        this.panel.add(button15);
//        this.panel.add(button16);
//        this.panel.add(button17);
//        this.panel.add(button18);
//        this.panel.add(button19);
//        this.panel.add(button20);
//        this.panel.add(button21);
//        this.panel.add(button22);
//        this.panel.add(button23);
//        this.panel.add(button24);
//
//
//
//
        this.setLayout(new BorderLayout());
//        this.add(panel, BorderLayout.CENTER);
//        JPanel opcionesDelSur = new JPanel();
//        opcionesDelSur.setLayout(new FlowLayout());
//        opcionesDelSur.add(new JButton("Sur"));
//        opcionesDelSur.add(new JButton("Sur"));
//        opcionesDelSur.add(new JButton("Sur"));
//        opcionesDelSur.add(new JButton("Sur"));
//
//        // Hacer más grande el panel de opcionesDelSur
//        opcionesDelSur.setPreferredSize(new Dimension(200, 200));
//
//        this.add(opcionesDelSur, BorderLayout.SOUTH);
//
//        Thing pinguino = new Thing();
//        if(pinguino.setImage("src/main/resources/pingu.jpg"))
//            System.out.println("Imagen cargada");
//        else
//            System.out.println("No se pudo cargar la imagen");
//        JPanel panel = new JPanel();
//        panel.add(new JButton(null,pinguino.getImageInIcon()));
//        this.add(panel, BorderLayout.WEST);

        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(new JButton("Sur"));
        buttonsPanel.add(new JButton("Sur"));
        buttonsPanel.add(new JButton("Sur"));
        buttonsPanel.add(new JButton("Sur"));
        this.add(buttonsPanel, BorderLayout.SOUTH);

        this.pack();

        //this.add(panel);
    }

    public static void main(String[] args) {

        PantallaPrincipal pantallaPrincipal = new PantallaPrincipal();
        pantallaPrincipal.setVisible(true);


    }

}
