package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends Enemy implements Interactable {
    public static final int DEFAULT_SPAWN_INTERVAL = 0;

    public ZombieToastSpawner(Position position, int spawnInterval) {
        super(position, 0, 0);
    }

    public void spawn(Game game) {
        game.enableSpawnZombie(game, this);
    }

    @Override
    public void onDestroy(GameMap map) {
        map.getGame().unsubscribe(getId());
    }

    @Override
    public void interact(Player player, Game game) {
        player.getInventory().getWeapon().use(game);
        game.getMap().destroyEntity(this);
    }

    @Override
    public boolean isInteractable(Player player) {
        return Position.isAdjacent(player.getPosition(), getPosition()) && player.hasWeapon();
    }

    @Override
    public void move(Game game) {
        return;
    }
}
