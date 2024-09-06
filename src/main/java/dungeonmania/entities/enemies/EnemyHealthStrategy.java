package dungeonmania.entities.enemies;

import dungeonmania.battles.BattleStatistics;

public class EnemyHealthStrategy implements HealthStrategy {

    @Override
    public void healthStrategy(BattleStatistics target, double damageOnTarget) {
        target.setHealth(target.getHealth() - damageOnTarget);
    }
}
