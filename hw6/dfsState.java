public class dfsState {
    Trie.TrieNode node;
    String word;
    int x, y;
    boolean isBacktrack;

    dfsState(Trie.TrieNode node, String word, int x, int y, boolean isBacktrack) {
        this.node = node;
        this.word = word;
        this.x = x;
        this.y = y;
        this.isBacktrack = isBacktrack; // Initialize with an empty visited array
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String getWord() {
        return word;
    }
    public boolean isBacktrack() {
        return isBacktrack;
    }
}
