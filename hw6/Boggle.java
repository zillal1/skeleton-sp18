import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Boggle {

    static String dictPath = "words.txt";

    Boggle(String dictPath) {
        Boggle.dictPath = dictPath;
    }

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

    public static List<String> solve(int k, String boardFilePath) {
        List<String> board = read(boardFilePath);
        List<String> dict = read(dictPath);
        if (board.isEmpty() || dict.isEmpty()) {
            return new ArrayList<>();
        }

        Trie trie = new Trie(dict);
        Set<String> foundWords = new HashSet<>();

        // 为每行分配正确长度的visited数组
        boolean[][] visited = new boolean[board.size()][];
        for (int i = 0; i < board.size(); i++) {
            visited[i] = new boolean[board.get(i).length()];
        }

        // 使用StringBuilder避免字符串拼接开销
        StringBuilder wordBuilder = new StringBuilder();

        // 对每个起点进行独立的DFS
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).length(); j++) {
                char firstChar = board.get(i).charAt(j);
                if (trie.search(trie.root, firstChar)) {
                    wordBuilder.append(firstChar);
                    visited[i][j] = true;

                    dfsOptimized(board, i, j, trie.root.children.get(firstChar),
                            wordBuilder, visited, foundWords, trie);

                    visited[i][j] = false;
                    wordBuilder.deleteCharAt(wordBuilder.length() - 1);
                }
            }
        }

        // 批量处理结果，避免频繁的堆操作
        List<String> sortedWords = new ArrayList<>(foundWords);
        sortedWords.sort((a, b) -> {
            if (a.length() != b.length()) {
                return b.length() - a.length(); // 长度降序
            }
            return a.compareTo(b); // 字典序升序
        });

        return sortedWords.subList(0, Math.min(k, sortedWords.size()));
    }

    private static void dfsOptimized(List<String> board, int row, int col,
                                     Trie.TrieNode currentNode, StringBuilder wordBuilder,
                                     boolean[][] visited, Set<String> foundWords, Trie trie) {

        // 检查当前单词是否完整且长度>=3
        if (trie.isComplete(currentNode) && wordBuilder.length() >= 3) {
            foundWords.add(wordBuilder.toString());
        }

        // 探索8个方向（无环绕，根据你的代码逻辑）
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }

                int newRow = row + dx;
                int newCol = col + dy;

                // 边界检查
                if (newRow < 0 || newRow >= board.size()
                        || newCol < 0 || newCol >= board.get(newRow).length()) {
                    continue;
                }

                // 检查是否已访问
                if (visited[newRow][newCol]) {
                    continue;
                }

                char nextChar = board.get(newRow).charAt(newCol);

                // 检查Trie中是否有这个字符的路径
                if (!trie.search(currentNode, nextChar)) {
                    continue;
                }

                // 递归探索
                wordBuilder.append(nextChar);
                visited[newRow][newCol] = true;

                dfsOptimized(board, newRow, newCol, currentNode.children.get(nextChar),
                        wordBuilder, visited, foundWords, trie);

                // 回溯
                visited[newRow][newCol] = false;
                wordBuilder.deleteCharAt(wordBuilder.length() - 1);
            }
        }
    }
}
