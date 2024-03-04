package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MazeTest {

    @Test
    public void livesTest(){
        Maze map = new Maze();
        map.setLives(3);
        assertEquals(3, map.getLives());
    }

    @Test
    public void getTest(){
        Maze map = new Maze();
        assertEquals(0, map.getSpeed());
        assertTrue(map.getFruits().isEmpty());
        assertTrue(map.getWalls().isEmpty());
        assertEquals(0, map.getPlayerPos()[0]);
        assertEquals(0, map.getPlayerPos()[1]);
        assertTrue(map.getGhosts().isEmpty());
    }

    @Test
    public void jsonTest(){
        Maze map = new Maze();
        map.readJson();
        assertEquals(1, map.getSpeed());
        assertEquals(3, map.getLives());
    }
}
