package ghost;

import processing.core.PImage;

/**
 * SuperFruit is a class that extends from Fruit class.
 * Represents all the sprite that is super fruit.
 * SuperFruit can be passed through by Ghost or Player.
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public class SuperFruit extends Fruit{

    /**
     * Creates a SuperFruit instance with the flag isSuperFruit
     * being true.
     * @param x         x coordinate of this Fruit, measures in pixel.
     * @param y         y coordinate of this Fruit, measures in pixel.
     * @param sprite    PImage of this Fruit.
     */
    public SuperFruit(int x, int y, PImage sprite){
        super(x, y, sprite);
        this.isSuperFruit = true;
    }
}
