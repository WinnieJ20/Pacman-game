package ghost;

import org.json.simple.JSONArray;
import processing.core.PImage;

/**
 * Ambusher is a class that extends from Ghost class.
 * Its target position is the top right corner of the screen during scatter mode.
 * Its target position is four grid away at the front of Player's current 
 * position.
 * Ambusher can be eaten during frightened mode.
 * In other time, it can eat Player
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public class Ambusher extends Ghost{

    /**
     * Creates an Ambusher instance with the given x and y coordinate, PImage sprite, speed, a list of time length
     * for each mode and a time length for frightened mode.
     * @param x         x coordinate of this Ambusher, measures in pixel.
     * @param y         y coordinate of this Ambusher, measures in pixel.
     * @param sprite    PImage of this Ambusher.
     * @param speed     moving speed of this Ambusher, measures in pixel per frame.
     * @param modeLength a JSONArray of how long each mode will be.
     * @param frightenTime the time length of frightened mode in seconds.
     */
    public Ambusher(int x, int y, PImage sprite, int speed, JSONArray modeLength, long frightenTime){
        super(x, y, sprite, speed, modeLength, frightenTime);
        this.targetX = 448;
        this.targetY = 0;
    }

    /**
     * Sets the target position in scatter mode.
     */
    public void setTarget(){
        this.targetX = 448;
        this.targetY = 0;
    }

    /**
     * Sets the target position in chase mode.
     * The x coordinate of the target is set within 0 - 448.
     * The y coordinate of the target is set within 0 - 576.
     * @param player        targeted player for this Ambusher.
     */
    public void setTarget(Moveable player){
        Direction now = player.current;
        if (now == Direction.UP){
            this.targetX = player.getX();
            this.targetY = player.getY() - 4 * 16;
        }else if (now == Direction.DOWN){
            this.targetX = player.getX();
            this.targetY = player.getY() + 4 * 16;
        }else if (now == Direction.LEFT){
            this.targetX = player.getX() - 4 * 16;
            this.targetY = player.getY();
        }else if (now == Direction.RIGHT){
            this.targetX = player.getX() + 4 * 16;
            this.targetY = player.getY();
        }
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
