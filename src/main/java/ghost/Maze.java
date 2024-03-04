package ghost;

import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Maze is a public class that parses the map.
 * Maze reads in the JSON file and parses the map
 * extracted from that JSON file.
 * Each required features for other Image classes is parsing
 * in Maze.
 * @author      Winnie Jiang
 * @version     %I%, %G%
 */
public class Maze {
    /**
     * Lives of Player.
     */
    private int lives;

    /**
     * File name of the map to be parsed.
     */
    private String mapFile;

    /**
     * The speed of Image.
     */
    private int speed;

    /**
     * The frightened mode time length for Ghost in seconds.
     */
    private long frightenLength;

    /**
     * The JSONArray of time length for scatter and chase mode.
     */
    private JSONArray modeLength;

    /**
     * Resource path for each number in the map file.
     */
    private HashMap<String, String> charaNum;

    /**
     * List of Fruit objects that will be drawn on the screen.
     */
    private ArrayList<Fruit> fruits;

    /**
     * List of Wall objects that will be drawn on the screen.
     */
    private ArrayList<Wall> walls; 

    /**
     * Inital position for Player.
     */
    private int[] playerPos;

    /**
     * List of Ghosts that will be drawn on the screen.
     */
    private ArrayList<Ghost> ghosts;

    /**
     * Stores Ghost chaser for Whim.
     */
    private Ghost chaser;

    /**
     * Initial position for Whim.
     */
    private int[] whimPos;
    
    /**
     * Creates a new instance of Maze.
     */
    public Maze(){
        this.charaNum = new HashMap<String, String>();
        this.walls = new ArrayList<Wall>(); //walls, pos;
        this.playerPos = new int[2];
        this.ghosts = new ArrayList<Ghost>();
        this.fruits = new ArrayList<Fruit>();
    }
    /**
     * Sets up the basic graphic for game.
     * Draws Wall objects on the screen and lives that Player
     * has at the bottom left of the screen.
     * @param app       an instance of PApplet for loading PImage.
     */
    public void draw(PApplet app){
        if (app == null){
            return;
        }
        for ( Wall wall : this.walls){
            wall.draw(app);
        }
        PImage pimage = app.loadImage("src/main/resources/playerRight.png");
        for (int i = 0; i < lives; i++){
            int x = 8 + pimage.width * i;
            int y = 33;
            app.image(pimage, x, y * 16 + 10);
        }
    }

    /**
     * Sets the lives shown on the screen.
     * @param number       number of lives that Player has.
     */
    public void setLives(int number){
        this.lives = number;
    }

    /**
     * Gets the lives of this Maze stored.
     * @return the lives of this Maze stored.
     */
    public int getLives(){
        return this.lives;
    }

    /**
     * Gets the speed of this Maze stored.
     * @return the speed of this Maze stored.
     */
    public int getSpeed(){
        return this.speed;
    }

    /**
     * Gets the list of Fruit object that this Maze stored.
     * @return the list of Fruit objects that this Maze stored.
     */
    public ArrayList<Fruit> getFruits(){
        return this.fruits;
    }

    /**
     * Gets the list of Wall object that this Maze stored.
     * @return the list of Wall objects that this Maze stored.
     */
    public ArrayList<Wall> getWalls(){
        return this.walls;
    }

    /**
     * Gets Player inital position.
     * @return  an array of player initial position, having x coordinate
     *           in position[0] and y coordinate in position[1].
     */
    public int[] getPlayerPos(){
        return this.playerPos;
    }

    /**
     * Gets the list of Ghost object that this Maze stored.
     * @return the list of Ghost objects that this Maze stored.
     */
    public ArrayList<Ghost> getGhosts(){
        return this.ghosts;
    }

    // /**
    //  * Gets the JSONArray of modelength.
    //  * @return the modeLength of this Maze.
    //  */
    // public JSONArray getModeLength(){
    //     return this.modeLength;
    // }

    // /**
    //  * Gets the frighten time length for Ghost.
    //  * @return the frightenLength of this Maze.
    //  */
    // public long getFrightenLength(){
    //     return this.frightenLength;
    // }

