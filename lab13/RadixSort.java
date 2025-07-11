/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        String[] sorted = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            sorted[i] = asciis[i];
        }
        int max = 0;
        for (int i = 0; i < asciis.length; i++) {
            max = Math.max(max, asciis[i].length());
        }
        for (int i = max - 1; i >= 0; i--) {
            int counts[] = new int[10];
            int starts[] = new int[10];
            String[] next = new String[asciis.length];
            for (int j = 0; j < asciis.length; j++) {
                counts[sorted[j].charAt(i) - '0']++;
            }
            int pos = 0;
            for (int j = 0; j < 10; j++) {
                starts[j] = pos;
                pos += counts[j];
            }
            for (int j = 0; j < asciis.length; j++) {
                int index = sorted[j].charAt(i) - '0';
                next[starts[index]] = sorted[j];
                starts[index]++;
            }
            for (int j = 0; j < asciis.length; j++) {
                sorted[j] = next[j];
            }
        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        return;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
