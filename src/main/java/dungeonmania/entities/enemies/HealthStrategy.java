package dungeonmania.entities.enemies;

import dungeonmania.battles.BattleStatistics;

public interface HealthStrategy {
    public void healthStrategy(BattleStatistics target, double damageOnTarget);

}
