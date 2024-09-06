package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

public class Shield extends Buildable {
    private int durability;
    private double defence;

    public static final int DEFAULT_HEALTH = 0;
    public static final int DEFAULT_ATTACK = 0;
    public static final int ATTACK_MAGNIFIER = 1;
    public static final int DAMAGE_REDUCER = 1;

    public Shield(int durability, double defence) {
        super(null);
        this.durability = durability;
        this.defence = defence;
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            // game.getPlayer().remove(this);
            game.removeInventoryItemFromPlayer(this);
        }
    }

    // jov
    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin,
                new BattleStatistics(DEFAULT_HEALTH, DEFAULT_ATTACK, defence, ATTACK_MAGNIFIER, DAMAGE_REDUCER));
    }

    @Override
    public int getDurability() {
        return durability;
    }

}
