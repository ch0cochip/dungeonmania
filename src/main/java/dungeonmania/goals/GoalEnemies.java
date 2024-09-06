package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class GoalEnemies implements Goal {
    private String type;
    private int target;

    public GoalEnemies(String type, int target) {
        this.type = type;
        this.target = target;
    }

    @Override
    public boolean achieved(Game game) {
        int target = this.target;
        int numCurrentEnemy = game.getEntityType(Enemy.class).size();
        int numInitialEnemy = game.getNumInitialEnemies();
        return (numInitialEnemy - numCurrentEnemy >= target)
                && game.getEntityType(ZombieToastSpawner.class).isEmpty();
    }

    @Override
    public String toString(Game game) {
        return ":enemies";
    }
}
