package org.samumarcesoft.lafuga.gui;

import org.samumarcesoft.lafuga.Drawable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class Tile extends JButton {
    private byte row;
    private byte column;
    Drawable drawable;

    //TODO: Implementar la clase Tile
    Tile(int row, int column) {
        this.row = (byte) row;
        this.column = (byte) column;
        setText(row + "-" + column);
//        throw new UnsupportedOperationException("Not implemented");
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Tile clicked is: R" + row + " - C" + column);
            }
        });
    }
    Tile(Drawable drawable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void setDrawable(Drawable drawable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Drawable getDrawable() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void draw() {
        throw new UnsupportedOperationException("Not implemented");
    }


}
