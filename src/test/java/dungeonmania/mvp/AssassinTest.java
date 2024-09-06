package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AssassinTest {

    @Test
    @Tag("17-1")
    @DisplayName("Test assassin in line with Player moves towards them")
    public void simpleMovement() {
        // Wall Wall Wall Wall Wall Wall
        // P1 P2 P3 P4 M4 M3 M2 M1 . Wall
        // Wall Wall Wall Wall Wall Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_simpleMovement", "c_assassinTest_simpleMovement");

        assertEquals(new Position(8, 1), getAssPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getAssPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), getAssPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), getAssPos(res));
    }

    @Test
    @Tag("17-2")
    @DisplayName("Test assassin stops if they cannot move any closer to the player")
    public void stopMovement() {
        // Wall Wall Wall
        // P1 P2 Wall M1 Wall
        // Wall Wall Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_stopMovement", "c_assassinTest_stopMovement");

        Position startingPos = getAssPos(res);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(startingPos, getAssPos(res));
    }

    @Test
    @Tag("17-3")
    @DisplayName("Test assassins cannot move through closed doors")
    public void doorMovement() {
        // Wall Door Wall
        // P1 P2 Wall M1 Wall
        // Key Wall Wall Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_doorMovement", "c_assassinTest_doorMovement");

        Position startingPos = getAssPos(res);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(startingPos, getAssPos(res));
    }

    @Test
    @Tag("17-4")
    @DisplayName("Test assassin moves around a wall to get to the player")
    public void evadeWall() {
        // Wall M2
        // P1 P2 Wall M1
        // Wall M2
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_evadeWall", "c_assassinTest_evadeWall");

        res = dmc.tick(Direction.RIGHT);
        assertTrue(new Position(4, 1).equals(getAssPos(res)) || new Position(4, 3).equals(getAssPos(res)));
    }

    @Test
    @Tag("17-5")
    @DisplayName("Testing a assassin can be bribed with a certain amount")
    public void bribeAmount() {
        // Wall Wall Wall Wall Wall
        // P1 P2/Treasure P3/Treasure P4/Treasure M4 M3 M2 M1 Wall
        // Wall Wall Wall Wall Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_bribeAmount", "c_assassinTest_bribeAmount");

        String assId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // pick up first treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(7, 1), getAssPos(res));

        // attempt bribe
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // pick up second treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(6, 1), getAssPos(res));

        // attempt bribe
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        // pick up third treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(5, 1), getAssPos(res));

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(assId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // assassin becomes allied
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(4, 1), getAssPos(res));
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(4, 2), getAssPos(res));
    }

    @Test
    @Tag("17-6")
    @DisplayName("Testing a assassin can be bribed within a radius")
    public void bribeRadius() {
        // Wall Wall Wall Wall Wall
        // P1 P2/Treasure P3 P4 M4 M3 M2 M1 Wall
        // Wall Wall Wall Wall Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_bribeRadius", "c_assassinTest_bribeRadius");

        String assId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(7, 1), getAssPos(res));

        // attempt bribe
        assertDoesNotThrow(() -> dmc.interact(assId));
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @Tag("17-7")
    @DisplayName("Testing an allied assassin does not battle the player")
    public void allyBattle() {
        // Wall Wall Wall
        // P1 P2/Treasure . M2 M1 Wall
        // Wall Wall Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_allyBattle", "c_assassinTest_allyBattle");

        String assId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(assId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // walk into assassin, a battle does not occur
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
    }

    @Test
    @Tag("17-8")
    @DisplayName("Testing a assassin is bribed next to the player, then follow the player")
    public void allyMovementStick() {
        /**
         * W W W W W W E
         * W T P - - M -
         * W W W W W W -
         *
         * bribe_radius = 100
         * bribe_amount = 1
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_allyMovementStick", "c_assassinTest_allyMovementStick");

        String assId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(4, 1), getAssPos(res));

        // Wait until the mercenary is next to the player
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(3, 1), getAssPos(res));
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(2, 1), getAssPos(res));

        // achieve bribe - success
        res = assertDoesNotThrow(() -> dmc.interact(assId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(2, 1), getAssPos(res));

        // Ally follows the player
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(2, 1), getAssPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), getPlayerPos(res));
        assertEquals(new Position(1, 1), getAssPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), getPlayerPos(res));
        assertEquals(new Position(2, 1), getAssPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), getPlayerPos(res));
        assertEquals(new Position(3, 1), getAssPos(res));
    }

    @Test
    @Tag("17-9")
    @DisplayName("Testing an allied assassin finds the player, then follow the player")
    public void allyMovementFollow() {
        /**
         * W W W - W W W W W E
         * P T W - - - - M W -
         * - W W - W W W W W -
         *
         * bribe_radius = 100
         * bribe_amount = 1
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_allyMovementFollow", "c_assassinTest_allyMovementFollow");

        String assId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(6, 1), getAssPos(res));

        // achieve bribe - success
        res = assertDoesNotThrow(() -> dmc.interact(assId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(5, 1), getAssPos(res));

        // Mercenary uses dijkstra to find the player
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(4, 1), getAssPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(3, 1), getAssPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(3, 2), getAssPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(3, 3), getAssPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(2, 3), getAssPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(1, 3), getAssPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(0, 3), getAssPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(0, 2), getAssPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(0, 1), getAssPos(res));

        // Ally follows the player
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(0, 1), getPlayerPos(res));
        assertEquals(new Position(1, 1), getAssPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(0, 2), getPlayerPos(res));
        assertEquals(new Position(0, 1), getAssPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(0, 3), getPlayerPos(res));
        assertEquals(new Position(0, 2), getAssPos(res));
    }

    @Test
    @Tag("17-10")
    @DisplayName("Test invisibility potions cause assassins to move randomly")
    public void invisibilityAssassinMovement() throws InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_invisiblePlayer",
                "c_assassinTest_invisiblePlayer");

        assertEquals(1, TestUtils.getEntities(res, "invisibility_potion").size());
        assertEquals(0, TestUtils.getInventory(res, "invisibility_potion").size());
        assertEquals(1, TestUtils.getEntities(res, "assassin").size());

        // pick up invisibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "invisibility_potion").size());
        assertEquals(1, TestUtils.getInventory(res, "invisibility_potion").size());

        // consume invisibility potion
        res = dmc.tick(TestUtils.getFirstItemId(res, "invisibility_potion"));
        assertEquals(0, TestUtils.getInventory(res, "invisibility_potion").size());
        assertEquals(0, TestUtils.getEntities(res, "invisibility_potion").size());

        // check that distance between assassin and player does not always
        // decrease over time
        Position playerPos = getPlayerPos(res);
        Position assPos = getAssPos(res);
        int currentMagnitude = (int) Math.floor(TestUtils.getEuclideanDistance(playerPos, assPos));
        boolean movedAway = false;

        for (int i = 0; i <= 10; i++) {
            dmc.tick(Direction.DOWN);
            assPos = getAssPos(res);
            int endingMagnitude = (int) Math.floor(TestUtils.getEuclideanDistance(playerPos, assPos));
            if (endingMagnitude >= currentMagnitude) {
                movedAway = true;
            }
            currentMagnitude = endingMagnitude;
        }
        assertTrue(movedAway);
    }

    @Test
    @Tag("17-11")
    @DisplayName("Test player battles assassin and player dies")
    public void testPlayerDiesWhenBattleMercenary() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = TestUtils.genericMercenarySequence(controller,
                "c_assassinTest_basicAssassinPlayerDies");
        List<EntityResponse> entities = postBattleResponse.getEntities();

        assertTrue(TestUtils.countEntityOfType(entities, "player") == 0);
    }

    @Test
    @Tag("17-12")
    @DisplayName("Test player battles assassin and assassin dies")
    public void testMercenariyDiesWhenBattle() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = TestUtils.genericMercenarySequence(controller,
                "c_assassinTest_basicAssassinAssassinDies");
        List<EntityResponse> entities = postBattleResponse.getEntities();

        assertTrue(TestUtils.countEntityOfType(entities, "assassin") == 0);
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }

    private Position getAssPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "assassin").get(0).getPosition();
    }

    public void assertBattleCalculations(BattleResponse battle, boolean enemyDies, String configFilePath,
            String enemyType) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = battle.getInitialPlayerHealth(); // Should come from config
        double enemyHealth = battle.getInitialEnemyHealth(); // Should come from config
        double playerAttack = Double.parseDouble(TestUtils.getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double
                .parseDouble(TestUtils.getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(-enemyAttack / 10, round.getDeltaCharacterHealth(), 0.001);
            assertEquals(-playerAttack / 5, round.getDeltaEnemyHealth(), 0.001);
            // Delta health is negative
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }
}
