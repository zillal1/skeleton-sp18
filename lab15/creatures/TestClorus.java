package creatures;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

public class TestClorus {
    @Test
    public void testChoose() {
        Clorus c = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        // You can create new empties with new Empty();
        // Despite what the spec says, you cannot test for Plips nearby yet.
        // Sorry!

        Action action = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);
        assertEquals(expected, action);
        assertEquals(1.19, c.energy(), 0.001);

        surrounded.put(Direction.TOP, new Plip());
        surrounded.put(Direction.BOTTOM, new Empty());
        action = c.chooseAction(surrounded);
        expected = new Action(Action.ActionType.ATTACK, Direction.TOP);
        assertEquals(expected, action);
        assertEquals(2.19, c.energy(), 0.001);

        //以下测试过程是随机的
        surrounded.put(Direction.TOP, new Empty());
        action = c.chooseAction(surrounded);
        expected = new Action(Action.ActionType.REPLICATE, Direction.TOP);
        assertEquals(expected, action);
        assertEquals(1.095, c.energy(), 0.0001);

        surrounded.put(Direction.TOP, new Empty());
        surrounded.put(Direction.BOTTOM, new Empty());
        action = c.chooseAction(surrounded);
        expected = new Action(Action.ActionType.REPLICATE, Direction.TOP);
        assertEquals(expected, action);
    }
}
