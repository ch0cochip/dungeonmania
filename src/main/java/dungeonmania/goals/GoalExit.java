package dungeonmania.goals;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Exit;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class GoalExit implements Goal {
    private String type;

    public GoalExit(String type) {
        this.type = type;
    }

    @Override
    public boolean achieved(Game game) {
        Player character = game.getPlayer();
        Position pos = character.getPosition();
        List<Exit> es = game.getExitFromMap();
        if (es == null || es.size() == 0)
            return false;
        return es.stream().map(Entity::getPosition).anyMatch(pos::equals);
    }

    @Override
    public String toString(Game game) {
        return ":exit";
    }

}
