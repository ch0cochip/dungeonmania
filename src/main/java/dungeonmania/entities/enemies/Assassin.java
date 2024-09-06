package dungeonmania.entities.enemies;

import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.enemies.moveStrategy.DijkstraMercMoveStrategy;
import dungeonmania.entities.enemies.moveStrategy.InvinciblePlayerMoveStrategy;
import dungeonmania.entities.enemies.moveStrategy.InvisiblePlayerMoveStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Assassin extends Mercenary {
    public static final int DEFAULT_BRIBE_AMOUNT = 3;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 7.0;
    public static final double DEFAULT_HEALTH = 10.0;
    public static final double DEFAULT_BRIBE_FAIL_RATE = 0.5;

    private int bribeAmount = Assassin.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Assassin.DEFAULT_BRIBE_RADIUS;
    private double bribeFailRate = Assassin.DEFAULT_BRIBE_FAIL_RATE;

    // private boolean allied = false;
    private boolean isAdjacentToPlayer = false;

    public Assassin(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence, double bribeFailRate) {
        super(position, health, DEFAULT_ATTACK, bribeAmount, DEFAULT_BRIBE_RADIUS, allyAttack, allyDefence);
        this.bribeAmount = bribeAmount;
        this.bribeFailRate = bribeFailRate;
    }

    // public boolean isAllied() {
    // return allied;
    // }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (isAllied() || ((Player) entity).isInvisible())
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current assx can be bribed
     *
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        Random random = new Random();
        double result = random.nextInt(100);
        return result >= bribeFailRate * 100 && bribeRadius >= 0
                && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }

    /**
     * bribe the assassin
     */
    protected void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }
    }

    @Override
    public void interact(Player player, Game game) {
        if (isInteractable(player)) {
            this.setAllied();
            if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), getPosition()))
                isAdjacentToPlayer = true;
        }

        bribe(player);
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        Player player = game.getPlayer();
        if (this.isAllied()) {
            nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition()
                    : new DijkstraMercMoveStrategy().moveStrategy(game, getPosition(), this, map,
                            player,
                            isAdjacentToPlayer);
            if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(),
                    nextPos))
                isAdjacentToPlayer = true;
            map.moveTo(this, nextPos);
        } else if (map.getPotionInPlayer() instanceof InvisibilityPotion) {
            nextPos = new InvisiblePlayerMoveStrategy().moveStrategy(game, getPosition(),
                    this, map, player,
                    isAdjacentToPlayer);
        } else if (map.getPotionInPlayer() instanceof InvincibilityPotion) {
            nextPos = new InvinciblePlayerMoveStrategy().moveStrategy(game,
                    getPosition(), this, map, player,
                    isAdjacentToPlayer);
        } else {
            // Follow hostile
            nextPos = new DijkstraMercMoveStrategy().moveStrategy(game, getPosition(),
                    this, map, player,
                    isAdjacentToPlayer);
        }
        map.moveTo(this, nextPos);
    }

    @Override
    public boolean isInteractable(Player player) {
        return !this.isAllied() && canBeBribed(player);
    }
}
