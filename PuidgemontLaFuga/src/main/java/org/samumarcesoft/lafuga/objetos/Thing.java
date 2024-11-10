package org.samumarcesoft.lafuga.objetos;

import org.samumarcesoft.lafuga.Drawable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Thing implements Drawable {

    private String imageAddress;

    private Image imagen;

    @Override
    public void draw() {
        if(imagen == null) {
            System.out.println("No image to draw");
            return;
        }
    }

    public Boolean setImage(String imageAddress) {
        if (!new File(imageAddress).isFile()) {
            return false;
        }
        try {
            this.imagen = ImageIO.read(new File(imageAddress));
            //todo: ¿Cámbiame?
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        this.imageAddress = imageAddress;

        return true;
    }

    public Icon getImageInIcon() {
        return new ImageIcon(imagen);
    }
}
