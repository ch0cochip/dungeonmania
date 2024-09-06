package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class LogicSwitchesTest {
    @Test
    @Tag("19-1")
    @DisplayName("test if lightBulb can light up basic1")
    public void testLightBulbLightUp() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitches_basicBulb", "c_logicSwitches_basicBulb");
        res = dmc.tick(Direction.RIGHT);
        List<EntityResponse> entities = res.getEntities();
        String type = entities.get(4).getType();
        assertEquals("light_bulb_on", type);
    }

    @Test
    @Tag("19-2")
    @DisplayName("test if lightBulb can light up basic2")
    public void testLightBulbUnlight() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitches_basicBulb", "c_logicSwitches_basicBulb");
        res = dmc.tick(Direction.RIGHT);
        List<EntityResponse> entities = res.getEntities();
        String type = entities.get(4).getType();
        assertEquals("light_bulb_on", type);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        entities = res.getEntities();
        type = entities.get(4).getType();
        assertEquals("light_bulb_off", type);
    }

    @Test
    @Tag("19-3")
    @DisplayName("test if switch door can open up")
    public void testOpenSwitchDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitches_basicSwitchDoor", "c_logicSwitches_basicSwitchDoor");
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 0), getPlayerPos(res));
    }

    @Test
    @Tag("19-4")
    @DisplayName("test if switch door can close back")
    public void testOpenCloseSwitchDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitches_basicSwitchDoor", "c_logicSwitches_basicSwitchDoor");
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(2, 1), getPlayerPos(res));
    }

    @Test
    @Tag("19-5")
    @DisplayName("test functionality of wire - basic1")
    public void testWireConnectingToLightBulb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitches_wire", "c_logicSwitches_wire");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getEntities(res, "light_bulb_off").size(), 0);
        assertEquals(TestUtils.getEntities(res, "light_bulb_on").size(), 1);
    }

    @Test
    @Tag("19-6")
    @DisplayName("test functionality of wire - basic2")
    public void testDisconnectingWire() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitches_wire", "c_logicSwitches_wire");
        res = dmc.tick(Direction.RIGHT);
        List<EntityResponse> entities = res.getEntities();
        String type = entities.get(3).getType();
        assertEquals("light_bulb_on", type);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        entities = res.getEntities();
        type = entities.get(3).getType();
        assertEquals("light_bulb_off", type);
    }

    @Test
    @Tag("19-7")
    @DisplayName("test OR lightbulbs")
    public void testOrLightBulb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitches_orBulb", "c_logicSwitches_orBulb");
        res = dmc.tick(Direction.RIGHT);
        List<EntityResponse> entities = res.getEntities();
        String type = entities.get(4).getType();
        assertEquals("light_bulb_on", type);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        entities = res.getEntities();
        type = entities.get(4).getType();
        assertEquals("light_bulb_on", type);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        entities = res.getEntities();
        type = entities.get(4).getType();
        assertEquals("light_bulb_on", type);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        entities = res.getEntities();
        type = entities.get(4).getType();
        assertEquals("light_bulb_off", type);
    }

    @Test
    @Tag("19-8")
    @DisplayName("testf AND lightbulbs")
    public void testAndLightBulb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitch_andBulb", "c_logicSwitch_andBulb");
        res = dmc.tick(Direction.RIGHT);
        List<EntityResponse> entities = res.getEntities();
        String type = entities.get(4).getType();
        assertEquals("light_bulb_off", type);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        entities = res.getEntities();
        type = entities.get(4).getType();
        assertEquals("light_bulb_on", type);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        entities = res.getEntities();
        type = entities.get(4).getType();
        assertEquals("light_bulb_off", type);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        entities = res.getEntities();
        type = entities.get(4).getType();
        assertEquals("light_bulb_off", type);
    }

    @Test
    @Tag("19-9")
    @DisplayName("test XOR lightbulbs")
    public void testXorLightBulb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitch_xorBulb", "c_logicSwitch_xorBulb");
        res = dmc.tick(Direction.RIGHT);
        List<EntityResponse> entities = res.getEntities();
        String type = entities.get(4).getType();
        assertEquals("light_bulb_on", type);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        entities = res.getEntities();
        type = entities.get(4).getType();
        assertEquals("light_bulb_off", type);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        entities = res.getEntities();
        type = entities.get(4).getType();
        assertEquals("light_bulb_on", type);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        entities = res.getEntities();
        type = entities.get(4).getType();
        assertEquals("light_bulb_off", type);
    }

    @Test
    @Tag("19-10")
    @DisplayName("test OR switch doors")
    public void testOrSwitchDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitch_orSwitchDoor", "c_logicSwitch_orSwitchDoor");
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(8, 2), getPlayerPos(res));
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }
}
