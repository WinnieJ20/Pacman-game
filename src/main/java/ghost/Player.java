package ghost;

import processing.core.PImage;
import processing.core.PApplet;

import java.util.*;

/**
 * Player is a class that extends from Moveable class.
 * Player is an instance that is controllable by the user.
 * Player can be controlled by the direction keys on keyboard.
 * Player cannot go through Wall but can go through and eat Fruit.
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public class Player extends Moveable{

    /**
     * Lives of this Player in the game.
     */
    private int lives;

    /**
     * Index number for alternating between close and open PImage.
     */
    private int imageIndex;

    /**
     * Future direction inputted by user.
     */
    private Direction next; 

    /**
     * Inital x, y coordinate position of this Player.
     */
    private int[] initialPos;

    /**
     * Inital lives this Player have at the start of the game.
     */
    private int initialLife;

    /**
     * Creates a Player instance with the given x, y coordinate, PImage sprite, 
     * speed and lives at the start.
     * @param x         x coordinate of this Player, measures in pixel.
     * @param y         y coordinate of this Player, measures in pixel.
     * @param sprite    PImage of this Player.
     * @param speed     moving speed of this Player, measures in pixel per frame.
     * @param lives     lives of this Player
     */
    public Player(int x, int y, PImage sprite, int speed, int lives){
        super(x, y, sprite, speed);
        this.initialPos = new int[]{this.x, this.y};
        this.lives = lives;
        this.initialLife = lives;
        this.imageIndex = 0;
    }

    /**
     * Handles the logic behind this Player.
     * This method is called every frame.
     * The movement and behaviour of this Player is 
     * specified in this method.
     * @param app       an instance of App that can load PImage and get Wall objects lists. 
     */
    public void tick(App app){  //handles logic
        this.x += this.xvel; // postive xyel: right, negative xvel: left
        this.y += this.yvel; //postive yvel: down, negative yvel: up
        
        ArrayList<Wall> walls = app.getManager().getMap().getWalls();
        if (this.next == null && this.current != null && this.checkWallAhead(walls)){
            this.xvel = 0;
            this.yvel = 0;
        }else if (this.checkWallforNext(walls) && this.checkWallAhead(walls) && this.current != null && this.next != null){
            this.xvel = 0;
            this.yvel = 0;
        }else if (this.next != null && walls != null && ! this.checkWallforNext(walls)){
            this.current = this.next;
            this.next = null;
            if (current != null){
                if (current == Direction.UP){
                    this.goUp();
                }else if (current == Direction.DOWN){
                    this.goDown();
                }else if (current == Direction.RIGHT){
                    this.goRight();
                }else if (current == Direction.LEFT){
                    this.goLeft();
                }
            }
        }
        PImage[] imagesUp = {app.loadImage("src/main/resources/playerUp.png"), app.loadImage("src/main/resources/playerClosed.png")};
        PImage[] imagesDown = {app.loadImage("src/main/resources/playerDown.png"), app.loadImage("src/main/resources/playerClosed.png")};
        PImage[] imagesLeft = {app.loadImage("src/main/resources/playerLeft.png"), app.loadImage("src/main/resources/playerClosed.png")};
        PImage[] imagesRight = {app.loadImage("src/main/resources/playerRight.png"), app.loadImage("src/main/resources/playerClosed.png")};
               
        if (app.frameCount % 8 == 0) {
            if (current == null){
                this.sprite = imagesLeft[imageIndex];
                imageIndex = (imageIndex + 1) % 2;
            }else if (current == Direction.LEFT){
                this.sprite = imagesLeft[imageIndex];
                imageIndex = (imageIndex + 1) % 2;
            }else if (current == Direction.RIGHT){
                this.sprite = imagesRight[imageIndex];
                imageIndex = (imageIndex + 1) % 2;
            }else if (current == Direction.UP){
                this.sprite = imagesUp[imageIndex];
                imageIndex = (imageIndex + 1) % 2;
            }else if (current == Direction.DOWN){
                this.sprite = imagesDown[imageIndex];
                imageIndex = (imageIndex + 1) % 2;
            }
        }
        Ghost[] ghosts = app.getManager().getGhosts();
        for (Ghost g : ghosts){
            if (! g.getMode().equals("frighten") && ! g.isEaten && ! g.isInvisble){
                this.eaten(g);
            }
        }
    }

    /**
     * Draws this Player on the screen.
     * If app is null, nothing is drawn.
     * @param app       instance of PApplet which is for drawing on 
     *                  the screen.
     */
    public void draw(PApplet app){
        //handling graphics - ideally: a few lines just to draw (no if statement)
        if (app == null){
            return;
        }
        app.image(this.sprite, this.x, this.y);
    }

    /**
     * Determine whether this Player still have lives left.
     * @return          <code>true</code> if this Player still lives left.
     *                  <code>false</code> otherwise.
     */
    public boolean isAlived(){
        if (this.lives > 0){
            return true;
        }
        return false;
    }

    /**
     * Sets every attribute and field back to the inital 
     * stage.
     * x coordinate of this Player becomes inital x coordinate.
     * y coordinate of this Player becomes inital y coordinate.
     * current and next direction of this Player is set to null.
     * lives of this Player is set to inital lives.
     */
    public void restart(){
        this.x = this.initialPos[0];
        this.y = this.initialPos[1];
        this.current = null;
        this.next = null;
        this.xvel = 0;
        this.yvel = 0;
        this.lives = this.initialLife;
        this.isEaten = false;
    }

    /**
     * Checks if this Player is collided with ghost and has been eaten.
     * If collided, lives of this Player decrements by 1, 
     * the xvel and yvel of this Player back to 0.
     * @param ghost     an Image that could collide with this Player.
     */
    public void eaten(Image ghost){
        if (ghost == null){
            return;
        }
        if (this.checkCollideCenter(ghost)){
            --this.lives;
            this.xvel = 0;
            this.yvel = 0;
            this.current = null;
            this.next = null;
            this.x = this.initialPos[0];
            this.y = this.initialPos[1];
            this.isEaten = true;
        }
    }

    /**
     * Determine if there is a Wall in front of this Player.
     * @param walls     Arraylist of Wall objects from the map. 
     * @return          <code>true</code> if this Player is collding with Wall
     *                  <code>false</code> otherwise.
     */
    public boolean checkWallAhead(ArrayList<Wall> walls){
        //checking for the wall in the direction of the current command; true if wall is blocking, otherwise false
        if (walls == null){
            return false;
        }
        for (Wall w : walls){     
            if (w.checkCollideCenter(this)){
                if (current != null){
                    if (current == Direction.UP){
                        this.y = this.y + 4;
                    }else if (current == Direction.DOWN){
                        this.y = this.y - 4;
                    }else if (current == Direction.LEFT){
                        this.x = this.x + 4;
                    }else if (current == Direction.RIGHT){
                        this.x = this. x - 4;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether there is a Wall in the future frame according to the next command.
     * @param walls     Arraylist of Wall objects from the map. 
     * @return          <code>true</code> if this Player is collding with Wall
     *                  <code>false</code> otherwise.
     */
    public boolean checkWallforNext(ArrayList<Wall> walls){
    //checking for the wall in the direction of the next command; true if wall is blocking, otherwise false
        if (walls == null || this.next == null){
            return false;
        }
        int playerRight = this.x + this.getWidth();
        int playerBottom = this.y + this.getHeight();
        int playerExtendTop = 0;
        int playerExtendBottom = 0;
        int playerExtendRight = 0;
        int playerExtendLeft = 0;
        int extract = 0;
        int extend = 0;
        if (current != null && next != null){
            if ( (current == Direction.UP || current == Direction.DOWN) && (next == Direction.RIGHT || next == Direction.LEFT) ){
                extract = 3;
                extend = 1;
            }else if ( (current == Direction.RIGHT || current == Direction.LEFT) && (next == Direction.UP || next == Direction.DOWN) ){
                extract = 3;
                extend = 1;
            }
        }
        
        for (Wall w : walls){
            int wallCenterX = w.getX() + w.getWidth()/2;
            int wallCenterY = w.getY() + w.getHeight()/2;
            
            if (next == Direction.UP){ //detect the wall in future by moving the player up by its height;
                playerExtendTop = this.y - this.getHeight();
                playerExtendBottom = this.y;
            }else if (next == Direction.DOWN){//detect the wall in future by moving the player down by its height;
                playerExtendTop = playerBottom;
                playerExtendBottom = playerBottom + this.getHeight();
            }
            if (wallCenterX > this.x - extend && wallCenterX < playerRight + extract && wallCenterY > playerExtendTop && wallCenterY < playerExtendBottom){
                return true;
            }
            if (next == Direction.RIGHT){//detect the wall in future by moving the player right by its width;
                playerExtendRight = playerRight + this.getWidth();
                playerExtendLeft = playerRight;
            }else if (next == Direction.LEFT){//detect the wall in future by moving the player left by its width;
                playerExtendRight = this.x;
                playerExtendLeft = this.x - this.getWidth();
            } 
            if (wallCenterX > playerExtendLeft && wallCenterX < playerExtendRight && wallCenterY > this.y - extend && wallCenterY < playerBottom + extract){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the lives of this Player.
     * @return lives of this Player.
     */
    public int getLives(){
        return this.lives;
    }

    /**
     * Gets the next Direction of this Player;
     * @return the inputted next direction by the user.
     */
    public Direction getNextDirection(){
        return this.next;
    }

    /**
     * Takes command from the user.
     * Adds the command to this Player's next command.
     * @param direction     input direction from the user.
     */
    public void addCommand(Direction direction){
        this.next = direction;
    }
}
