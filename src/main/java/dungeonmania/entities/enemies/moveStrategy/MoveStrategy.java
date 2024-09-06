package dungeonmania.entities.enemies.moveStrategy;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public interface MoveStrategy {
    public Position moveStrategy(Game game, Position position, Enemy enemy, GameMap map, Player player,
            boolean isAdjacentToPlayer);

}
