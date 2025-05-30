import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    Boggle (String dictPath) {
        Boggle.dictPath = dictPath;
    }
    //private static List<String> dict = new ArrayList<>();
    private static List<String> read(String boardFilePath) {
        File file = new File(boardFilePath);
        List<String> board = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.toLowerCase();
                board.add(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Board file not found: " + boardFilePath);
        }
        return board;
    }

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // YOUR CODE HERE
        List<String> board = Boggle.read(boardFilePath);
        List<String> dict = Boggle.read(dictPath);
        if (board.isEmpty() || dict.isEmpty()) {
            return new ArrayList<>();
        }
        // Initialize a Trie with the dictionary
        Trie trie = new Trie(dict);
        Set<String> seen = new HashSet<>();
        PriorityQueue<String> solution = new PriorityQueue<>((a, b) -> {
            if (a.length() != b.length()) {
                return b.length() - a.length(); // Sort by length descending
            }
            return a.compareTo(b); // Sort alphabetically ascending
        });
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).length(); j++) {
                // Start DFS from each cell in the board
                boolean[][] visited = new boolean[board.size()][board.get(i).length()];
                //StringBuilder currentWord = new StringBuilder();
                Stack<dfsState> stack = new Stack<>();
                if (!trie.search("" + board.get(i).charAt(j))) {
                    continue; // Skip if the first character is not in the Trie
                }
                visited[i][j] = true;
                stack.add(new dfsState("" + board.get(i).charAt(j), i, j, visited));
                while (!stack.isEmpty()) {
                    dfsState state = stack.pop();
                    String currentString = state.getWord();
                    if (trie.isComplete(currentString)) {
                        if (!seen.contains(currentString)) {
                            seen.add(currentString);
                            solution.add(currentString);
                            if (solution.size() > k) {
                                solution.poll(); // Keep only the top k words
                            }
                        }
                    }
                    int currentRow = state.getX();
                    int currentCol = state.getY();
                    // Explore neighbors
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            if (x == 0 && y == 0) continue; // Skip the current cell
                            int newRow = ((currentRow + x) + board.size()) % board.size();
                            int newCol = ((currentCol + y) + board.get(0).length()) % board.get(newRow).length();
                            String nextWord = currentString + board.get(newRow).charAt(newCol);
                            if (trie.search(nextWord)) {
                                // Check if the next word is not already visited
                                if (!visited[newRow][newCol]) {
                                    visited[newRow][newCol] = true; // Mark as visited
                                    stack.add(new dfsState(nextWord, newRow, newCol, visited));
                                    visited[newRow][newCol] = false; // Unmark for other paths
                                }
                            }
                        }
                    }
                }
                visited[i][j] = false;
            }
        }
        // Convert the priority queue to a list and return it
        List<String> solutionList = new ArrayList<>();
        while (!solution.isEmpty()) {
            solutionList.add(solution.poll());
        }
        return solutionList;
    }
}