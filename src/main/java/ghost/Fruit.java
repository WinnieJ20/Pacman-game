package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Fruit is a class that extends from Stationary class and 
 * implements the Eatable and Resetable interface.
 * Represents all the sprite that can be eaten.
 * Fruit can be passed through by Ghost or Player.
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */

public class Fruit extends Stationary implements Eatable, Resetable{
    /**
     * The eaten status of this Fruit.
     */
    protected boolean isEaten;

    /**
     * This Fruit is a super fruit or not.
     */
    protected boolean isSuperFruit;

    /**
     * This Fruit is a SodaCan or not.
     */
    protected boolean isSoda;

    /**
     * The used status of this Fruit.
     */
    protected boolean used;

    /**
     * Creates a Fruit object with the given x and y coordinate and PImage
     * @param x         x coordinate of this Fruit, measures in pixel.
     * @param y         y coordinate of this Fruit, measures in pixel.
     * @param sprite    PImage of this Fruit.
     */
    public Fruit(int x, int y, PImage sprite){
        super(x, y, sprite);
    }

    /**
     * Gets the eaten status of this Fruit.
     * @return      <code>true</code> if this Fruit is eaten
     *              <code>false</code> otherwise.
     */
    public boolean isEaten(){
        return this.isEaten;
    }

    /**
     * Gets the super fruit flag of this Fruit.
     * @return      <code>true</code> if this Fruit is super fruit
     *              <code>false</code> otherwise.
     */
    public boolean isSuperFruit(){
        return this.isSuperFruit;
    }

    /**
     * Gets the isSoda flag of this Fruit.
     * @return      <code>true</code> if this Fruit is SodaCan
     *              <code>false</code> otherwise.
     */
    public boolean isSoda(){
        return this.isSoda;
    }

    /**
     * Gets the used status of this Fruit.
     * @return      <code>true</code> if this Fruit is used
     *              <code>false</code> otherwise.
     */
    public boolean isUsed(){
        return this.used;
    }

    /**
     * Sets the used flag to be true.
     */
    public void setUsed(){
        this.used = true;
    }

    /**
     * Draws this Fruit on the screen.
     * Only draw this Fruit on the screen if 
     * it is not eaten.
     * @param app           instance of an PApplet which is for drawing PImage.
     */
    public void draw(PApplet app){
        if (app == null){
            return;
        }
        if (! this.isEaten){
            app.image(this.sprite, this.x, this.y);
        }
    }

    /**
     * Sets this Fruit's isEaten flag be true if 
     * Image player is in the same position with this Fruit.
     * @param player       instance that eats this Fruit.
     */
    public void eaten(Image player){
        if (this.checkCollideCenter(player) && ! this.isEaten){
            this.isEaten = true;
        }
    }

    /**
     * Sets every attribute and field back to the inital 
     * stage.
     * isEaten flag is set to false.
     * used flag is set to false.
     */
    public void restart(){
        this.isEaten = false;
        this.used = false;
    }
}
