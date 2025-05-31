import java.util.HashMap;
import java.util.List;

public class Trie {
    public class TrieNode {
        public final HashMap<Character, TrieNode> children;
        boolean isEndOfWord;

        public TrieNode() {
            children = new HashMap<>();
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
            if (current.children.get(c) == null) {
                current.children.put(c, new TrieNode());
            }
            current = current.children.get(c);
        }
        current.isEndOfWord = true;
    }
    public boolean isComplete(TrieNode node) {
        return node.isEndOfWord; // Check if it's a complete word
    }
    public boolean search(TrieNode node, char c) {
        return (node.children.get(c) != null); // Check if the character exists in the children
    }
}
