package org.samumarcesoft.lafuga.objetos;

import jdk.jshell.spi.ExecutionControl;
import org.samumarcesoft.lafuga.Drawable;
import org.samumarcesoft.lafuga.enums.Direccion;

import java.io.File;

public class Thing implements Drawable {

    private String imageAddress;


    public Boolean setImage(String imageAddress)
    {
        if(!new File(imageAddress).isFile())
        {
            return false;
        }
        this.imageAddress = imageAddress;

        return true;
    }

}
