package dungeonmania.entities.logical;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.entities.Wire;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class LightBulb extends Entity {
    private boolean activated;
    private String logic;
    private List<Switch> subs = new ArrayList<>();
    private List<Wire> wires = new ArrayList<>();

    public LightBulb(Position position, String logic) {
        super(position);
        this.activated = false;
        this.logic = logic;
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

    private void activate(GameMap map) {
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
            activated = numAdjacentActivated == 1;
            break;
        case "or":
            activated = numAdjacentActivated >= 1;
            break;
        case "and":
        case "co_and": // combine "and" and "co_and" cases
            activated = numAdjacentActivated >= 2;
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
            activated = numAdjacentActivated > 0;
            break;
        case "or":
            activated = numAdjacentActivated >= 1;
            break;
        case "and":
        case "co_and":
            activated = numAdjacentActivated >= 2;
            break;
        default:
            activated = true;
            break;
        }
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
