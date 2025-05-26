package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    public MazeCycles(Maze m) {
        super(m);

    }

    @Override
    public void solve() {
        // Start DFS from vertex 0 (or any other vertex)
        announce();
        for (int v = 0; v < maze.V(); v++) {
            if (!marked[v]) {
                dfs(v, -1); // -1 indicates no parent for the starting vertex
            }
        }
    }

    // Helper methods go here
    private void dfs(int v, int parent) {
        marked[v] = true;
        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(w, v);
            } else if (w != parent) {
                // Found a cycle
                edgeTo[w] = v;
                announce();
                int x = v;
                while (x != w) {
                    announce();;
                    x = edgeTo[x];
                }
                return;
            }
        }
    }
}

