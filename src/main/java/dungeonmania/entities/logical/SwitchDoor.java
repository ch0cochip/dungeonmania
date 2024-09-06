package dungeonmania.entities.logical;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.entities.Wire;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwitchDoor extends Entity {
    private boolean activated;
    private String logic;
    private List<Switch> subs = new ArrayList<>();
    private List<Wire> wires = new ArrayList<>();

    public SwitchDoor(Position position, String logic) {
        super(position);
        this.activated = false;
        this.logic = logic;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return activated || entity instanceof Spider;
    }

    public void subscribe(Switch s) {
        this.subs.add(s);
    }

    public void subscribe(Wire w) {
        this.wires.add(w);
    }

    public void notify(GameMap map, boolean isActivated) {
        if (isActivated) {
            activate(map);
        } else {
            deactivate(map);
        }
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    private void activate(GameMap map) {
        int numAdjacentActivated = 0;

        numAdjacentActivated += wires.stream()
                .filter(w -> Position.isAdjacent(this.getPosition(), w.getPosition()) && w.isActivated()).count();

        numAdjacentActivated += subs.stream()
                .filter(s -> Position.isAdjacent(this.getPosition(), s.getPosition()) && s.isActivated()).count();

        switch (logic) {
        case "xor":
            activated = numAdjacentActivated == 1;
            break;
        case "or":
            activated = numAdjacentActivated >= 1;
            break;
        case "and":
            activated = numAdjacentActivated >= 2;
            break;
        case "co_and":
            activated = false;
            break;
        default:
            activated = false;
            break;
        }
    }

    private void deactivate(GameMap map) {
        int numAdjacentActivated = 0;

        for (Wire w : wires) {
            if (Position.isAdjacent(this.getPosition(), w.getPosition()) && w.isActivated()) {
                numAdjacentActivated++;
            }
        }

        for (Switch s : subs) {
            if (Position.isAdjacent(this.getPosition(), s.getPosition()) && s.isActivated()) {
                numAdjacentActivated++;
            }
        }

        switch (logic) {
        case "xor":
            if (numAdjacentActivated == 0) {
                activated = false;
            } else {
                activated = true;
            }
            break;
        case "or":
            if (numAdjacentActivated < 1) {
                activated = false;
            } else {
                activated = true;
            }
            break;
        case "and":
        case "co_and":
            if (numAdjacentActivated < 2) {
                activated = false;
            } else {
                activated = true;
            }
            break;
        default:
            activated = false;
            break;
        }
    }
}
