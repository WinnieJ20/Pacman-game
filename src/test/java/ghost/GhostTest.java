package ghost;

import org.junit.jupiter.api.Test;

import processing.core.PImage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;

public class GhostTest {
    Ghost chaser;
    Ghost ambusher;
    Ghost ignorant;
    Ghost whim;
    ArrayList<Wall> walls;

    @BeforeEach
    public void setUp(){
        PImage sprite = new PImage(28, 28);
        this.chaser = new Chaser(200, 300, sprite, 1, null, 5);
        this.ambusher = new Ambusher(100, 300, sprite, 1, null, 5);
        this.ignorant = new Ignorant(159, 400, sprite, 1, null, 5);
        this.whim = new Whim(200, 149, sprite, 1, null, 5, chaser);
        walls = new ArrayList<>();
        PImage wallsprite = new PImage(16, 16);
        walls.add(new Wall(215, 290, wallsprite)); // wall at the up of chaser
    }

    @Test
    public void ghostEatenTest(){
        PImage sprite = new PImage(24, 26);
        Image player_chaser = new Player(204, 295, sprite, 1, 3);
        Image player_ambusher = new Player(96, 295, sprite, 1, 3);
        Image player_ignorant = new Player(166, 396, sprite, 1, 3);
        Image player_whim = new Player(204, 142, sprite, 1, 3);

        //not being eaten if not in frightened mode.
        chaser.eaten(player_chaser);
        assertFalse(chaser.isEaten);

        ambusher.eaten(player_ambusher);
        assertFalse(ambusher.isEaten);

        ignorant.eaten(player_ignorant);
        assertFalse(ignorant.isEaten);

        whim.eaten(player_whim);
        assertFalse(whim.isEaten);

        chaser.setMode("frighten");
        ambusher.setMode("frighten");
        ignorant.setMode("frighten");
        whim.setMode("frighten");

        chaser.eaten(player_chaser);
        assertTrue(chaser.isEaten);

        ambusher.eaten(player_ambusher);
        assertTrue(ambusher.isEaten);

        ignorant.eaten(player_ignorant);
        assertTrue(ignorant.isEaten);

        whim.eaten(player_whim);
        assertTrue(whim.isEaten);
    }

    @Test
    public void restartTest(){
        chaser.x = 140;
        chaser.y = 349;
        chaser.setMode("frighten");
        chaser.isEaten = true;
        chaser.current = Direction.RIGHT;
        chaser.restart();
        assertEquals(200, chaser.x);
        assertEquals(300, chaser.y);
        assertEquals("scatter", chaser.getMode());
        assertFalse(chaser.isEaten);
        assertEquals(Direction.LEFT, chaser.current);
    }

    @Test
    public void debugTest(){
        int[] positions = ambusher.debug();
        assertEquals(114, positions[0]);
        assertEquals(314, positions[1]);
        assertEquals(448, positions[2]);
        assertEquals(0, positions[3]);

        int[] c_positions = chaser.debug();
        assertEquals(214, c_positions[0]);
        assertEquals(314, c_positions[1]);
        assertEquals(0, c_positions[2]);
        assertEquals(0, c_positions[3]);

        int[] i_positions = ignorant.debug();
        assertEquals(173, i_positions[0]);
        assertEquals(414, i_positions[1]);
        assertEquals(0, i_positions[2]);
        assertEquals(576, i_positions[3]);

        int[] w_positions = whim.debug();
        assertEquals(214, w_positions[0]);
        assertEquals(163, w_positions[1]);
        assertEquals(448, w_positions[2]);
        assertEquals(576, w_positions[3]);
    }

    @Test
    public void wallAroundTest(){
        assertNull(chaser.checkWallAround(null));
        PImage sprite = new PImage(16, 16);
        ArrayList<Direction> wallAround = new ArrayList<>();
        wallAround.add(Direction.RIGHT);
        wallAround.add(Direction.UP);
        assertTrue(wallAround.containsAll(chaser.checkWallAround(walls)));
        walls.add(new Wall(215, 320, sprite)); // blocking wall in down direction
        wallAround.add(Direction.DOWN);
        assertTrue(wallAround.containsAll(chaser.checkWallAround(walls)));

        chaser.current = Direction.UP;
        chaser.previous = Direction.UP;
        wallAround.remove(0);
        walls.remove(1);
        wallAround.add(Direction.DOWN);
        assertTrue(wallAround.containsAll(chaser.checkWallAround(walls)));
    
        chaser.current = Direction.DOWN;
        chaser.previous = Direction.DOWN;
        walls.remove(0);
        walls.add(new Wall(220, 314, sprite)); // blocking wall at the right

        ArrayList<Direction> wallAround_new = new ArrayList<>();
        wallAround_new.add(Direction.UP);
        wallAround_new.add(Direction.RIGHT);
        assertTrue(wallAround_new.containsAll(chaser.checkWallAround(walls)));
    }
    
    @Test
    public void directionDistanceTest(){
        HashMap<Direction, Double> result = chaser.direcDist();
        assertEquals(119584.0, result.get(Direction.LEFT));
        assertEquals(141984.0, result.get(Direction.RIGHT));
        assertEquals(113984.0, result.get(Direction.UP));
        assertEquals(147584.0, result.get(Direction.DOWN));
    }

