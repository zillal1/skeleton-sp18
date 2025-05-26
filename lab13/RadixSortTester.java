import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class RadixSortTester {
    @Test
    public void testRadixSort() {
        String[] input = {"123", "456", "789", "234", "567", "890"};
        String[] expected = {"123", "234", "456", "567", "789", "890"};
        String[] sorted = RadixSort.sort(input);
        assertArrayEquals(expected, sorted);
    }
}
