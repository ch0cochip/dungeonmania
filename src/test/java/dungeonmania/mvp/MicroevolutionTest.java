package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MicroevolutionTest {
    @Test
    @Tag("16-1")
    @DisplayName("Test achieving a basic enemies goal - destroy one spider and one zombie toast spawner")
    public void oneSpiderAndOneZombieToastSpawner() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_microevolutionTest_oneSpiderAndOneZombieToastSpawner",
                "c_microevolutionTest_oneSpiderAndOneZombieToastSpawner");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // check number of spider
        assertTrue(TestUtils.countEntityOfType(res.getEntities(), "spider") == 1);

        // check number of zombie toast spawner
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // move player down to meet spider (battle)
        res = dmc.tick(Direction.DOWN);

        // check number of spider (spider is destroyed)
        assertTrue(TestUtils.countEntityOfType(res.getEntities(), "spider") == 0);

        // move player to right to pick up weapon (sword)
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // move player to right to be cardinally adjacent to spawner, and destroy spawner
        res = dmc.tick(Direction.RIGHT);

        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));

        // check number of zombie toast spawner should be zero
        assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));

        // check number of enemies
        assertEquals(0, TestUtils.countType(res, "zombie_toast"));
        assertEquals(0, TestUtils.countType(res, "spider"));
        assertEquals(0, TestUtils.countType(res, "mercenary"));

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-2")
    @DisplayName("Test achieving a basic enemies goal - destroy one spider")
    public void enemies() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_microevolutionTest_oneSpider", "c_microevolutionTest_oneSpider");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // check number of spider
        assertTrue(TestUtils.countEntityOfType(res.getEntities(), "spider") == 1);

        // move player down to meet spider (battle)
        res = dmc.tick(Direction.DOWN);

        // check number of spider (spider is destroyed)
        assertTrue(TestUtils.countEntityOfType(res.getEntities(), "spider") == 0);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-3")
    @DisplayName("Test achieving a basic enemies goal - destroy one zombie toast spawner")
    public void oneZombieToastSpawner() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_microevolutionTest_oneZombieToastSpawner",
                "c_microevolutionTest_oneZombieToastSpawner");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // check number of zombie toast spawner
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // move player to right to pick up weapon (sword)
        res = dmc.tick(Direction.RIGHT);

        // move player to right to be cardinally adjacent to spawner, and destroy spawner
        res = dmc.tick(Direction.RIGHT);

        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));

        // check number of zombie toast spawner should be zero
        assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));

        // check number of enemies
        assertEquals(0, TestUtils.countType(res, "zombie_toast"));
        assertEquals(0, TestUtils.countType(res, "spider"));
        assertEquals(0, TestUtils.countType(res, "mercenary"));

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }
}
