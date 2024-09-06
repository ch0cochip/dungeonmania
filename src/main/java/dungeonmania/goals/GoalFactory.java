package dungeonmania.goals;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoalFactory {
    public static Goal createGoal(JSONObject jsonGoal, JSONObject config) {
        JSONArray subgoals;
        switch (jsonGoal.getString("goal")) {
            case "AND":
                subgoals = jsonGoal.getJSONArray("subgoals");
                return new GoalAnd("AND", createGoal(subgoals.getJSONObject(0), config),
                        createGoal(subgoals.getJSONObject(1), config));
            case "OR":
                subgoals = jsonGoal.getJSONArray("subgoals");
                return new GoalOr("OR", createGoal(subgoals.getJSONObject(0), config),
                        createGoal(subgoals.getJSONObject(1), config));
            case "exit":
                return new GoalExit("exit");
            case "boulders":
                return new GoalBoulders("boulders");
            case "treasure":
                int treasureGoal = config.optInt("treasure_goal", 1);
                return new GoalTreasure("treasure_goal", treasureGoal);
            case "enemies":
                int enemyGoal = config.optInt("enemy_goal", 1);
                return new GoalEnemies("enemy_goal", enemyGoal);
            default:
                return null;
        }
    }
}
