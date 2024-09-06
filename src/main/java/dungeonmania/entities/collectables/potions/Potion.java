package dungeonmania.entities.collectables.potions;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Collectable;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Potion extends Collectable implements BattleItem {
    private int duration;

    // jov: should i store it in potion.java or collectables
    public static final int DEFAULT_HEALTH = 0;
    public static final int DEFAULT_ATTACK = 0;
    public static final int DEFAULT_DEFENCE = 0;
    public static final int DEFAULT_ATTACK_MAGNIFIER = 1;
    public static final int DEFAULT_DAMAGE_REDUCER = 1;

    public Potion(Position position, int duration) {
        super(position);
        this.duration = duration;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void use(Game game) {
        return;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return origin;
    }

    @Override
    public int getDurability() {
        return 1;
    }

    public static int getDefaultHealth() {
        return DEFAULT_HEALTH;
    }

    public static int getDefaultAttack() {
        return DEFAULT_ATTACK;
    }

    public static int getDefaultDefence() {
        return DEFAULT_DEFENCE;
    }

    public static int getDefaultAttackMagnifier() {
        return DEFAULT_ATTACK_MAGNIFIER;
    }

    public static int getDefaultDamageReducer() {
        return DEFAULT_DAMAGE_REDUCER;
    }
}
