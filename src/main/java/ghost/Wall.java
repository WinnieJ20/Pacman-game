package ghost;

import processing.core.PApplet;
import processing.core.PImage;
/**
 * Wall is a class that extends from Stationary class.
 * Represents all the sprite that cannot be eaten or moved.
 * Wall cannot be gone through.
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public class Wall extends Stationary{

    /**
     * Creates a Wall object with the given x and y coordination and PImage sprite.
     * @param x         x coordinate of this Wall, measures in pixel.
     * @param y         y coordinate of this Wall, measures in pixel.
     * @param sprite    PImage of this Wall.
     */
    public Wall(int x, int y, PImage sprite){
        super(x, y, sprite);
    }

    /**
     * Draws this Wall on the screen.
     * @param app       instance of PApplet which is drawing
     *                  this Wall on the screen.
     */
    public void draw(PApplet app){
        app.image(this.sprite, this.x, this.y);
    }

    /** Checks if a Moveable sprite will collide with this Wall in next frame given the Direction direc.
     * This method checks out the wall in future movement by extending 
     * @param sprite    instance of Moveable.
     * @param direc     instance of Direction. Specify the future direction. 
     * @return          <code>true</code> if this Wall is within the Moveable sprite's boundary
     *                  <code>false</code> otherwise.
    */
    public boolean checkWallFuture(Moveable sprite, Direction direc){
        if (sprite == null){
            return false;
        }
        int centerX = this.x + this.getWidth()/2;
        int centerY = this.y + this.getHeight()/2;
        int spriteLeft = sprite.getX() - 2;
        int spriteTop = sprite.getY() - 2;
        int spriteRight = sprite.getX() + sprite.getWidth() + 2;
        int spriteBottom = sprite.getY() + sprite.getHeight() + 2;
        
        if (direc == Direction.LEFT){
            spriteLeft = spriteLeft - 4;
            spriteRight = spriteRight - 4;
        }
        if (direc == Direction.RIGHT){
            spriteRight = spriteRight + 4;
            spriteLeft = spriteLeft + 4;
        }
        if (direc == Direction.UP){
            spriteTop = spriteTop - 4;
            spriteBottom = spriteBottom - 4;
        }
        if (direc == Direction.DOWN){
            spriteBottom = spriteBottom + 4;
            spriteTop = spriteTop + 4;
        }
        if (centerX > spriteLeft && centerX < spriteRight && centerY > spriteTop && centerY < spriteBottom){
            return true;
        }
        return false;
    }
}
