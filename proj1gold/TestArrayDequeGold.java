import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testStudentArrayDeque() {
        StudentArrayDeque<Integer> testArray = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> stdArray = new ArrayDequeSolution<>();
        String log = "";
        for (int i = 0; i < 1000; i++) {
            if (stdArray.isEmpty()) {
                int addNumber = StdRandom.uniform(10);
                int headOrBack = StdRandom.uniform(2);
                if (headOrBack == 0) {
                    log += "addFirst(" + addNumber + ")\n";
                    testArray.addFirst(addNumber);
                    stdArray.addFirst(addNumber);
                } else {
                    log += "addLast(" + addNumber + ")\n";
                    testArray.addLast(addNumber);
                    stdArray.addLast(addNumber);
                }
            } else {
                int x = StdRandom.uniform(4);
                int addNumber = StdRandom.uniform(1000);
                Integer testremoveNumber = 1;
                Integer stdremoveNumber = 1;
                if (x == 0) {
                    log += "addFirst(" + addNumber + ")\n";
                    testArray.addFirst(addNumber);
                    stdArray.addFirst(addNumber);
                } else if (x == 1) {
                    log += "addLast(" + addNumber + ")\n";
                    testArray.addLast(addNumber);
                    stdArray.addLast(addNumber);
                } else if (x == 2) {
                    log += "removeFirst()\n";
                    testremoveNumber = stdArray.removeFirst();
                    stdremoveNumber = stdArray.removeFirst();
                } else {
                    log += "removeLast()\n";
                    testremoveNumber = stdArray.removeLast();
                    stdremoveNumber = stdArray.removeLast();
                }
                assertEquals(log, stdremoveNumber, testremoveNumber);
            }
        }
    }
}
