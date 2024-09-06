package dungeonmania.entities.enemies.moveStrategy;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class InvinciblePlayerMoveStrategy implements MoveStrategy {

    @Override
    public Position moveStrategy(Game game, Position position, Enemy enemy, GameMap map, Player player,
            boolean isAdjacentToPlayer) {
        Position nextPos;
        Position plrDiff = Position.calculatePositionBetween(map.getPlayer().getPosition(), position);

        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(position, Direction.RIGHT)
                : Position.translateBy(position, Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(position, Direction.UP)
                : Position.translateBy(position, Direction.DOWN);
        Position offset = position;
        if (plrDiff.getY() == 0 && map.canMoveTo(enemy, moveX))
            offset = moveX;
        else if (plrDiff.getX() == 0 && map.canMoveTo(enemy, moveY))
            offset = moveY;
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            if (map.canMoveTo(enemy, moveX))
                offset = moveX;
            else if (map.canMoveTo(enemy, moveY))
                offset = moveY;
            else
                offset = position;
        } else {
            if (map.canMoveTo(enemy, moveY))
                offset = moveY;
            else if (map.canMoveTo(enemy, moveX))
                offset = moveX;
            else
                offset = position;
        }
        nextPos = offset;
        return nextPos;
        // map.moveTo(enemy, nextPos);
    }

}
