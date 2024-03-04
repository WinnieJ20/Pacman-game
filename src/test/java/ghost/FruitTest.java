package ghost;

import org.junit.jupiter.api.Test;

import processing.core.PImage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class FruitTest {
    public Fruit fruit;
    public SodaCan soda;
    SuperFruit superFruit;

    @BeforeEach
    public void setup(){
        PImage sprite = new PImage(16, 16);
        this.fruit = new Fruit(200, 240, sprite);
        this.soda = new SodaCan(200, 230, sprite);
        this.superFruit = new SuperFruit(120, 260, sprite);
    }
    
    @Test
    public void resetTest(){
        PImage sprite = new PImage(16, 16);
        Fruit fruit = new Fruit(200, 240, sprite);
        fruit.isEaten = true;
        fruit.used = true;
        fruit.restart();
        assertFalse(fruit.isEaten);
        assertFalse(fruit.isUsed());
    }

    @Test
    public void typeTest(){
        assertTrue(soda.isSoda());
        assertFalse(soda.isSuperFruit());
        assertFalse(soda.used);
        assertFalse(soda.isEaten());
        assertTrue(superFruit.isSuperFruit());
        assertFalse(superFruit.isSoda());
        assertFalse(superFruit.isEaten());
        superFruit.setUsed();
        assertTrue(superFruit.isUsed());

        fruit.isEaten = true;
        assertFalse(fruit.isSuperFruit());
        assertFalse(fruit.isSoda());
        assertFalse(fruit.isUsed());
        assertTrue(fruit.isEaten());
    }

    @Test
    public void eatenTest(){
        PImage sprite = new PImage(24, 26);
        Image player = new Player(197, 220, sprite, 1, 3);
        soda.eaten(player);
        assertTrue(soda.isEaten);
    }
}
