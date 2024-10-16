package org.samumarcesoft.lafuga.objetos;

import org.samumarcesoft.lafuga.Drawable;
import org.samumarcesoft.lafuga.enums.Direccion;

public abstract class Character implements Drawable {

    /**
     * Puntos de vida de un personaje
     */
    private int healthPoints;

    /**
     * Mueve el personaje a x dirección
     * @param dir A donde se va a mover el personaje
     */
    public abstract void move(Direccion dir);

    /**
     * Ataca a otro personaje
     * @param other Personaje a atacar
     */
    public abstract void attack(Character other);

    /**
     * Entabla conversación con otro personaje
     * @param other
     */
    public abstract void chat(Character other);


}
