package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.BattleResponse;
// import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HydraTest {
    @Test
    @Tag("18-1")
    @DisplayName("Testing hydras movement")
    public void movement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_hydraTest_movement", "c_hydraTest_movement");

        assertEquals(1, getHydra(res).size());

        // Teams may assume that random movement includes choosing to stay still, so we
        // should just
        // check that they do move at least once in a few turns
        boolean hydraMoved = false;
        Position prevPosition = getHydra(res).get(0).getPosition();
        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.UP);
            if (!prevPosition.equals(getHydra(res).get(0).getPosition())) {
                hydraMoved = true;
                break;
            }
        }
        assertTrue(hydraMoved);
    }

    @Test
    @Tag("18-2")
    @DisplayName("Testing hydras cannot move through closed doors and walls")
    public void doorsAndWalls() {
        // W W W W
        // P W Z W
        // W D W
        // K
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_hydraTest_doorsAndWalls", "c_hydraTest_doorsAndWalls");
        assertEquals(1, getHydra(res).size());
        Position position = getHydra(res).get(0).getPosition();
        res = dmc.tick(Direction.UP);
        assertEquals(position, getHydra(res).get(0).getPosition());
    }

    @Test
    @Tag("18-3")
    @DisplayName("Test basic health calculations hydra - player loses")
    public void testHealthBelowZeroHydra() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = TestUtils.genericHydraSequence(controller,
                "c_hydraTest_basicHydraPlayerDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations(battle, false, "c_hydraTest_basicHydraPlayerDies", "hydra");
    }

    @Test
    @Tag("18-4")
    @DisplayName("Test basic health calculations hydra - hydra loses")
    public void testHealthBelowZeroPlayer() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = TestUtils.genericHydraSequence(controller,
                "c_hydraTest_basicHydraHydraDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations(battle, true, "c_hydraTest_basicHydraHydraDies", "hydra");
    }

    private List<EntityResponse> getHydra(DungeonResponse res) {
        return TestUtils.getEntities(res, "hydra");
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
