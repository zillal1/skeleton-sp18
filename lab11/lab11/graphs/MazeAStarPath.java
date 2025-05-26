package lab11.graphs;

import edu.princeton.cs.algs4.MinPQ;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    //private SearchNode start;
    //private SearchNode target;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        //start = new SearchNode(s, 0, h(s));
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int targetX = maze.toX(t);
        int targetY = maze.toY(t);
        int vX = maze.toX(v);
        int vY = maze.toY(v);
        return Math.abs(targetX - vX) + Math.abs(targetY - vY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }
    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        MinPQ<Integer>pq = new MinPQ<> ((v1, v2) -> {;
            int dist1 = distTo[v1] + h(v1);
            int dist2 = distTo[v2] + h(v2);
            return Integer.compare(dist1, dist2);
        });
        pq.insert(s);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            marked[v] = true;
            announce();

            if (v == t) {
                targetFound = true;
                break; // Stop if target is found
            }

            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    announce();
                    distTo[w] = distTo[v] + 1;
                    pq.insert(w); // Insert into priority queue
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

