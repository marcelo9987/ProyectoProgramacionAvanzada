package org.samumarcesoft.lafuga.gui;

import javax.swing.JPanel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel {
    Map<Integer,Tile> tiles;

    /**
     * Constructor de la clase
     */
    public GamePanel()
    {
        this.tiles = new HashMap<>();
        for(int cell = 0; cell<5;cell++)
        {
            for(int row = 0; row<5;row++)
            {
                Tile tile = new Tile();
                tile.setText(cell+"-"+row);
                tiles.put(cell*5+row,tile);
            }
        }
        this.setLayout(new GridLayout(5,5));
        for(int cell = 0; cell<5;cell++)
        {
            for(int row = 0; row<5;row++)
            {
                this.add(tiles.get(cell*5+row));
            }
        }

        this.setVisible(true);

    }

}
