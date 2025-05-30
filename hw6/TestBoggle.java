import org.junit.Test;

import java.util.*;
import java.io.*;

import static org.junit.Assert.assertEquals;

public class TestBoggle {

    @Test
    public void solveBoggle() {
        String boardFilePath = "exampleBoard.txt";
        String dictionaryFilePath = "words.txt";
        int k = 7;
        Boggle solver = new Boggle(dictionaryFilePath);
        List<String> solution = solver.solve(k, boardFilePath);
        List<String> expected = Arrays.asList("thumbtacks", "thumbtack", "setbacks", "setback", "ascent", "humane", "smacks");
        assertEquals(expected, solution);
    }
}
