package dungeonmania.goals;

import dungeonmania.Game;

public class GoalBoulders implements Goal {
    private String type;

    public GoalBoulders(String type) {
        this.type = type;
    }

    @Override
    public boolean achieved(Game game) {
        return game.getSwitchFromMap().stream().allMatch(s -> s.isActivated());
    }

    @Override
    public String toString(Game game) {
        return ":boulders";
    }
}
