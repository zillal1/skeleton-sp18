import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;


public class Boggle {

    // File path of dictionary file
    static String dictPath = "words.txt";
    Boggle(String dictPath) {
        Boggle.dictPath = dictPath;
    }
    private static Trie trie;
    private static List<String> board;
    private static boolean[][] visited;
    private static Stack<dfsState> stack;
    private static int M;
    private static int N;
    private static char[][] boradArray;
    //private static List<String> dict = new ArrayList<>();
    private static List<String> read(String boardFilePath) {
        In inputFile = new In(boardFilePath);
        if (!inputFile.exists()) {
            throw new IllegalArgumentException("<UNK>");
        }
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
    private static void checkIsRectangular(List<String> board) {
        if (board.isEmpty()) {
            return; // An empty board is considered rectangular
        }
        int rowLength = board.get(0).length();
        for (String row : board) {
            if (row.length() != rowLength) {
                throw new IllegalArgumentException("Board is not rectangular");
            }
        }
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
        if (k < 0) {
            throw new IllegalArgumentException("k must be non-negative");
        }
        board = Boggle.read(boardFilePath);
        checkIsRectangular(board);
        M = board.size();
        N = board.get(0).length();
        boradArray = new char [M][N];
        for (int i = 0; i < M; i++) {
            boradArray[i] = board.get(i).toCharArray();
        }
        List<String> dict = Boggle.read(dictPath);
        if (board.isEmpty() || dict.isEmpty()) {
            return new ArrayList<>();
        }
        // Initialize a Trie with the dictionary
        trie = new Trie(dict);
        Set<String> seen = new HashSet<>();
        PriorityQueue<String> solution = new PriorityQueue<>((a, b) -> {
            if (a.length() != b.length()) {
                return a.length() - b.length(); // Sort by length increasing
            }
            return b.compareTo(a);
        });
        visited = new boolean[board.size()][board.get(0).length()];
        stack = new Stack<>();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                // Start DFS from each cell in the board
                if (!trie.search(trie.root, boradArray[i][j])) {
                    continue; // Skip if the first character is not in the Trie
                }
                stack.add(new dfsState(trie.root.children.get(boradArray[i][j]),
                        "" + boradArray[i][j], i, j, false));
                helper(k, solution, seen);
            }
        }
        // Convert the priority queue to a list and return it
        List<String> solutionList = new ArrayList<>();
        while (!solution.isEmpty()) {
            solutionList.add(0, solution.poll());
        }
        return solutionList;
    }
    private static void helper(int k, PriorityQueue<String> solution, Set<String> seen) {
        while (!stack.isEmpty()) {
            dfsState state = stack.pop();
            Trie.TrieNode node = state.node;
            String currentString = state.word;
            int currentRow = state.x;
            int currentCol = state.y;
            boolean isBacktrack = state.isBacktrack;

            if (isBacktrack) {
                visited[currentRow][currentCol] = false; // Unmark the cell
                continue; // Skip backtracking states
            }
            visited[currentRow][currentCol] = true;
            stack.add(new dfsState(node, currentString, currentRow, currentCol, true));
            if (trie.isComplete(node) && currentString.length() >= 3
                    && !seen.contains(currentString)) {
                solution.add(currentString);
                seen.add(currentString);
                if (solution.size() > k) {
                    solution.poll(); // Remove the shortest word if we exceed k
                }
            }

            // Explore neighbors
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == 0 && y == 0) {
                        continue; // Skip the current cell
                    }
                    int newRow = currentRow + x;
                    int newCol = currentCol + y;
                    if (newRow < 0 || newRow >= M || newCol < 0
                            || newCol >= N) {
                        continue; // Skip out of bounds
                    }
                    char nextChar = boradArray[newRow][newCol];
                    if (trie.search(node, nextChar) && !visited[newRow][newCol]) {
                        String nextWord = currentString + nextChar;
                        stack.add(new dfsState(node.children.get(nextChar), nextWord,
                                newRow, newCol, false));
                    }
                }
            }
        }
    }
}
