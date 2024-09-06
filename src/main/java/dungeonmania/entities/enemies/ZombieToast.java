package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.enemies.moveStrategy.InvinciblePlayerMoveStrategy;
import dungeonmania.entities.enemies.moveStrategy.InvisiblePlayerMoveStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
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
}
