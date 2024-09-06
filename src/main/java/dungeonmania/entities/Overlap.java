package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface Overlap {
    public void onOverlap(GameMap map, Entity entity);
}
