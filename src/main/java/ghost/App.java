package ghost;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * App is a public class that extends PApplet.
 * This class is specified for creating a screen
 * which can allows the game to be shown.
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public class App extends PApplet {
    /**
     * Width of the screen.
     */
    public static final int WIDTH = 448;

    /**
     * Height of the screen.
     */
    public static final int HEIGHT = 576;

    /**
     * The game manager for this App.
     */
    private GameManage manager;

    /**
     * The font used for game ending.
     */
    private PFont font;

    /**
     * Restart counts down after game ends.
     */
    private int timer;

    /**
     * Creates a App instance for showing the screen.
     */
    public App() {
        //Set up your objects 
    }

    /**
     * Sets up the environment and frame rate for the screen.
     */
    public void setup() {
        frameRate(60);
        font = createFont("PressStart2P-Regular.ttf", 16);
        this.manager = new GameManage();
        this.manager.setUp(this);
    }

    /**
     * Sets the size of the screen.
     */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Draws the Image controlled by the manage of this App.
     */
    public void draw() { 
        background(0, 0, 0);
        if (this.manager.isGameEnd()){
            if (this.manager.playerNoLife()){
                textFont(font);
                textAlign(CENTER, CENTER);
                text("GAME OVER", WIDTH/2, HEIGHT/2);
            }
            if (this.manager.isPlayerWin()){
                textFont(font);
                textAlign(CENTER, CENTER);
                text("YOU WIN", WIDTH/2, HEIGHT/2);
            }
            if (this.timer == 60 * 10){
                this.timer = 0;
                this.manager.restart();
            }else{
                this.timer ++;
            }
        }else{
            this.manager.draw(this);
        }
    }

    /**
     * Gets the input key from the user.
     * The key input is sent back to Player.
     */
    public void keyPressed(){
        if (key == CODED) {
            if (keyCode == UP) {
                this.manager.getPlayer().addCommand(Direction.UP);
            }else if (keyCode == DOWN){
                this.manager.getPlayer().addCommand(Direction.DOWN);
            }else if (keyCode == LEFT){
                this.manager.getPlayer().addCommand(Direction.LEFT);
            }else if (keyCode == RIGHT){
                this.manager.getPlayer().addCommand(Direction.RIGHT);
            }
        }else if (key == ' '){
            this.manager.change();
        }
    }

    /**
     * Gets the manager of this App.
     * @return the instance of GameManage for this App.
     */
    public GameManage getManager(){
        return this.manager;
    }
    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }
}
