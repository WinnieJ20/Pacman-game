package ghost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.json.simple.JSONArray;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Ghost is an abstract class that extends from Moveable class.
 * Every Ghost has a target position during the scatter mode 
 * and chase mode.
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public abstract class Ghost extends Moveable{

    /**
     * Previous direction of this Ghost.
     */
    protected Direction previous;

    /**
     * Contains the time length for scatter mode and chase mode.
     */
    protected JSONArray modeLength;

    /**
     * Current mode of this Ghost.
     */
    protected String mode;

    /**
     * Stores the PImage of this Ghost under a scatter or chase mode
     */
    protected PImage normalImage;

    /**
     * Index for accessing the time length from modeLength.
     */
    protected int index;

    /**
     * Counts down for each mode.
     */
    protected long timer;

    /**
     * Counts down for invisible time.
     */
    protected long invisibleCount;

    /**
     * Fixed time length for invisible.
     */
    protected final long INVISBLELENGTH = 5; // in seconds

    /**
     * Invisible status for this Ghost.
     */
    protected boolean isInvisble;

    /**
     * Frightened mode counts down.
     */
    protected long frightenTime;

    /**
     * Stores the frightened mode time length
     */
    protected long frightenStored;

    /**
     * x coordinate of the target.
     */
    protected int targetX;

    /**
     * y coordinate of the target.
     */
    protected int targetY;

    /**
     * Starting coordination of this Ghost.
     */
    protected int[] initialPos = new int[2];

    /**
     * 
     * @param x             x coordinate of this Ghost, measures in pixel.
     * @param y             y coordinate of this Ghost, measures in pixel.
     * @param sprite        PImage of this Ghost.
     * @param speed         moving speed of this Ghost, measures in pixel per frame.
     * @param modeLength   JSONArray of how long each mode will be.
     * @param frightenLength  the time length of frightened mode in second.
     */
    public Ghost(int x, int y, PImage sprite, int speed, JSONArray modeLength, long frightenLength){
        super(x, y, sprite, speed); //sprite - 28x28
        this.normalImage = sprite;
        this.current = Direction.LEFT;
        this.previous = Direction.LEFT;
        this.modeLength = modeLength;
        this.mode = "scatter";
        this.index = 0;
        this.targetX = 0;
        this.targetY = 0;
        //this.timer = (long) modeLength.get(0) * 60;
        this.timer = 0;
        this.frightenStored = frightenLength * 60;
        this.initialPos[0] = x;
        this.initialPos[1] = y;
    }

    /**
     * Draws this Ghost on the screen.
     * If app is null, nothing is drawn.
     * If this Ghost is in frightened mode, its PImage changes to
     * frightened PImage.
     * If not in frightened mode, normal PImage is used.
     * If this Ghost is eaten, it will not be drawn.
     * @param app       instance of PApplet which is for drawing on 
     *                  the screen.
     */
    public void draw(PApplet app){
        if (app == null){
            return;
        }
        if (this.mode.equals("frighten")){
            this.sprite = app.loadImage("src/main/resources/frightened.png");
        }else{
            this.sprite = this.normalImage;
        }
        if (! this.isEaten && ! this.isInvisble){
            app.image(this.sprite, this.x, this.y);
        }
    }

    /**
     * Sets this Ghost in eaten status if this Ghost is collided with player
     * during frightened mode.
     * @param player        instance of Image that can eat
     *                      this Ghost.
     */
    public void eaten(Image player){
        if (this.mode.equals("frighten")){
            if (this.checkCollideCenter(player)){
                this.isEaten = true;
            }
        }
    }

    /**
     * Sets every attribute and field back to the inital 
     * stage.
     * x coordinate of this Ghost becomes inital x coordinate.
     * y coordinate of this Ghost becomes inital y coordinate.
     * Current and previous direction of this GHost is set to left.
     * isEaten flag is back to false.
     * The current mode will be in scatter.
     * The index of this Ghost goes back to 0.
     * The frightenTime goes to 0.
     * The timer is set back to its inital value.
     */
    public void restart(){
        this.x = this.initialPos[0];
        this.y = this.initialPos[1];
        this.current = Direction.LEFT;
        this.previous = Direction.LEFT;
        this.isEaten = false;
        this.isInvisble = false;
        this.invisibleCount = 0;
        this.mode = "scatter";
        this.index = 0;
        this.frightenTime = 0;
        this.timer = 0;
    }

    /**
     * Gets the target position.
     * This function is specifically used for the debugging mode in 
     * GameManage.
     * @return      an integer array which contains the position of the 
     *              x and y coordinate of the target.
     */
    public int[] debug(){
        int[] positions = new int[4];
        positions[0] = this.x + this.getWidth()/2;
        positions[1] = this.y + this.getHeight()/2;
        positions[2] = this.targetX;
        positions[3] = this.targetY;
        return positions;
    }


    /**
     * Gets the list of Direction with wall around this Ghost.
     * This list includes the direction that this Ghost can not go back.
     * @param walls     A list of Wall object existing on the screen
     * @return          an ArrayList of directions that have Wall
     *                  nearby this Ghost.
     */
    public ArrayList<Direction> checkWallAround(ArrayList<Wall> walls){// return an array of directions that have wall nearby
        ArrayList<Direction> wallAround = new ArrayList<>();
        if (walls == null){
            return null;
        }
        wallAround.add(this.current.opposite());
        wallAround.add(this.previous.opposite());
        Direction directionA = null;
        Direction directionB = null;

        if (this.current == Direction.DOWN || this.current == Direction.UP){
            directionA = Direction.LEFT;
            directionB = Direction.RIGHT;
        }else{
            directionA = Direction.UP;
            directionB = Direction.DOWN;
        }
        
        for (Wall w : walls){
            if (w.checkWallFuture(this, directionA)){
                if (! wallAround.contains(directionA)){
                    wallAround.add(directionA);
                }
            }else if (w.checkWallFuture(this, directionB)){
                if (! wallAround.contains(directionB)){
                    wallAround.add(directionB);
                }
            }else if (w.checkWallFuture(this, this.current)){
                if (! wallAround.contains(this.current)){
                    wallAround.add(this.current);
                }
            }
        }
        return wallAround;
    }

    /**
     * Gets the directions with how far the distance is from the target.
     * @return      Each direction with a distance value from the target.
     */
    public HashMap<Direction, Double> direcDist(){
        int newLeft = 0;
        int newTop = 0;
        double distance = 0;
        Direction[] Directions = Direction.values();

        HashMap<Direction, Double> dircs = new HashMap<>();
        for (int i = 0; i < Directions.length; i++){
            if (i == 0 || i == 2){// go left or right, get a new left x
                if (i == 0){//left
                    newLeft = this.x - this.getWidth();
                }else if (i == 2){ // right
                    newLeft = this.x + this.getWidth();
                }
                distance = Math.pow(targetX - newLeft, 2) + Math.pow(targetY - this.y, 2);
            }else{// go up or down, get a new top y
                if (i == 1){ // up
                    newTop = this.y - this.getHeight();
                }else if (i == 3){ // down
                    newTop = this.y + this.getHeight();
                }
                distance = Math.pow(targetX - this.x, 2) + Math.pow(targetY - newTop, 2);
            }
            dircs.put(Directions[i], distance);
        }
        return dircs;
    }

    /**
     * Determines which direction does not have Wall blocking.
     * @param wallDir       a list of Wall that is around this Ghost.
     * @return              a list of Direction that doest not have Wall
     *                      in that direction.
     */
    public ArrayList<Direction> directionNoWalls(ArrayList<Direction> wallDir){
        
        ArrayList<Direction> noWalls = new ArrayList<>();
        for (int i = 0; i < Direction.values().length; i++){
            if (wallDir.contains(Direction.values()[i])){
                continue;
            }else{
                noWalls.add(Direction.values()[i]);
            }
        }
        if (noWalls.contains(this.current.opposite())){
            noWalls.remove(this.current.opposite());
        }
        if (previous != null){
            if (noWalls.contains(this.previous.opposite())){
                noWalls.remove(this.previous.opposite());
            }
        }
        return noWalls;
    }

    /**
     * Sets the target position in scatter mode.
     */
    public abstract void setTarget();

    /**
     * Sets the target position in chase mode.
     * @param sprite        target for this Ghost.
     */
    public abstract void setTarget(Moveable sprite);

    /**
     * Determine the movement of this Ghost.
     * Gets the direction with no wall blocking and the shortest path 
     * to the target. 
     * This function controls the current and previous direction of 
     * this Ghost.
     * @param noWalls       a list of Direction that does not have Wall 
     *                      in that direction.
     */
    public void move(ArrayList<Direction> noWalls){
        HashMap<Direction, Double> dircs = this.direcDist();
        if (noWalls.size() == 1){
            this.previous = this.current;
            this.current = noWalls.get(0);
        }else if (noWalls.isEmpty()){
            this.previous = this.current;
        }else{
            double[] dist = new double[noWalls.size()];
            for (int i = 0; i < noWalls.size(); i++){
                dist[i] = dircs.get(noWalls.get(i));
            }
            Arrays.sort(dist);
            for (int i = 0; i < noWalls.size(); i++){
                if (dircs.get(noWalls.get(i)) == dist[0]){
                    this.previous = this.current;
                    this.current = noWalls.get(i);
                    break;
                }
            }
        }
    }

    /**
     * Sets the required mode for this Ghost.
     * @param mode      Mode that this Ghost is in.
     */
    public void setMode(String mode){
        this.mode = mode;
        if (mode.equals("frighten")){
            this.frightenTime = this.frightenStored;
        }
    }

    /**
     * Sets this Ghost to be invisble.
     */
    public void setInvisible(){
        this.isInvisble = true;
        this.invisibleCount = this.INVISBLELENGTH * 60;
    }

    /**
     * Gets the current mode of this Ghost.
     * @return          the current mode of this Ghost.
     */
    public String getMode(){
        return this.mode;
    }

    /**
     * Handles the logic behind this Ghost.
     * This method is called every frame.
     * The movement and behaviour of this Ghost is 
     * specified in this method.
     * The effect of frighten mode and invisible can come together.
     * @param app       an instance of App that can load PImage and get access to Wall object
     *                  list. 
     */
    public void tick(App app){
        this.x += this.xvel;
        this.y += this.yvel;
        ArrayList<Wall> walls = app.getManager().getMap().getWalls();
        ArrayList<Direction> wallDir = this.checkWallAround(walls);
        ArrayList<Direction> noWalls = this.directionNoWalls(wallDir);
        if (this.isInvisble){
            if (this.invisibleCount != 0){
                this.invisibleCount --;
            }else{
                this.isInvisble = false;
            }
        }
        if (this.mode.equals("frighten")){
            Random ran = new Random();
            int num = 0;
            if (noWalls.size() > 0){
                num = ran.nextInt(noWalls.size());
                this.previous = this.current;
                this.current = noWalls.get(num);
            }
            this.frightenTime--;
            this.setTarget();
            if (this.frightenTime == 0){
                if (index % 2 == 0){
                    this.mode = "scatter";
                }else{
                    this.mode = "chase";
                }
            }
        }else{
            if (this.timer == (long) modeLength.get(index) * 60){
                index = (index + 1) % modeLength.size();
                //this.timer = (long) modeLength.get(index) * 60; // 60 frame per second
                this.timer = 0;
            }else{
                this.timer ++;
            }
    
            if (index % 2 == 0){
                this.mode = "scatter";
            }else{
                this.mode = "chase";
            }
            if (this.mode.equals("scatter")){
                this.setTarget();
            }else if (this.mode.equals("chase")){
                this.setTarget(app.getManager().getPlayer());
            }
            this.move(noWalls);
        }
        // if (this.mode.equals("scatter")){
        //     this.setTarget();
        // }else if (this.mode.equals("chase")){
        //     this.setTarget(app.getManager().getPlayer());
        // }
        // this.move(noWalls);

        if (current == Direction.LEFT){
            this.goLeft();
        }else if (current == Direction.UP){
            this.goUp();
        }else if (current == Direction.RIGHT){
            this.goRight();
        }else if (current == Direction.DOWN){
            this.goDown();
        }
    }
}
