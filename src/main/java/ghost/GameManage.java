package ghost;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.*;

/**
 * GmaeManage is a public class that implements Resetable.
 * This is the total control of the game.
 * It manages the behaviour and logic of Player, Ghost, Wall,
 * and Fruit. 
 * GameManage also control Maze class.
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public class GameManage implements Resetable{
    /**
     * Player who controlls the Waka.
     */
    private Player player;

    /**
     * List of Ghost objects to be drawn on screen.
     */
    private Ghost[] ghosts;

    /**
     * The map of this GameMange.
     */
    private Maze map;

    /**
     * Lists of Fruit object to be drawn on screen.
     */
    private ArrayList<Fruit> fruits;

    /**
     * Flag for whether a super fruit is eaten or not.
     */
    private boolean superEaten;

    /**
     * Flag for whether a SodaCan is eaten.
     */
    private boolean sodaEaten;

    /**
     * Flag for in the debugging mode or not.
     */
    private boolean inDebug;

    /**
     * Creates an instance of GameManage.
     */
    public GameManage(){
    }

    /**
     * Sets up the environment in this GameMange.
     * @param app       instance of PApplet which allows the set up
     *                  of load PImage.
     */
    public void setUp(PApplet app){
        this.map = new Maze();
        map.readJson();
        map.readFile(app);
        this.fruits = map.getFruits();
        this.ghosts = new Ghost[map.getGhosts().size()];
        
        int playerX = map.getPlayerPos()[0] * 16 - 3;
        int playerY = map.getPlayerPos()[1] * 16 - 6;
        PImage playerImage = app.loadImage("src/main/resources/playerClosed.png");
        this.player = new Player(playerX, playerY, playerImage, map.getSpeed(), map.getLives());
        for (int i = 0; i < this.ghosts.length; i++){
            this.ghosts[i] = this.map.getGhosts().get(i);
        }
    }

    /**
     * Draws the objects on the screen by implementing the logic of Image.
     * @param app       instance of App which allows this GameManage to draw.
     */
    public void draw(App app){
        for (Fruit f : this.fruits){
            f.eaten(this.player);
            if (f.isSuperFruit && f.isEaten() && !f.isUsed()){
                this.superEaten = true;
                f.setUsed();
            }
            if (f.isSoda && f.isEaten() && !f.isUsed()){
                this.sodaEaten = true;
                f.setUsed();
            }
            f.draw(app);
        }
        this.player.tick(app);
        this.map.draw(app);
        app.stroke(255);
        if (this.ghosts.length != 0){
            for (Ghost g : this.ghosts){
                if (this.superEaten){
                    g.setMode("frighten");
                }
                if (this.sodaEaten){
                    g.setInvisible();
                }
                if (this.inDebug){
                    int[] position = g.debug();
                    app.line(position[0], position[1], position[2], position[3]);
                }
                g.eaten(this.player);
                g.tick(app);
                g.draw(app);
            }
        }
        this.map.setLives(this.player.getLives());
        this.player.draw(app);
        if (this.player.isEaten){
            this.resetGhost();
            this.player.isEaten = false;
        }
        this.superEaten = false;
        this.sodaEaten = false;
    }

    /**
     * Gets the current debug mode status.
     * @return  true if in debug mode, otherwise false.
     */
    public boolean isDebug(){
        return this.inDebug;
    }

    /**
     * Gets the flag of whether SodaCan is eaten or not.
     * @return true if SodaCan is eaten, otherwise false.
     */
    public boolean isSodaEaten(){
        return this.sodaEaten;
    }

    /**
     * Gets the flag of whether SuperFruit is eaten or not.
     * @return true if SuperFruit is eaten, otherwise false.
     */
    public boolean isSuperEaten(){
        return this.superEaten;
    }
    
    /**
     * Determines if Player wins.
     * @return          <code>true</code> if Player has eaten every Fruit on the map.
     *                  <code>false</code> otherwise.
     */
    public boolean isPlayerWin(){
        for (Fruit f : this.fruits){
            if (f.isEaten()){
                continue;
            }else{
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if Player is dead with no lives left.
     * @return          <code>true</code> if Player has no lives left.
     *                  <code>false</code> otherwise.
     */
    public boolean playerNoLife(){
        if (! this.player.isAlived()){
            return true;
        }
        return false;
    }

    /**
     * Determines if the game is ended or not.
     * @return          <code>true</code> if Player has no lives left or
     *                                    Player has eaten every fruit.
     *                  <code>false</code> otherwise.
     */
    public boolean isGameEnd(){
        if (this.playerNoLife() || this.isPlayerWin()){
            return true;
        }
        return false;
    }

    /**
     * Gets the list of Ghosts objects.
     * @return an array of Ghost objects in the map.
     */
    public Ghost[] getGhosts(){
        return this.ghosts;
    }

    /**
     * Gets the map of this GameManage.
     * @return map of this GameManage.
     */
    public Maze getMap(){
        return this.map;
    }

    /**
     * Gets Player.
     * @return an instance of Player.
     */
    public Player getPlayer(){
        return this.player;
    }
    
    /**
     * Alternates between debug mode.
     * If inDebug flag is true, then it is set to be false.
     * If inDebug flag is false, then it is set to be true.
     */
    public void change(){
        if (this.inDebug == true){
            this.inDebug = false;
        }else{
            this.inDebug = true;
        }
    }

    /**
     * Restarts the Ghost objects in the ghost list of this GameManage.
     */
    public void resetGhost(){
        for (Ghost ghost : this.ghosts){
            ghost.restart();
        }
    }

    /**
     * Restarts the whole Game with initial setting.
     * Player is restarted.
     * Each Ghost is restarted.
     * Each Fruit is restarted.
     */
    public void restart(){
        this.player.restart();
        this.resetGhost();
        for (Fruit fruit : this.fruits){
            fruit.restart();
        }
    }
}
