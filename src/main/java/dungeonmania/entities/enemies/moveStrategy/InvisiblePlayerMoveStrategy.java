package dungeonmania.entities.enemies.moveStrategy;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class InvisiblePlayerMoveStrategy implements MoveStrategy {

    @Override
    public Position moveStrategy(Game game, Position position, Enemy enemy, GameMap map, Player player,
            boolean isAdjacentToPlayer) {
        Position nextPos;
        Random randGen = new Random();
        List<Position> pos = position.getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> map.canMoveTo(enemy, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            nextPos = position;
            // map.moveTo(enemy, nextPos);
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
            // map.moveTo(enemy, nextPos);
        }
        return nextPos;
    }

}
