import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    public static class Node {
        String id;
        double lat;
        double lon;
        String location;
        private List<String> neighbors = new ArrayList<>();
        private List<String> edges = new ArrayList<>();

        public Node(String id, double lat, double lon) {
            this.id  = id;
            this.lat = lat;
            this.lon = lon;
            this.location = "";
        }

        public void connect(String neighbor) {
            neighbors.add(neighbor);
        }
        public void addEdges(String edgeId) {
            edges.add(edgeId);
        }
        public void setLocation(String location) {
            this.location = location;
            String cleanLocation = cleanString(location);
            nameTrie.add(cleanLocation);
            if (names.containsKey(cleanLocation)) {
                names.get(cleanLocation).add(Long.parseLong(this.id));
            } else {
                names.put(cleanLocation, new ArrayList<>(Collections.singletonList(Long.parseLong(this.id))));
            }
        }
        public String getId() {
            return id;
        }
        public List<String> getNeighbors() {
            return neighbors;
        }
        public List<String> getEdges() {
            return edges;
        }
        public double getLat() {
            return lat;
        }
        public double getLon() {
            return lon;
        }
        public String getLocation() {
            return location;
        }
    }
    public static class Edge {
        String id;
        String name;
        String maxspeed;
        boolean valid;

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setMaxspeed(String maxspeed) {
            this.maxspeed = maxspeed;
        }
        public boolean isValid() {
            return valid;
        }
        public void setValid(boolean valid) {
            this.valid = valid;
        }
    }
    private static Map<String, Node> nodes = new HashMap<>();
    private Map<String, Edge> edges = new HashMap<>();
    private static MyTrieSet nameTrie = new MyTrieSet();
    private static Map<String, ArrayList<Long>> names = new HashMap<>();
    static class MyTrieSet {
        private class Node {
            boolean isKey = false;
            Map<Character, Node> next = new HashMap<>();
        }
        private Node root;
        public MyTrieSet() {
            root = new Node();
        }
        public void add(String key) {
            Node current = root;
            for (char c : key.toCharArray()) {
                current.next.putIfAbsent(c, new Node());
                current = current.next.get(c);
            }
            current.isKey = true;
        }
        public boolean contains(String key) {
            Node current = root;
            for (char c : key.toCharArray()) {
                if (!current.next.containsKey(c)) {
                    return false;
                }
                current = current.next.get(c);
            }
            return current.isKey;
        }
        public List<String> getKeysWithPrefix(String prefix) {
            Node current = root;
            for (char c : prefix.toCharArray()) {
                if (!current.next.containsKey(c)) {
                    return new ArrayList<>();
                }
                current = current.next.get(c);
            }
            return collectKeys(current, prefix);
        }
        public List<String> collectKeys(Node node, String prefix) {
            List<String> keys = new ArrayList<>();
            if (node.isKey) {
                keys.add(prefix);
            }
            for (Map.Entry<Character, Node> entry : node.next.entrySet()) {
                keys.addAll(collectKeys(entry.getValue(), prefix + entry.getKey()));
            }
            return keys;
        }
    }
    public Node getNode(String id) {
        return nodes.get(id);
    }
    public Edge getEdge(String id) {
        return edges.get(id);
    }
    public void addNode(Node node) {
        nodes.put(node.id, node);
    }
    public void addEdge(Edge edge) {
        edges.put(edge.id, edge);
    }
    public static List<String> getLocationsByPrefix(String prefix) {
        List<String> matchedCleaned = nameTrie.getKeysWithPrefix(cleanString(prefix));
        List<String> result = new ArrayList<>();
        for (String cleaned : matchedCleaned) {
            ArrayList<Long> ids = names.get(cleaned);
            if (ids != null) {
                for (Long id : ids) {
                    Node node = nodes.get(id.toString());
                    if (node != null && node.getLocation() != null) {
                        result.add(node.getLocation());
                    }
                }
            }
        }
        return result;
    }
    public static List<Map<String, Object>> getLocations(String location) {
        ArrayList<Long> ids = names.get(cleanString(location));
        List<Map<String, Object>> result = new ArrayList<>();
        if (ids == null) {
            return result; // Return empty list if no ids found
        }
        for (Long id : ids) {
            Node node = nodes.get(id);
            if (node != null) {
                Map<String, Object> info = new HashMap<>();
                info.put("id", id);
                info.put("lat", node.getLat());
                info.put("lon", node.getLon());
                info.put("location", node.getLocation());
                result.add(info);
            }
        }
        return result;
    }
    public String getEdgeName(Long id1, Long id2) {
        Node node1 = nodes.get(Long.toString(id1));
        Node node2 = nodes.get(Long.toString(id2));
        if (node1 == null || node2 == null) {
            return null;
        }
        for (String edgeId : node1.getEdges()) {
            Edge edge = edges.get(edgeId);
            if (node2.getEdges().contains(edgeId)) {
                return edge.getName();
            }
        }
        return null;
    }
    public double angleBetweenNodes(Long id1, Long id2, Long id3) {
        double bearing1 = bearing(id1, id2);
        double bearing2 = bearing(id2, id3);
        double angle = bearing2 - bearing1;
        if (angle < -180) {
            angle += 360;
        } else if (angle > 180) {
            angle -= 360;
        }
        return angle;
    }

    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {

        try {
            File inputFile = new File(dbPath);
            System.out.println("Trying to load OSM from: " + inputFile.getAbsolutePath());
            System.out.println("File exists? " + inputFile.exists());
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        List<String> alones = new ArrayList<>();
        for (String id : nodes.keySet()) {
            Node node = nodes.get(id);
            if (node.getNeighbors().isEmpty()) {
                alones.add(id);
            }
        }
        for (String id : alones) {
            nodes.remove(id);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        List<Long> vertexIds = new ArrayList<>();
        for (String id : nodes.keySet()) {
            vertexIds.add(Long.parseLong(id));
        }
        return vertexIds;
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        List<Long> adjacentIds = new ArrayList<>();
        Node node = nodes.get(Long.toString(v));
        for (String id : node.getNeighbors()) {
            adjacentIds.add(Long.parseLong(id));
        }
        return adjacentIds;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double closest = Double.MAX_VALUE;
        long closestId = -1;
        for (Node node : nodes.values()) {
            double distance = GraphDB.distance(lon, lat, node.lon, node.lat);
            if (distance < closest) {
                closest = distance;
                closestId = Long.parseLong(node.id);
            }
        }
        if (closestId == -1) {
            throw new IllegalArgumentException("No nodes in the graph.");
        }
        return closestId;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodes.get(Long.toString(v)).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodes.get(Long.toString(v)).lat;
    }
}
