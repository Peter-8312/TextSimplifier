package ie.atu.sw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Peter Carroll
 * @version 1.0
 * @since 1.8
 * 
 * GoogleWordsManager is responsible for:
 * 1) Loading the Google-1000 word list from a file.
 * 2) Building a subset map of word -> embedding 
 *    by looking up each Google-1000 word in the main GloVE map.
 */
public class GoogleWordsManager {
	
	 /**
     * Loads the Google-1000 words from a file where each line
     * contains exactly one word (no vectors).
     * 
     * @param filePath The path to the Google-1000 file.
     * @return A List<String> of Google-1000 words in lower-case.
     * @throws IOException If the file cannot be read.
     */
	
	// Big O for loadGoogleWordList = O(G) where G is number of lines/words
    public List<String> loadGoogleWordList(String filePath) throws IOException {
        List<String> googleWords = new ArrayList<>();

        // Try-with-resources ensures the file is closed automatically
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Convert to lower-case for consistent lookups
                line = line.trim().toLowerCase();
                // If not empty, add it to the list
                if (!line.isEmpty()) {
                    googleWords.add(line);
                }
            }
        }

        return googleWords;
    }

    /**
     * Builds a subset map of word -> embedding vectors using the
     * main GloVE embeddings for the words in the Google-1000 list.
     * 
     * @param gloveMap     A Map containing all GloVE embeddings (word -> vector).
     * @param googleWords  The list of Google-1000 words (already loaded).
     * @return A Map of Google-1000 words -> their embedding vectors (if found in gloveMap).
     */
    
    // Big O for buildGoogleSubsetMap = O(G) where G is number of words
    public Map<String, double[]> buildGoogleSubsetMap(
        Map<String, double[]> gloveMap,
        List<String> googleWords
    ) {
        Map<String, double[]> subsetMap = new HashMap<>();

        // For each word in Google-1000, retrieve its vector from the GloVE map
        for (String word : googleWords) {
            double[] vector = gloveMap.get(word);
            // Only add if the GloVE map has an embedding for this word (but we know it will!)
            if (vector != null) {
                subsetMap.put(word, vector);
            }
        }

        return subsetMap;
    }
}
