package ghost;

import processing.core.PImage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class PlayerTest {
    public Player player;
    public ArrayList<Wall> walls;

    @BeforeEach
    public void setup(){
        PImage sprite = new PImage(24, 26);
        this.player = new Player(300, 100, sprite, 1, 3);
        //create a list of wall that will not collide with the player and also
        // not in the next frame future.
        PImage wallImage = new PImage(16, 16);
        walls = new ArrayList<>();
        walls.add(new Wall(100, 100, wallImage));
        walls.add(new Wall(200, 100, wallImage));
        walls.add(new Wall(100, 140, wallImage));
        walls.add(new Wall(156, 392, wallImage));
    }
    @Test
    public void testRestart(){
        player.isEaten = true;
        player.current = Direction.DOWN;
        player.xvel = 1;
        player.yvel = 1;
        player.x = 400;
        player.y = 300;
        player.restart();
        assertFalse(player.isEaten);
        assertNull(player.current);
        assertEquals(0, player.xvel);
        assertEquals(0, player.yvel);
        assertEquals(300, player.x);
        assertEquals(100, player.y);
    }

    @Test
    public void eatenTest(){
        PImage sprite = new PImage(28, 28);
        Image ghost = new Wall(302, 104, sprite); 
        // since it's testing for collide and only needs x and y coordinate, 
        // will use Wall object as Ghost for simplification.
        player.eaten(ghost);
        assertTrue(player.isEaten);

    }

    @Test
    public void alivedTest(){
        assertTrue(player.isAlived());
        assertEquals(3, player.getLives());
        this.player = new Player(300, 100, null, 1, 0);
        assertFalse(player.isAlived());
    }

    @Test
    public void addDirection(){
        player.addCommand(Direction.RIGHT);
        assertNull(player.current);
        assertTrue(player.getNextDirection() == Direction.RIGHT);
    }

    @Test
    public void wallAheadTest(){
        assertFalse(player.checkWallAhead(walls));
        // add wall that will collide with the player;
        PImage sprite = new PImage(16, 16);
        walls.add(new Wall(299, 107, sprite));
        player.current = Direction.UP;
        assertTrue(player.checkWallAhead(walls));
        assertTrue(player.y == 104);
        player.y = 100;
        player.current = Direction.DOWN;
        assertTrue(player.checkWallAhead(walls));
        assertTrue(player.y == 96);
        player.x = 300;
        player.current = Direction.LEFT;
        assertTrue(player.checkWallAhead(walls));
        assertTrue(player.x == 304);
        player.x = 300;
        player.current = Direction.RIGHT;
        assertTrue(player.checkWallAhead(walls));
        assertTrue(player.x == 296);
    }

    @Test
    public void wallNextTest(){
        player.current = Direction.UP;
        assertFalse(player.checkWallforNext(walls));
        player.addCommand(Direction.RIGHT);
        assertFalse(player.checkWallforNext(walls));
        player.addCommand(Direction.LEFT);
        assertFalse(player.checkWallforNext(walls));
        player.current = Direction.RIGHT;
        player.addCommand(Direction.DOWN);
        assertFalse(player.checkWallforNext(walls));
        player.addCommand(Direction.UP);
        assertFalse(player.checkWallforNext(walls));

        //add a wall that will collide with the player in the next future frame.
        PImage sprite = new PImage(16, 16);
        walls.add(new Wall(315, 90, sprite));
        player.current = Direction.LEFT;
        player.addCommand(Direction.UP);
        assertTrue(player.checkWallforNext(walls));

        walls.add(new Wall(280, 115, sprite));
        player.current = Direction.DOWN;
        player.addCommand(Direction.LEFT);
        assertTrue(player.checkWallforNext(walls));
    }

    @Test
    public void inputByUser(){
        player.goUp();
        assertTrue(player.xvel == 0);
        assertTrue(player.yvel == -1);

        player.goDown();
        assertTrue(player.xvel == 0);
        assertTrue(player.yvel == 1);

        player.goLeft();
        assertTrue(player.xvel == -1);
        assertTrue(player.yvel == 0);

        player.goRight();
        assertTrue(player.xvel == 1);
        assertTrue(player.yvel == 0);
    }
}
