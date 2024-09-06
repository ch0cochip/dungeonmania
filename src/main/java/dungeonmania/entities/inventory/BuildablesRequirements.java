package dungeonmania.entities.inventory;

public class BuildablesRequirements {
    public static final int WOOD_REQUIRED_FOR_BOW = 1;
    public static final int ARROWS_REQUIRED_FOR_BOW = 3;
    public static final int WOOD_REQUIRED_FOR_SHIELD = 2;
    public static final int TREASURE_REQUIRED_FOR_SHIELD = 1;
    public static final int KEYS_REQUIRED_FOR_SHIELD = 1;

    public static int getWoodRequiredForBow() {
        return WOOD_REQUIRED_FOR_BOW;
    }

    public static int getArrowsRequiredForBow() {
        return ARROWS_REQUIRED_FOR_BOW;
    }

    public static int getWoodRequiredForShield() {
        return WOOD_REQUIRED_FOR_SHIELD;
    }

    public static int getTreasureRequiredForShield() {
        return TREASURE_REQUIRED_FOR_SHIELD;
    }

    public static int getKeysRequiredForShield() {
        return KEYS_REQUIRED_FOR_SHIELD;
    }
}
