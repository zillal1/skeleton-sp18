import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    private static class Node implements Serializable , Comparable<Node> {
        private char value;
        private int frequency;
        private Node left;
        private Node right;
        public Node(char value, int frequency) {
            this.value = value;
            this.frequency = frequency;
            this.left = null;
            this.right = null;
        }
        private Node(char value, int frequency, Node left, Node right) {
            this.value = value;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.frequency, other.frequency);
        }
    }
    private Node root;
    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        this.root = null;
        MinPQ<Node> pq = new MinPQ<Node>();
        for (Map.Entry<Character, Integer> entry : frequencyTable.entrySet()) {
            char character = entry.getKey();
            int frequency = entry.getValue();
            pq.insert(new Node(character, frequency));
            // Insert the character into the trie
            //insert(root, character, frequency);
        }
        while (pq.size() > 1) {
            Node left = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.frequency + right.frequency, left, right);
            pq.insert(parent);
        }
        root = pq.delMin();
    }
    public Match longestPrefixMatch(BitSequence querySequence) {
        Node currentNode = root;
        Node matchedNode = root;
        StringBuilder currentPrefix = new StringBuilder();
        //BitSequence matchedSequence = new BitSequence();
        if (currentNode == null) {
            return new Match(new BitSequence(), '\0');
        }
        for (int i = 0; i < querySequence.length(); i++) {
            if (querySequence.bitAt(i) == 0) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
            if (currentNode == null) {
                break;
            }
            matchedNode = currentNode;
            //currentPrefix.append(querySequence.bitAt(i) + '0');
            currentPrefix.append(querySequence.bitAt(i) == 0 ? '0' : '1');
        }
        return new Match(new BitSequence(String.valueOf(currentPrefix)), matchedNode.value);
    }
    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> lookupTable = new HashMap<>();
        buildLookupTable(root, new BitSequence(), lookupTable);
        return lookupTable;
    }
    private void buildLookupTable(Node node, BitSequence currentSequence, Map<Character, BitSequence> lookupTable) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            lookupTable.put(node.value, currentSequence);
            return;
        }
        buildLookupTable(node.left, currentSequence.appended(0), lookupTable);
        buildLookupTable(node.right, currentSequence.appended(1), lookupTable);
    }
}
