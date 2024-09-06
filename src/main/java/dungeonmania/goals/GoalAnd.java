package dungeonmania.goals;

import dungeonmania.Game;

public class GoalAnd implements Goal {
    private String type;
    private Goal goal1;
    private Goal goal2;

    public GoalAnd(String type, Goal goal1, Goal goal2) {
        this.type = type;
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    @Override
    public boolean achieved(Game game) {
        return goal1.achieved(game) && goal2.achieved(game);
    }

    @Override
    public String toString(Game game) {
        if (goal1.achieved(game))
            return "(" + goal2.toString(game) + ")";
        if (goal2.achieved(game))
            return "(" + goal1.toString(game) + ")";
        if (goal1.achieved(game) && goal2.achieved(game))
            return "";
        return "(" + goal1.toString(game) + " AND " + goal2.toString(game) + ")";
    }

}
