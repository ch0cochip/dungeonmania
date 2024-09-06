package dungeonmania.entities.enemies;

import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.enemies.moveStrategy.InvinciblePlayerMoveStrategy;
import dungeonmania.entities.enemies.moveStrategy.InvisiblePlayerMoveStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Hydra extends ZombieToast {

    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;
    public static final double DEFAULT_HEALTH_INCREASE_RATE = 0.5;
    public static final double DEFAULT_HEALTH_INCREASE_AMOUNT = 1.0;

    private double healthIncreaseRate = Hydra.DEFAULT_HEALTH_INCREASE_RATE;
    private double healthIncreaseAmount = Hydra.DEFAULT_HEALTH_INCREASE_AMOUNT;

    public Hydra(Position position, double health, double attack, double healthIncreaseRate,
            double healthIncreaseAmount) {
        super(position, health, attack);
        this.healthIncreaseRate = healthIncreaseRate;
        this.healthIncreaseAmount = healthIncreaseAmount;
        getBattleStatistics().setHealthStrategy(new HydraHealthStrategy(this));
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        if (map.getPotionInPlayer() instanceof InvincibilityPotion) {
            nextPos = new InvinciblePlayerMoveStrategy().moveStrategy(game, getPosition(), this, map, game.getPlayer(),
                    false);
        } else {
            nextPos = new InvisiblePlayerMoveStrategy().moveStrategy(game, getPosition(), this, map, game.getPlayer(),
                    false);
        }
        map.moveTo(this, nextPos);
    }

    public boolean randomIncreaseHealth() {
        Random random = new Random();
        double result = random.nextInt(100);
        return result >= healthIncreaseRate * 100;
    }

}
