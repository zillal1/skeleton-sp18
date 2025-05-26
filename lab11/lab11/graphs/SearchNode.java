package lab11.graphs;

public class SearchNode implements Comparable<SearchNode> {
    private int vertex;
    private int distance;
    private int priority;

    public SearchNode(int vertex, int distance, int priority) {
        this.vertex = vertex;
        this.distance = distance;
        this.priority = priority;
    }

    public int getVertex() {
        return vertex;
    }

    public int getDistance() {
        return distance;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(SearchNode other) {
        return Integer.compare(this.priority, other.priority);
    }
}
