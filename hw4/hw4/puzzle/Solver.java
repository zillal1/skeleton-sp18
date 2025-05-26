package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Solver {
    private int moves = 0;
    private ArrayList<WorldState> solutions = new ArrayList<>();

    public Solver(WorldState initial) {
        /*if (initial == null) {
            throw new IllegalArgumentException("Initial state cannot be null");
        }
        if (initial.isGoal()) {
            return;
        }*/
        MinPQ<SearchNode> MP = new MinPQ<>();
        Set<WorldState> visited = new HashSet<>();
        MP.insert(new SearchNode(initial, 0, null));
        while (!MP.isEmpty()) {
            SearchNode current = MP.delMin();
            WorldState currentState = current.getState();
            visited.add(currentState);
            if (currentState.isGoal()) {
                moves = current.getMoves();
                do {
                    currentState = current.getState();
                    solutions.add(0, currentState);
                    current = current.getPrevious();
                } while (current != null);
                return;
            }
            for (WorldState neighbor : currentState.neighbors()) {
                if (!visited.contains(neighbor)) {
                    MP.insert(new SearchNode(neighbor, current.getMoves() + 1, current));
                }
            }
        }
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return solutions;
    }
}
