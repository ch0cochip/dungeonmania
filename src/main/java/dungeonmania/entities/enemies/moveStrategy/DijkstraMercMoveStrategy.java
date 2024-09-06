package dungeonmania.entities.enemies.moveStrategy;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class DijkstraMercMoveStrategy implements MoveStrategy {

    @Override
    public Position moveStrategy(Game game, Position position, Enemy enemy, GameMap map, Player player,
            boolean isAdjacentToPlayer) {
        Position nextPos;
        nextPos = map.dijkstraPathFind(position, player.getPosition(), enemy);
        return nextPos;
    }

}