    /**
     * Reads the JSON file and organising all the 
     * information.
     */
    public void readJson(){//read json file and extract the information
        JSONParser jsonParser = new JSONParser();
        try{
            FileReader reader = new FileReader("config.json");
            Object obj = jsonParser.parse(reader);
            JSONObject elementObj = (JSONObject) obj;

            this.mapFile = (String) elementObj.get("map");
            long livesLong = (long) elementObj.get("lives");
            this.lives = (int) livesLong;
            long speedLong = (long) elementObj.get("speed");
            this.speed = (int) speedLong;
            this.frightenLength = (long) elementObj.get("frightenedLength");
            this.modeLength = (JSONArray) elementObj.get("modeLengths");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the map file extracted from JSON file.
     * Assigns and creating Image object according to 
     * their corresponding number representation.
     * @param app       instance of PApplet for loading PImage
     */
    public void readFile(PApplet app){//read the map file extracted from the json file
        charaNum.put("1", "src/main/resources/horizontal.png");
        charaNum.put("2", "src/main/resources/vertical.png");
        charaNum.put("3", "src/main/resources/upLeft.png");
        charaNum.put("4", "src/main/resources/upRight.png");
        charaNum.put("5", "src/main/resources/downLeft.png");
        charaNum.put("6", "src/main/resources/downRight.png");
        charaNum.put("7", "src/main/resources/fruit.png");
        charaNum.put("8", "src/main/resources/superFruit.png");
        charaNum.put("9", "src/main/resources/sodaCan.png");
        charaNum.put("a", "src/main/resources/ambusher.png");
        charaNum.put("c", "src/main/resources/chaser.png");
        charaNum.put("i", "src/main/resources/ignorant.png");
        charaNum.put("w", "src/main/resources/whim.png");

        try{
            File file = new File(mapFile);
            Scanner scan = new Scanner(file);
            int y = 0;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                for (int x = 0; x < line.length(); x++) {
                    String symbol = Character.toString(line.charAt(x));
                    if (symbol.equals("p")){
                        playerPos[0] = x;
                        playerPos[1] = y;
                    }else if (Character.isLetter(symbol.charAt(0)) && charaNum.containsKey(symbol)){
                        PImage image= app.loadImage(charaNum.get(symbol));
                        Ghost ghost = null;
                        // Ghost chaser = null;
                        // int[] whimPos = null;
                        if (symbol.equals("a")){
                            ghost = new Ambusher(x * 16 - 6, y * 16 - 6, image, this.speed, this.modeLength, this.frightenLength);
                        }else if (symbol.equals("c")){
                            ghost = new Chaser(x * 16 - 6, y * 16 - 6, image, this.speed, this.modeLength, this.frightenLength);
                            chaser = ghost;
                            if (whimPos != null){
                                this.ghosts.add(ghost);
                                ghost = new Whim(whimPos[0], whimPos[1], app.loadImage(charaNum.get("w")), this.speed, this.modeLength, this.frightenLength, chaser);
                                this.ghosts.add(ghost);
                                continue;
                            }
                        }else if (symbol.equals("i")){
                            ghost = new Ignorant(x * 16 - 6, y * 16 - 6, image, this.speed, this.modeLength, this.frightenLength);
                        }else if (symbol.equals("w")){
                            if (chaser != null){
                                ghost = new Whim(x * 16 - 6, y * 16 - 6, image, this.speed, this.modeLength, this.frightenLength, chaser);
                            }else{
                                whimPos = new int[2];
                                whimPos[0] = x * 16 - 6;
                                whimPos[1] = y * 16 - 6;
                            }
                        }
                        if (ghost != null){
                            this.ghosts.add(ghost);
                        }
                    }else if (symbol.equals("0")){
                        continue;
                    }else {
                        if (charaNum.containsKey(symbol)){
                            if (symbol.equals("7")){
                                PImage image = app.loadImage(charaNum.get(symbol));
                                Fruit fruit = new Fruit(x * 16, y * 16, image);
                                this.fruits.add(fruit);
                            }else if (symbol.equals("8")) {
                                PImage image = app.loadImage(charaNum.get(symbol));
                                SuperFruit fruit = new SuperFruit(x * 16 - 8, y * 16 - 10, image);
                                this.fruits.add(fruit);
                            }else if (symbol.equals("9")){
                                PImage image = app.loadImage(charaNum.get(symbol));
                                SodaCan fruit = new SodaCan(x * 16, y * 16, image);
                                this.fruits.add(fruit);
                            }else{
                                PImage image = app.loadImage(charaNum.get(symbol));
                                Wall wall = new Wall(x * 16, y * 16, image);
                                this.walls.add(wall);
                            }
                        }
                    }
                }
                y ++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } 
    }
}
