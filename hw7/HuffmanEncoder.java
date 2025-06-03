import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (char symbol : inputSymbols) {
            frequencyTable.put(symbol, frequencyTable.getOrDefault(symbol, 0) + 1);
        }
        return frequencyTable;
    }
    /*public static char[] readFileAsCharArray(String filePath) {
        // Implement file reading logic here
        // For now, returning an empty char array as a placeholder
        File file = new File(filePath);
        byte[] inputBytes = new byte[(int) file.length()];
        char[] inputSymbols = new char[(int) file.length()];
        try (FileReader fileReader = new FileReader(file)) {
            fileReader.read(inputBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return inputSymbols;
    }*/
    public static void main(String[] args) {
        String inputFile = args[0];
        String outputFile = inputFile + ".huf";
        char[] inputSymbols = FileUtils.readFile(inputFile);
        Map<Character, Integer> frequencyTable = buildFrequencyTable(inputSymbols);
        BinaryTrie trie = new BinaryTrie(frequencyTable);
        Map<Character, BitSequence> lookupTable = trie.buildLookupTable();
        List<BitSequence> sequences = new ArrayList<BitSequence>();
        for (char symbol : inputSymbols) {
            sequences.add(lookupTable.get(symbol));
        }
        BitSequence result = BitSequence.assemble(sequences);
        ObjectWriter out = new ObjectWriter(outputFile);
        out.writeObject(trie);
        out.writeObject(result);
    }
}
