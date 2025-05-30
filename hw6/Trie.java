import java.util.List;

public class Trie {
    public class TrieNode {
        TrieNode[] children;
        boolean isEndOfWord;

        public TrieNode() {
            children = new TrieNode[266];
            isEndOfWord = false;
        }
    }
    public TrieNode root;
    public Trie(List<String> dict) {
        root = new TrieNode();
        for (String word : dict) {
            insert(word);
        }
    }
    private void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            int index = c;
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }
            current = current.children[index];
        }
        current.isEndOfWord = true;
    }
    public boolean isNext(TrieNode node, char c) {
        return node.children[c] != null;
    }
    public boolean isComplete(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            int index = c;
            if (current.children[index] == null) {
                return false; // Character not found
            }
            current = current.children[index];
        }
        return current.isEndOfWord; // Check if it's a complete word
    }
    public boolean search(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            int index = c;
            if (current.children[index] == null) {
                return false; // Character not found
            }
            current = current.children[index];
        }
        return true;
    }
}
