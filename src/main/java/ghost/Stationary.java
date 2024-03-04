package ghost;
import processing.core.PImage;

/**
 * Stationary is the abstract class that extends from Image class.
 * Represents all the sprite that is unmoveable or stationary when is 
 * drawn on the screen.
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public abstract class Stationary extends Image{

    /**
     * 
     * @param x     x coordinate of this Stationary, measures in pixel.
     * @param y     x coordinate of this Stationary, measures in pixel.
     * @param sprite PImage of this Stationary.
     */
    public Stationary(int x, int y, PImage sprite){
        super(x, y, sprite);
    }
}
