package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.logical.LightBulb;
import dungeonmania.entities.logical.SwitchDoor;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Wire extends Entity {
    private boolean activated;
    private List<Switch> subs = new ArrayList<>();
    private List<LightBulb> lightBulbs = new ArrayList<>();
    private List<Wire> wires = new ArrayList<>();
    private List<SwitchDoor> switchDoors = new ArrayList<>();

    public Wire(Position position) {
        super(position);
        this.activated = false;
    }

    public void subscribe(Switch s) {
        this.subs.add(s);
    }

    public void subscribe(LightBulb l) {
        this.lightBulbs.add(l);
    }

    public void subscribe(Wire w) {
        if (!this.equals(w)) {
            this.wires.add(w);
        }
    }

    public void subscribe(SwitchDoor sd) {
        this.switchDoors.add(sd);
    }

    public void notify(GameMap map, boolean isActivated) {
        if (isActivated && !activated) {
            activate(map);
            onActivate(map);
        } else if (!isActivated && activated) {
            deactivate(map);
            onDeactivate(map);
        }
    }

    private void activate(GameMap map) {
        activated = true;
    }

    private void deactivate(GameMap map) {
        activated = false;
    }

    private void onActivate(GameMap map) {
        wires.forEach(w -> w.notify(map, activated));
        lightBulbs.forEach(l -> l.notify(map, activated));
        switchDoors.forEach(s -> s.notify(map, activated));
    }

    private void onDeactivate(GameMap map) {
        wires.forEach(w -> w.notify(map, activated));
        lightBulbs.forEach(l -> l.notify(map, activated));
        switchDoors.forEach(s -> s.notify(map, activated));
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

}
