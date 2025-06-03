import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class HuffmanDecoder {
    public static void main(String[] args) {
        String inputFile = args[0];
        String outputFile = args[1];
        BinaryTrie trie;
        BitSequence encodedData;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(inputFile))) {
            trie = (BinaryTrie) in.readObject();
            encodedData = (BitSequence) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            return;
        }
        StringBuilder decodedOutput = new StringBuilder();
        BitSequence remainingBits = new BitSequence(encodedData);
        while (remainingBits.length() > 0) {
            Match match = trie.longestPrefixMatch(remainingBits);
            decodedOutput.append(match.getSymbol());
            remainingBits = remainingBits.lastNBits(remainingBits.length() - match.getSequence().length());
        }
        try (java.io.FileWriter writer = new java.io.FileWriter(outputFile)) {
            writer.write(decodedOutput.toString());
        } catch (IOException e) {
            System.err.println("Error writing to output file: " + e.getMessage());
        }
    }
}
