package ghost;

import org.json.simple.JSONArray;
import processing.core.PImage;

/**
 * Ignorant is a class that extends from Ghost class.
 * Its target position is the bottom right corner of the screen 
 * during scatter mode.
 * Its target position is doubling the vector from Chaser to 2 grid spaces ahead of
 * Player during chase mode. 
 * If Chaser is eaten, then Whim's target position will be Player position during chase mode.
 * Otherwise, target location is bottom left corner.
 * Whim can be eaten during frightened mode.
 * In other time, it can eat Player
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public class Whim extends Ghost{
    /**
     * targeting position depends on Chaser when this Whim is in 
     * chase mode.
     */
    private Ghost chaser;

    /**
     * Creates a Ignorant instance with the given x and y coordinate, PImage sprite, speed, a list of time length
     * for each mode and a time length for frightened mode.
     * @param x         x coordinate of this Whime, measures in pixel.
     * @param y         y coordinate of this Whime, measures in pixel.
     * @param sprite    PImage of this WHime.
     * @param speed     moving speed of this Whim, measures in pixel per frame.
     * @param modeLength a JSONArray of how long each mode will be.
     * @param frightenTime the time length of frightened mode in seconds.
     * @param chaser     targeting position of this Whim depends on Chaser.
     */
    public Whim(int x, int y, PImage sprite, int speed, JSONArray modeLength, long frightenTime, Ghost chaser){
        super(x, y, sprite, speed, modeLength, frightenTime);
        this.targetX = 448;
        this.targetY = 576;
        this.chaser = chaser;
    }

    /**
     * Sets the target position in scatter mode.
     */
    public void setTarget(){
        this.targetX = 448;
        this.targetY = 576;
    }

    /**
     * if Chaser is eaten, then the target position of 
     * this Whim is Player position, else normal targeting 
     * method for this Whim.
     * The x coordinate of the target is set within 0 - 448.
     * The y coordinate of the target is set within 0 - 576.
     * @param player    targetted player for this Whim.
     */
    public void setTarget(Moveable player){
        if (chaser.isEaten){
            targetX = player.getX();
            targetY = player.getY();
            return;
        }
        Direction now = player.current;
        int frontX = 0;
        int frontY = 0;
        if (now == Direction.UP){
            frontX = player.getX();
            frontY = player.getY() - 2 * 16;
        }else if (now == Direction.DOWN){
            frontX = player.getX();
            frontY = player.getY() + 2 * 16;
        }else if (now == Direction.LEFT){
            frontX = player.getX() - 2 * 16;
            frontY = player.getY();
        }else if (now == Direction.RIGHT){
            frontX = player.getX() + 2 * 16;
            frontY = player.getY();
        }
        
        int smallVectorX = frontX - this.chaser.getX();
        int smallVectorY = frontY - this.chaser.getY();

        targetX = this.chaser.getX() + 2 * smallVectorX;
        targetY = this.chaser.getY() + 2 * smallVectorY;

        if (this.targetX < 0){
            this.targetX = 0;
        }else if (this.targetX > 448){
            this.targetX = 448;
        }
        if (this.targetY < 0){
            this.targetY = 0;
        }else if (this.targetY > 576){
            this.targetY = 576;
        }
    }
}
