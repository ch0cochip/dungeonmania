package dungeonmania.goals;

import dungeonmania.Game;

public class GoalTreasure implements Goal {
    private String type;
    private int target;

    public GoalTreasure(String type, int target) {
        this.type = type;
        this.target = target;
    }

    @Override
    public boolean achieved(Game game) {
        return game.getCollectedTreasureCount() >= target;
    }

    @Override
    public String toString(Game game) {
        return ":treasure";
    }
}
