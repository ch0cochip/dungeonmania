package dungeonmania.entities.enemies;

import dungeonmania.battles.BattleStatistics;

public class HydraHealthStrategy implements HealthStrategy {

    private Hydra hydra;

    public HydraHealthStrategy(Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public void healthStrategy(BattleStatistics target, double damageOnTarget) {
        if (hydra.randomIncreaseHealth()) {
            target.setHealth(target.getHealth() + Hydra.DEFAULT_HEALTH_INCREASE_AMOUNT);
        } else {
            target.setHealth(target.getHealth() - damageOnTarget);
        }
    }
}
