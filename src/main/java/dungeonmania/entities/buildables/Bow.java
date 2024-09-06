package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

public class Bow extends Buildable {
    private int durability;

    public static final int DEFAULT_HEALTH = 0;
    public static final int DEFAULT_ATTACK = 0;
    public static final int DEFAULT_DEFENCE = 0;

    public Bow(int durability) {
        super(null);
        this.durability = durability;
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            // game.getPlayer().remove(this);
            game.removeInventoryItemFromPlayer(this);
        }
    }

    // jov: should i make the constants and store it in Buildables
    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin,
                new BattleStatistics(DEFAULT_HEALTH, DEFAULT_ATTACK, DEFAULT_DEFENCE, 2, 1));
    }

    @Override
    public int getDurability() {
        return durability;
    }
}
