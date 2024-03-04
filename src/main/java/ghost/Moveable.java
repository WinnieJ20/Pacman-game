package ghost;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Moveable is an abstract class that extends from Image class and 
 * implements Eatable and Resetable interface.
 * Moveable object can move in the screen created a PApplet instance with 
 * four different directions.
 * Moveable object can both be eaten or resetted.
 */
public abstract class Moveable extends Image implements Eatable, Resetable {
    /**
     * The x velocity of this Moveable.
     */
    protected int xvel;

    /**
     * The y velocity of this Moveable.
     */
    protected int yvel;

    /**
     * Speed of this Moveable.
     */
    protected int speed;

    /**
     * Current direction of this Moveable.
     */
    protected Direction current;

    /**
     * A flag of this Moveable being eaten or not.
     */
    protected boolean isEaten;

    /**
     * 
     * @param x         x coordinate of this Moveable, measures in pixel.
     * @param y         y coordinate of this Moveable, measures in pixel.
     * @param sprite    PImage of this Moveable.
     * @param speed     moving speed of this Moveable, measures in pixel per frame.
     */
    public Moveable(int x, int y, PImage sprite, int speed){
        super(x, y, sprite);
        this.speed = speed;
        this.xvel = 0;
        this.yvel = 0;
        this.current = null;
    }

    /**
     * Moves this Moveable vertically up in the screen.
     */
    public void goUp(){
        this.xvel = 0;
        this.yvel = -this.speed;
    }

    /**
     * Moves this Moveable vertically down in the screen.
     */
    public void goDown(){
        this.xvel = 0;
        this.yvel = this.speed;
    }

    /**
     * Moves this Moveable horizontally to the right direction in the screen.
     */
    public void goRight(){
        this.yvel = 0;
        this.xvel = this.speed;
    }

    /**
     * Moves this Moveable horizontally to the left direction in the screen.
     */
    public void goLeft(){
        this.yvel = 0;
        this.xvel = -this.speed;
    }
    /**
     * Handles the logic behind this Player.
     * This method is called every frame.
     * The movement and behaviour of this Moveable is 
     * specified in this method.
     * @param app       an instance of App that can load PImage and get 
     *                  access to attribute in Maze through its GameMange
     *                  instance.
     */
    public abstract void tick(App app);
    /**
     * Draws this Moveable on the screen.
     * @param app       instance of PApplet which is for drawing on 
     *                  the screen.
     */
    public abstract void draw(PApplet app);
}