    @Test
    public void directionNoWall(){
        ArrayList<Direction> hasWall = new ArrayList<>();
        hasWall.add(Direction.LEFT);
        hasWall.add(Direction.RIGHT);

        ArrayList<Direction> result = chaser.directionNoWalls(hasWall);
        assertTrue(result.contains(Direction.UP));
        assertTrue(result.contains(Direction.DOWN));
        assertFalse(result.contains(Direction.LEFT));
        assertFalse(result.contains(Direction.RIGHT));

        chaser.current = Direction.UP;
        chaser.previous = Direction.LEFT;
        hasWall.remove(1);
        ArrayList<Direction> result_2 = chaser.directionNoWalls(hasWall);
        assertTrue(result_2.contains(Direction.UP));
        assertFalse(result_2.contains(Direction.DOWN));
        assertFalse(result_2.contains(Direction.LEFT));
        assertFalse(result_2.contains(Direction.RIGHT));
    }

    @Test
    public void moveTest(){
        ArrayList<Direction> noWall = new ArrayList<>();
        whim.current = Direction.RIGHT;
        whim.move(noWall);
        assertEquals(Direction.RIGHT, whim.previous);

        noWall.add(Direction.RIGHT);
        chaser.move(noWall);
        assertEquals(Direction.RIGHT, chaser.current);
        assertEquals(Direction.LEFT, chaser.previous);

        noWall.add(Direction.UP); 
        chaser.move(noWall);
        assertEquals(Direction.UP, chaser.current); // up has a shorter distance to the target
        assertEquals(Direction.RIGHT, chaser.previous);
    }

    @Test
    public void invisibleTest(){
        whim.setInvisible();
        assertTrue(whim.isInvisble);
    }

    @Test
    public void targetSetingTest(){
        ambusher.setTarget();
        assertEquals(448, ambusher.targetX);
        assertEquals(0, ambusher.targetY);

        chaser.setTarget();
        assertEquals(0, chaser.targetX);
        assertEquals(0, chaser.targetY);

        ignorant.setTarget();
        assertEquals(0, ignorant.targetX);
        assertEquals(576, ignorant.targetY);

        whim.setTarget();
        assertEquals(448, whim.targetX);
        assertEquals(576, whim.targetY);

        PImage sprite = new PImage(24, 26);
        Player player = new Player(30, 489, sprite, 1, 3);
        player.current = Direction.UP;
        ambusher.setTarget(player);
        assertEquals(30, ambusher.targetX);
        assertEquals(425, ambusher.targetY);

        chaser.setTarget(player);
        assertEquals(30, chaser.targetX);
        assertEquals(489, chaser.targetY);

        ignorant.setTarget(player);
        assertEquals(30, ignorant.targetX);
        assertEquals(489, ignorant.targetY);

        whim.setTarget(player);
        assertEquals(0, whim.targetX);
        assertEquals(576, whim.targetY);

        Player closerPlayer = new Player(140, 380, sprite, 1, 3);
        ignorant.setTarget(closerPlayer);
        assertEquals(0, ignorant.targetX);
        assertEquals(576, ignorant.targetY);

        player.current = Direction.DOWN;
        player.x = 170;
        player.y = 250;
        ambusher.setTarget(player);
        assertEquals(170, ambusher.targetX);
        assertEquals(314, ambusher.targetY);

        whim.setTarget(player);
        assertEquals(140, whim.targetX);
        assertEquals(264, whim.targetY);

        player.current = Direction.RIGHT;
        ambusher.setTarget(player);
        assertEquals(234, ambusher.targetX);
        assertEquals(250, ambusher.targetY);

        whim.setTarget(player);
        assertEquals(204, whim.targetX);
        assertEquals(200, whim.targetY);

        player.current = Direction.LEFT;
        ambusher.setTarget(player);
        assertEquals(106, ambusher.targetX);
        assertEquals(250, ambusher.targetY);

        whim.setTarget(player);
        assertEquals(76, whim.targetX);
        assertEquals(200, whim.targetY);

        player.x = 10;
        ambusher.setTarget(player);
        assertEquals(0, ambusher.targetX);
        assertEquals(250, ambusher.targetY);

        player.current = Direction.RIGHT;
        player.x = 430;
        ambusher.setTarget(player);
        assertEquals(448, ambusher.targetX);
        assertEquals(250, ambusher.targetY);

        player.current = Direction.DOWN;
        player.y = 570;
        ambusher.setTarget(player);
        assertEquals(430, ambusher.targetX);
        assertEquals(576, ambusher.targetY);

        player.current = Direction.UP;
        player.y = 40;
        ambusher.setTarget(player);
        assertEquals(430, ambusher.targetX);
        assertEquals(0, ambusher.targetY);

        chaser.isEaten = true;
        whim.setTarget(player);
        assertEquals(430, whim.targetX);
        assertEquals(40, whim.targetY);

        chaser.isEaten = false;
        player.current = Direction.RIGHT;
        whim.setTarget(player);
        assertEquals(448, whim.targetX);
        assertEquals(0, whim.targetY);
    }
}
