package org.samumarcesoft.lafuga;

public interface Drawable {
    /**
     * Draw the image on the screen
     */
    void draw();

    /**
     * Set the image to be drawn
     * @param address the address of the image
     * @return true if the image was set successfully, false otherwise
     */
    Boolean setImage(String address);
}
