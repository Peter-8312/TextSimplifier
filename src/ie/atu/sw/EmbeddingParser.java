package ie.atu.sw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Carroll
 * @version 1.0
 * @since 1.8
 * 
 * EmbeddingParser is responsible for reading a file of 
 * word embeddings into a Map<String, double[]>.
 * 
 * Example file format (comma-delimited):
 *   word, val1, val2, val3, ..., valN
 * 
 */
public class EmbeddingParser {
	
	/**
     * parseFile reads each line of the specified file, extracts the word 
     * and its embedding vector, and stores them in a Map.
     *
     * @param filePath   The path to the embeddings file.
     * @param vectorSize The number of numeric values in each embedding.
     * @param delimiter  Used to split each line (e.g., ",\\s*" for comma + optional space).
     * @return           A Map where the key is a lower-case word (String), and the value is an array of doubles (the embedding).
     * @throws IOException If the file cannot be opened or read successfully.
     */
	
	// Big O for parseFile = O(M * vectorSize) where M is the number of lines in embedding file
    public static Map<String, double[]> parseFile(String filePath, int vectorSize, String delimiter) throws IOException {
        // Create a HashMap to store the embeddings. 
        // Key: the word, Value: the vector of doubles
        Map<String, double[]> embeddings = new HashMap<>();
        
        // Try-with-resources ensures the BufferedReader is closed automatically
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line; // will hold each line read from the file
            while ((line = br.readLine()) != null) {
                // Split the line using the specified delimiter
                // parts[0] = the word, parts[1..vectorSize] = the numeric values
                String[] parts = line.split(delimiter);

                // The first element is the word (store it in lower-case for consistency)
                String word = parts[0].trim().toLowerCase();

                // Create an array to hold the numeric embedding
                double[] vector = new double[vectorSize];

                // Parse each numeric value from the line into the vector array
                // Start i at 0, but the embedding data is at parts[i + 1]
                for (int i = 0; i < vectorSize; i++) {
                    // Convert each part from String to double
                    vector[i] = Double.parseDouble(parts[i + 1].trim());
                }

                // Store the word -> vector mapping in the HashMap
                embeddings.put(word, vector);
            }
        }
        
        // Return the constructed Map of word -> embedding
        return embeddings;
    }
}

