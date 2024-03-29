/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ghost;

import org.junit.jupiter.api.Test;

import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class AppTest {
    App app;

    @BeforeEach
    public void setupEnvironment(){
        this.app = new App();
        //PApplet.runSketch(new String[]{"App"}, app);
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(15000);
        
    }

    @AfterEach
    public void closeWindow(){
        app.noLoop();
    }

    @Test 
    public void simpleTest() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest);
    }

    @Test
    public void setupTest(){
        assertFalse(app.getManager().getMap().getFruits().isEmpty());
        assertNotNull(app.getManager().getPlayer());
        assertFalse(app.getManager().getMap().getGhosts().isEmpty());
        assertFalse(app.getManager().getMap().getWalls().isEmpty());
    }

    @Test
    public void testGameEnd(){
        for (Fruit f : app.getManager().getMap().getFruits()){
            f.isEaten = true;
        }
        assertTrue(app.getManager().isGameEnd());
        assertTrue(app.getManager().isPlayerWin());
        app.getManager().restart();
    }

    @Test
    public void debugTest(){

    }

}
