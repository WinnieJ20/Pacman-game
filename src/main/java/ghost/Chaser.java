package ghost;

import org.json.simple.JSONArray;
import processing.core.PImage;

/**
 * Chaser is a class that extends from Ghost class.
 * Its target position is the top left corner of the screen during scatter mode.
 * Its target position is the grid occupied by Player's current position
 * during chase mode.
 * Chaser can be eaten during frightened mode.
 * In other time, it can eat Player
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public class Chaser extends Ghost{

    /**
     * Creates a Chaser instance with the given x and y coordinate, PImage sprite, speed, a list of time length
     * for each mode and a time length for frightened mode.
     * @param x         x coordinate of this Chaser, measures in pixel.
     * @param y         y coordinate of this Chaser, measures in pixel.
     * @param sprite    PImage of this Chaser.
     * @param speed     moving speed of this Chaser, measures in pixel per frame.
     * @param modeLength a JSONArray of how long each mode will be.
     * @param frightenTime the time length of frightened mode in seconds.
     */
    public Chaser(int x, int y, PImage sprite, int speed, JSONArray modeLength, long frightenTime){
        super(x, y, sprite, speed, modeLength, frightenTime);
        this.targetX = 0;
        this.targetY = 0;
    }

    /**
     * Sets the target position in scatter mode.
     */
    public void setTarget(){
        this.targetX = 0;
        this.targetY = 0;
    }

    /**
     * Sets the target position in chase mode.
     * The x coordinate of the target is the x coordinate of Moveable player.
     * The y coordinate of the target is the y coordinate of Mobeable player.
     * @param player        targetted player for this Chaser.
     */
    public void setTarget(Moveable player){
        this.targetX = player.getX();
        this.targetY = player.getY();
    }
}
