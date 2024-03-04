package ghost;

import processing.core.PImage;
import org.json.simple.JSONArray;

/**
 * Ignorant is a class that extends from Ghost class.
 * Its target position is the bottom left corner of the screen 
 * during scatter mode.
 * In chase mode, if more than 8 units grid away from 
 * Player (straight line distance), target location is Player. 
 * Otherwise, target location is bottom left corner.
 * Ignorant can be eaten during frightened mode.
 * In other time, it can eat Player
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public class Ignorant extends Ghost{

    /**
     * Creates a Ignorant instance with the given x and y coordinate, PImage sprite, speed, a list of time length
     * for each mode and a time length for frightened mode.
     * @param x         x coordinate of this Ignorant, measures in pixel.
     * @param y         y coordinate of this Ignorant, measures in pixel.
     * @param sprite    PImage of this Ignorant.
     * @param speed     moving speed of this Ignorant, measures in pixel per frame.
     * @param modeLength a JSONArray of how long each mode will be.
     * @param frightenTime the time length of frightened mode in seconds.
     */
    public Ignorant(int x, int y, PImage sprite, int speed, JSONArray modeLength, long frightenTime){
        super(x, y, sprite, speed, modeLength, frightenTime);
        this.targetX = 0;
        this.targetY = 576;
    }

    /**
     * Sets the target position in scatter mode.
     */
    public void setTarget(){
        this.targetX = 0;
        this.targetY = 576;
    }

    /**
     * Sets the target position in chase mode.
     * The x coordinate of the target is the x coordinate of Moveable player if 
     * 8 unit grids away. Otherwise 0.
     * The y coordinate of the target is the y coordinate of Mobeable player if
     * 8 unit grids away. Otherwise 576.
     * The x coordinate of the target is set within 0 - 448.
     * The y coordinate of the target is set within 0 - 576.
     * @param player        targetted player for this Ignorant.
     */
    public void setTarget(Moveable player){
        double distance = Math.pow(player.getX() - this.x, 2) + Math.pow(player.getY() - this.y, 2);
        double unit = Math.sqrt(distance); //in pixels
        double grid = unit/16;
        if (grid > 8){
            this.targetX = player.getX();
            this.targetY = player.getY();
        }else{
            this.targetX = 0;
            this.targetY = 576;
        }
    }
}
