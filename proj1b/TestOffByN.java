import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    static OffByN offbyn = new OffByN(5);

    @Test
    public void testEqualChars() {
        // n = 5
        assertTrue(offbyn.equalChars('a', 'f'));
        assertTrue(offbyn.equalChars('f', 'a'));
        assertFalse(offbyn.equalChars('b', 'f'));
    }
}
