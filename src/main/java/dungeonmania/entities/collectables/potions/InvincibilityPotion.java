package dungeonmania.entities.collectables.potions;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {
    public static final int DEFAULT_DURATION = 8;

    public static final int DEFAULT_HEALTH = 0;
    public static final int DEFAULT_ATTACK = 0;
    public static final int DEFAULT_DEFENCE = 0;

    public InvincibilityPotion(Position position, int duration) {
        super(position, duration);
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(getDefaultHealth(), getDefaultAttack(),
                getDefaultDefence(), getDefaultAttackMagnifier(), getDefaultDamageReducer(), true, true));
    }
}
