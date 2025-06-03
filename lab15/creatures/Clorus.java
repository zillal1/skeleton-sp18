package creatures;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;

public class Clorus extends Creature {

    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

    /** creates clorus with energy equal to E. */
    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    /** creates a clorus with energy equal to 1. */
    public Clorus() {
        this(1);
    }

    /** Should return a color with red = 34, blue = 231, and green that varies
     *  linearly based on the energy of the Clorus. If the clorus has zero energy,
     *  it should have a green value of 0. If it has max energy, it should
     *  have a green value of 255. The green value should vary with energy
     *  linearly in between these two extremes.
     */
    public Color color() {
        r = 34;
        g = 0;
        b = 231;
        return color(r, g, b);
    }
    public void move() {
        energy -= 0.03;
        /*if (energy < 0) {
            energy = 0;
        }*/
    }
    public void stay() {
        energy -= 0.01;
    }
    public void attack(Creature c) {
        energy += c.energy();
    }
    public Clorus replicate() {
        Clorus offspring = new Clorus(energy / 2);
        energy /= 2;
        return offspring;
    }
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> emptySpaces = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");

        if (emptySpaces.isEmpty()) {
            stay();
            return new Action(Action.ActionType.STAY);
        } else if (!plips.isEmpty()) {
            Direction target = HugLifeUtils.randomEntry(plips);
            attack((Creature) neighbors.get(target));
            return new Action(Action.ActionType.ATTACK, target);
        } else if (energy >= 1) {
            Direction target = HugLifeUtils.randomEntry(emptySpaces);
            replicate();
            return new Action(Action.ActionType.REPLICATE, target);
        } else {
            Direction target = HugLifeUtils.randomEntry(emptySpaces);
            move();
            return new Action(Action.ActionType.MOVE, target);
        }
    }
}
