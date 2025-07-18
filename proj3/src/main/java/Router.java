import edu.princeton.cs.algs4.MinPQ;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {

    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long startNode = g.closest(stlon, stlat);
        long destNode = g.closest(destlon, destlat);
        class SearchNode implements Comparable<SearchNode> {
            long id;
            double gx;
            double hx;
            SearchNode parent;
            SearchNode(long id, SearchNode parent) {
                this.id = id;
                this.gx = g.distance(id, parent.id) + parent.gx;
                this.hx = g.distance(id, destNode);
                this.parent = parent;
            }
            SearchNode(long id) {
                this.id = id;
                this.gx = 0.0;
                this.hx = g.distance(id, destNode);
                this.parent = null;
            }

            @Override
            public int compareTo(SearchNode other) {
                return Double.compare(this.gx + this.hx, other.gx + other.hx);
            }
        }

        MinPQ<SearchNode> pq = new MinPQ<>();
        SearchNode start = new SearchNode(startNode);
        pq.insert(start);
        Set<Long> visited = new HashSet<>();
        LinkedList<Long> distTo = new LinkedList<>();
        Map<Long, SearchNode> nodes = new HashMap<>();
        nodes.put(startNode, start);
        while (!pq.isEmpty()) {
            SearchNode current = pq.delMin();
            if (visited.contains(current.id)) {
                continue;
            }
            visited.add(current.id);
            if (current.id == destNode) {
                while (current != null) {
                    distTo.addFirst(current.id);
                    current = current.parent;
                }
                break; // found the destination
            }
            for (long neighbor : g.adjacent(current.id)) {
                if (!visited.contains(neighbor)) {
                    if (!nodes.containsKey(neighbor)
                            || nodes.get(neighbor).gx > current.gx + g.distance(current.id, neighbor)) {
                        SearchNode neighborNode = new SearchNode(neighbor, current);
                        nodes.put(neighbor, neighborNode);
                        pq.insert(neighborNode);
                    }
                }
            }
        }
        return distTo;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> directions = new ArrayList<>();
        if (route.isEmpty()) {
            return directions; // no route, no directions
        }

        NavigationDirection currentDirection = new NavigationDirection();
        currentDirection.way = g.getEdgeName(route.get(0), route.get(1));
        currentDirection.distance = g.distance(route.get(0), route.get(1));
        currentDirection.direction = NavigationDirection.START;
        //directions.add(currentDirection);

        for (int i = 1; i < route.size() - 1; i++) {
            long fromNode = route.get(i);
            long toNode = route.get(i + 1);
            String nextWay = g.getEdgeName(fromNode, toNode);
            double distance = g.distance(fromNode, toNode);
            if (nextWay == null || !nextWay.equals(currentDirection.way)) {
                // we are changing ways
                if (currentDirection.distance > 0) {
                    directions.add(currentDirection);
                }
                currentDirection = new NavigationDirection();
                currentDirection.way = nextWay;
                currentDirection.distance = distance;
                // determine the direction
                double angle = g.angleBetweenNodes(route.get(i - 1), fromNode, toNode);
                if (angle > -15 && angle < 15) {
                    currentDirection.direction = NavigationDirection.STRAIGHT;
                } else if (angle >= 15 && angle < 30) {
                    currentDirection.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (angle <= -15 && angle > -30) {
                    currentDirection.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (angle >= 30 && angle < 100) {
                    currentDirection.direction = NavigationDirection.RIGHT;
                } else if (angle <= -30 && angle > -100) {
                    currentDirection.direction = NavigationDirection.LEFT;
                } else if (angle >= 100) {
                    currentDirection.direction = NavigationDirection.SHARP_RIGHT;
                } else if (angle <= -100) {
                    currentDirection.direction = NavigationDirection.SHARP_LEFT;
                }
            } else {
                currentDirection.distance += distance;
            }
        }
        // add the last direction
        if (currentDirection.distance > 0) {
            directions.add(currentDirection);
        }
        return directions;
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
