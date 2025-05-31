import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;


public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    Boggle(String dictPath) {
        Boggle.dictPath = dictPath;
    }
    private static Trie trie;
    private static List<String> board;
    private static boolean[][] visited;
    private static Set<String> seen;
    private static PriorityQueue<String> solution = new PriorityQueue<>((a, b) -> {
        if (a.length() != b.length()) {
            return a.length() - b.length(); // Sort by length increasing
        }
        return b.compareTo(a);
    });
    //private static List<String> dict = new ArrayList<>();
    private static List<String> read(String boardFilePath) {
        File file = new File(boardFilePath);
        List<String> list = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.toLowerCase();
                list.add(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Board file not found: " + boardFilePath);
        }
        return list;
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
        board = Boggle.read(boardFilePath);
        List<String> dict = Boggle.read(dictPath);
        if (board.isEmpty() || dict.isEmpty()) {
            return new ArrayList<>();
        }
        // Initialize a Trie with the dictionary
        trie = new Trie(dict);
        seen = new HashSet<>();
        visited = new boolean[board.size()][board.get(0).length()];
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).length(); j++) {
                // Start DFS from each cell in the board
                if (!trie.search(Trie.root, board.get(i).charAt(j))) {
                    continue; // Skip if the first character is not in the Trie
                }
                visited[i][j] = true;
                dfs(i, j, "" + board.get(i).charAt(j), k, trie.root.children.get(board.get(i).charAt(j)));
                visited[i][j] = false; // Backtrack
            }
        }
        // Convert the priority queue to a list and return it
        List<String> solutionList = new ArrayList<>();
        while (!solution.isEmpty()) {
            solutionList.add(0, solution.poll());
        }
        return solutionList;
    }
    private static void dfs(int currentRow, int currentCol, String currentString,
                            int k, Trie.TrieNode node) {
        visited[currentRow][currentCol] = true; // Mark the current cell as visited
        if (trie.isComplete(node) && !seen.contains(currentString)) {
            seen.add(currentString);
            solution.add(currentString);
            // If the solution size exceeds k, remove the smallest element
            if (solution.size() > k) {
                solution.poll();
            }
        }
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) {
                    continue; // Skip the current cell
                }
                int newRow = currentRow + x;
                int newCol = currentCol + y;
                if (newRow < 0 || newRow >= board.size() || newCol < 0
                        || newCol >= board.get(newRow).length()) {
                    continue; // Skip out of bounds
                }
                String nextWord = currentString + board.get(newRow).charAt(newCol);
                if (trie.search(node, board.get(newRow).charAt(newCol))) {
                    // Check if the next word is not already visited
                    if (!visited[newRow][newCol]) {
                        visited[newRow][newCol] = true; // Mark as visited
                        dfs(newRow, newCol, nextWord, k, node.children.get(board.get(newRow).charAt(newCol)));
                        visited[newRow][newCol] = false;
                    }
                }
            }
        }
    }
}
