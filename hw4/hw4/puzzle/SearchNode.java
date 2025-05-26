package hw4.puzzle;

public class SearchNode implements Comparable<SearchNode> {
    private final WorldState state;
    private final int moves;
    private final SearchNode previous;

    public SearchNode(WorldState state, int moves, SearchNode previous) {
        this.state = state;
        this.moves = moves;
        this.previous = previous;
    }

    public WorldState getState() {
        return state;
    }

    public int getMoves() {
        return moves;
    }

    public SearchNode getPrevious() {
        return previous;
    }

    @Override
    public int compareTo(SearchNode other) {
        return Integer.compare(this.moves + this.state.estimatedDistanceToGoal(),
                other.moves + other.state.estimatedDistanceToGoal());
    }
}
