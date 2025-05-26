import edu.princeton.cs.algs4.Queue;

public class QuickSort {
    /**
     * Returns a new queue that contains the given queues catenated together.
     *
     * The items in q2 will be catenated after all of the items in q1.
     */
    private static <Item extends Comparable> Queue<Item> catenate(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> catenated = new Queue<Item>();
        for (Item item : q1) {
            catenated.enqueue(item);
        }
        for (Item item: q2) {
            catenated.enqueue(item);
        }
        return catenated;
    }

    /** Returns a random item from the given queue. */
    private static <Item extends Comparable> Item getRandomItem(Queue<Item> items) {
        int pivotIndex = (int) (Math.random() * items.size());
        Item pivot = null;
        // Walk through the queue to find the item at the given index.
        for (Item item : items) {
            if (pivotIndex == 0) {
                pivot = item;
                break;
            }
            pivotIndex--;
        }
        return pivot;
    }

    /**
     * Partitions the given unsorted queue by pivoting on the given item.
     *
     * @param unsorted  A Queue of unsorted items
     * @param pivot     The item to pivot on
     * @param less      An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are less than the given pivot.
     * @param equal     An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are equal to the given pivot.
     * @param greater   An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are greater than the given pivot.
     */
    private static <Item extends Comparable> void partition(
            Queue<Item> unsorted, Item pivot,
            Queue<Item> less, Queue<Item> equal, Queue<Item> greater) {
        for (Item item : unsorted) {
            if (item.compareTo(pivot) < 0) {
                less.enqueue(item);
            } else if (item.compareTo(pivot) == 0) {
                equal.enqueue(item);
            } else {
                greater.enqueue(item);
            }
        }
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> quickSort(
            Queue<Item> items) {
        // Base case: if the queue is empty or has one item, it's already sorted.
        if (items.isEmpty() || items.size() == 1) {
            return items;
        }
        // Step 1: Choose a random pivot.
        Item pivot = getRandomItem(items);
        // Step 2: Partition the items into less, equal, and greater queues.
        Queue<Item> less = new Queue<>();
        Queue<Item> equal = new Queue<>();
        Queue<Item> greater = new Queue<>();
        partition(items, pivot, less, equal, greater);
        // Step 3: Recursively sort the less and greater queues.
        Queue<Item> sortedLess = quickSort(less);
        Queue<Item> sortedGreater = quickSort(greater);
        // Step 4: Combine the sorted less, equal, and greater queues.
        items = catenate(sortedLess, equal);
        items = catenate(items, sortedGreater);
        return items;
    }

    public static void main(String[] args) {
        Queue<Integer> items = new Queue<>();
        items.enqueue(3);
        items.enqueue(1);
        items.enqueue(4);
        items.enqueue(1);
        items.enqueue(5);
        items.enqueue(9);
        items.enqueue(2);
        items.enqueue(6);
        items.enqueue(5);
        items.enqueue(3);
        items.enqueue(5);

        Queue<Integer> sortedItems = quickSort(items);
        for (Integer item : sortedItems) {
            System.out.print(item + " ");
        }
    }
}
