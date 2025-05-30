public class dfsState {
    String word;
    int x, y;
    boolean[][] visited;

    dfsState(String word, int x, int y, boolean[][] visited) {
        this.word = word;
        this.x = x;
        this.y = y;
        this.visited = visited; // Initialize with an empty visited array
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
    public boolean[][] getVisited() {
        return visited;
    }
}
