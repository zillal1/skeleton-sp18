import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class HuffmanDecoder {
    public static void main(String[] args) {
        String inputFile = args[0];
        String outputFile = args[1];
        BinaryTrie trie;
        BitSequence encodedData;
        ObjectReader in = new ObjectReader(inputFile);
        trie = (BinaryTrie) in.readObject();
        encodedData = (BitSequence) in.readObject();
        StringBuilder decodedOutput = new StringBuilder();
        BitSequence remainingBits = new BitSequence(encodedData);
        while (remainingBits.length() > 0) {
            Match match = trie.longestPrefixMatch(remainingBits);
            decodedOutput.append(match.getSymbol());
            remainingBits = remainingBits.lastNBits(
                    remainingBits.length() - match.getSequence().length());
        }
        FileUtils.writeCharArray(outputFile, decodedOutput.toString().toCharArray());
    }
}
