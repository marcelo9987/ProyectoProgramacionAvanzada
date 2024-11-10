package org.samumarcesoft.lafuga.gui;

import javax.swing.JPanel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel {

    public static final int KFILAS = 5;
    public static final int KCOLUMNAS = 5;

    Map<Integer,Tile> tiles;

    /**
     * Constructor de la clase
     */
    public GamePanel()
    {
        this.tiles = new HashMap<>();
        for(int row = 0; row< KFILAS;row++)
        {
            for(int column = 0; column< KCOLUMNAS;column++)
            {
                Tile tile = new Tile(row,column);
                tile.setText(row+"-"+column);
                tiles.put(row*KFILAS+column,tile);
            }
        }
        this.setLayout(new GridLayout(KFILAS,KCOLUMNAS));
        for(int row = 0; row< KFILAS;row++)
        {
            for(int column = 0; column< KCOLUMNAS;column++)
            {
                this.add(tiles.get(row*KFILAS+column));
            }
        }

        this.setVisible(true);

    }

}
