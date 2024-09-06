package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface MovedAway {
    public void onMovedAway(GameMap map, Entity entity);
}
