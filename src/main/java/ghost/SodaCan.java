package ghost;

import processing.core.PImage;

/**
 * SodaCan is a public class that extends from Fruit.
 * Represents all the sprite that is unmoveable or stationary when is 
 * drawn on the screen.
 * It can be collected by Player.
 * Once the Player has eaten it, Ghost will be invisible for a period.
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public class SodaCan extends Fruit{

    /**
     * Creates a SodaCan object with the given x and y coordinate and PImage.
     * The isSoda flag is initialise true within the constructor.
     * @param x         x coordinate of this SodaCan, measures in pixel.
     * @param y         y coordinate of this SodaCan, measures in pixel.
     * @param sprite    PImage of this SodaCan.
     */
    public SodaCan(int x, int y, PImage sprite){
        super(x, y, sprite);
        this.isSoda = true;
    }
   
}
