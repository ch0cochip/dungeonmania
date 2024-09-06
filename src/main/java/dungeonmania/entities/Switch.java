package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.logical.LightBulb;
import dungeonmania.entities.logical.SwitchDoor;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends Entity implements Overlap, MovedAway {
    private boolean activated;
    private List<Bomb> bombs = new ArrayList<>();

    private List<LightBulb> lightBulbs = new ArrayList<>();
    private List<Wire> wires = new ArrayList<>();
    private List<SwitchDoor> switchDoors = new ArrayList<>();

    public Switch(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER));
    }

    public void subscribe(Bomb b) {
        bombs.add(b);
    }

    public void subscribe(Bomb bomb, GameMap map) {
        bombs.add(bomb);
        if (activated) {
            bombs.stream().forEach(b -> b.notify(map));
        }
    }

    public void unsubscribe(Bomb b) {
        bombs.remove(b);
    }

    // LightBulb
    public void subscribe(LightBulb lightBulb) {
        lightBulbs.add(lightBulb);
    }

    public void subscribe(LightBulb lightBulb, GameMap map) {
        lightBulbs.add(lightBulb);
        if (activated) {
            lightBulb.notify(map, activated);
        }
    }

    public void unsubscribe(LightBulb lightBulb) {
        lightBulbs.remove(lightBulb);
    }

    // Wire
    public void subscribe(Wire wire) {
        wires.add(wire);
    }

    public void subscribe(Wire wire, GameMap map) {
        wires.add(wire);
        if (activated) {
            wire.notify(map, activated);
        }
    }

    public void unsubscribe(Wire wire) {
        wires.remove(wire);
    }

    // SwitchDoor
    public void subscribe(SwitchDoor switchDoor) {
        switchDoors.add(switchDoor);
    }

    public void subscribe(SwitchDoor switchDoor, GameMap map) {
        switchDoors.add(switchDoor);
        if (activated) {
            switchDoor.notify(map, activated);
        }
    }

    public void unsubscribe(SwitchDoor switchDoor) {
        switchDoors.remove(switchDoor);
    }

    // done :)

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        System.out.println(wires.size());
        switch (entity.getClass().getSimpleName()) {
        case "Boulder":
            activated = true;
            bombs.forEach(b -> b.notify(map));
            wires.forEach(w -> w.notify(map, activated));
            lightBulbs.forEach(l -> l.notify(map, activated));
            switchDoors.forEach(s -> s.notify(map, activated));
            break;
        default:
            // Do nothing
            break;
        }
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        switch (entity.getClass().getSimpleName()) {
        case "Boulder":
            activated = false;
            wires.forEach(w -> w.notify(map, activated));
            lightBulbs.forEach(l -> l.notify(map, activated));
            switchDoors.forEach(s -> s.notify(map, activated));
            break;
        default:
            // Do nothing
            break;
        }
    }

    public boolean isActivated() {
        return activated;
    }
}
