package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Image is the abstract based class for all graphical image drawn on the screen.
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public abstract class Image {
    /**
     * x coordinate of this Image, measures in pixel.
     */
    protected int x;

    /**
     * y coordinate of this Image, measures in pixel.
     */
    protected int y;

    /**
     * PImage of this Image.
     */
    protected PImage sprite;


    /**
     * 
     * @param x x coordinate of this Image, measures in pixel.
     * @param y y coordinate of this Image, measures in pixel.
     * @param sprite PImage of this Image.
     */
    public Image(int x, int y, PImage sprite){
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    /**
     * Gets the x coordinate of this Image.
     * @return this Image's x coordinate.
     */
    public int getX(){
        return this.x;
    }
    
    /**
     * Gets the y coordinate of this Image.
     * @return this Image's y coordinate.
     */
    public int getY(){
        return this.y;
    }

    /**
     * Gets the width of this Image.
     * @return      this Image's width.
     */
    public int getWidth(){
        return this.sprite.width;
    }

    /**
     * Gets the height of this Image.
     * @return      this Image's height.
     */
    public int getHeight(){
        return this.sprite.height;
    }

    /**
     * Draws this Image on the screen.
     * @param app     PApplet instance
     */
    public abstract void draw(PApplet app);

    /**
     * Checks whether this Image is collided with the given Image sprite.
     * This is checked by determining whether the center of this Image is 
     * within the given sprite boundary. 
     * This method returns true is this Image collide with the given Image sprite.
     * @param sprite    this Image
     * @return          <code>true</code> if the center of this Image is within
     *                  the given sprite boundary; 
     *                  <code>false</code> otherwise*/
    public boolean checkCollideCenter(Image sprite){
        if (sprite == null){
            return false;
        }
        int centerX = this.x + this.getWidth()/2;
        int centerY = this.y + this.getHeight()/2;
        int spriteRight = sprite.getX() + sprite.getWidth();
        int spriteBottom = sprite.getY() + sprite.getHeight();

        if (centerX > sprite.getX() && centerX < spriteRight && centerY > sprite.getY() && centerY < spriteBottom){
            return true;
        }
        return false;
    }
}
