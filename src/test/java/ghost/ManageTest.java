package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class ManageTest {
    GameManage manager;

    @BeforeEach
    public void setup(){
        this.manager = new GameManage();
    }
    @Test
    public void debugMode(){
        assertFalse(manager.isDebug());
        manager.change();
        assertTrue(manager.isDebug());
        manager.change();
        assertFalse(manager.isDebug());
    }
}
