package ie.atu.sw;

import java.io.*;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Peter Carroll
 * @version 1.0
 * @since 1.8
 * 
 * TextSimplifier is responsible for:
 * 1) Reading an input text file,
 * 2) Replacing each word with the closest match from the Google-1000 subset,
 * 3) Writing the simplified text to an output file.
 * 
 */
public class TextSimplifier {
	
	// Stripping text
    private static final Pattern NON_ALPHA = Pattern.compile("[^a-zA-Z]");

    /**
     * Simplify the entire text from a given input file and write it to an output file.
     * 
     * @param inputFilePath  Path to the text file that needs simplification.
     * @param outputFilePath Path to the file where the simplified text will be saved.
     * @param gloveMap       A large map of word -> vector (e.g., GloVE embeddings).
     * @param googleMap      A subset map of 1,000 most common words -> vector.
     * @throws IOException If reading or writing fails.
     */
    
    // Big O for simplifyTextFile = O(T * cost of simplifyToken) where T is the total number of tokens
    public void simplifyTextFile(
            String inputFilePath,
            String outputFilePath,
            Map<String, double[]> gloveMap,
            Map<String, double[]> googleMap
    ) throws IOException {
        
        // Ensure can read and write files.
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = br.readLine()) != null) {
                // Split on whitespace to get tokens
                String[] tokens = line.split("\\s+");

                for (int i = 0; i < tokens.length; i++) {
                    // Replace the token with its simplified version
                    tokens[i] = simplifyToken(tokens[i], gloveMap, googleMap);
                }

                // Join the tokens back together and write to output
                String simplifiedLine = String.join(" ", tokens);
                bw.write(simplifiedLine);
                bw.newLine();
            }
        }
    }

    /**
     * Simplify an individual token by removing punctuation (optional),
     * preserving capitalization (optional), and finding the closest match
     * from the Google-1000 set using Cosine similarity.
     * 
     * @param token     The original token from the input text.
     * @param gloveMap  The large GloVE map of word -> vector.
     * @param googleMap The Google-1000 subset map of word -> vector.
     * @return The simplified token (possibly changed to a Google-1000 word),
     *         or the original token if no embeddings were found.
     */
    
    // Big O for simplifyToken = O(G * n) where G is size of googleMap (1000) and n is the embedding dimension (50)
    private String simplifyToken(
            String token,
            Map<String, double[]> gloveMap,
            Map<String, double[]> googleMap
    ) {
        // 1) Strip punctuation
        String cleaned = NON_ALPHA.matcher(token).replaceAll("");
        if (cleaned.isEmpty()) {
            // If there's no alphabetic content, just return an empty string
            // or return the token as-is if you want punctuation
            return "";  
        }

        // 2) Preserve the original capitalization style
        //  Store the "cleaned" version for lookup in lower-case
        String originalCase = cleaned;           // e.g. "Hello"
        String lookupWord   = cleaned.toLowerCase(); // e.g. "hello"

        // 3) If the word is already in googleMap, optionally skip further processing
        if (googleMap.containsKey(lookupWord)) {
            // Match the capitalization style to the existing word
            return matchCapitalization(originalCase, lookupWord);
        }

        // 4) If not in Google-1000, see if GloVE has an embedding
        double[] vector = gloveMap.get(lookupWord);
        if (vector == null) {
            // No embedding in GloVE -> keep it as-is (but punctuation removed)
            return originalCase;
        }

        // 5) Find the single best match in Google-1000 using Cosine similarity
        String bestMatch = null;
        double bestScore = -Double.MAX_VALUE;
        for (Map.Entry<String, double[]> entry : googleMap.entrySet()) {
            double[] googleVec = entry.getValue();
            double score = SimilarityCalculator.cosineSimilarity(vector, googleVec);
            if (score > bestScore) {
                bestScore = score;
                bestMatch = entry.getKey();
            }
        }

        // 6) If found a best match, apply the same capitalization style
        if (bestMatch != null) {
            return matchCapitalization(originalCase, bestMatch);
        } else {
            // If somehow didn't find anything (very unlikely here), return originalCase
            return originalCase;
        }
    }

    /**
     * Matches the capitalization style of the original word in the replacement word.
     * 
     * - If the original is ALL CAPS, we return the replacement in ALL CAPS.
     * - If the original is Capitalized (e.g., "Hello"), capitalize the first letter
     *   of the replacement and lower-case the rest.
     * - Otherwise, return the replacement in all lower-case.
     * 
     * @param original   The original word's capitalization (punctuation removed).
     * @param replacement The new word (in lower-case).
     * @return The replacement word adjusted to match the original's capitalization style.
     */
    
    // Big O for matchCapitalization = O(k) where k is length of word
    private String matchCapitalization(String original, String replacement) {
        // Check if original is all upper-case
        if (original.equals(original.toUpperCase())) {
            return replacement.toUpperCase();
        }
        // Check if only the first letter is upper-case, and the rest is lower-case
        if (original.length() > 1) {
            boolean firstIsUpper = Character.isUpperCase(original.charAt(0));
            String remainder     = original.substring(1);
            boolean restIsLower  = remainder.equals(remainder.toLowerCase());
            if (firstIsUpper && restIsLower) {
                // Capitalize just the first letter
                return replacement.substring(0, 1).toUpperCase() + replacement.substring(1).toLowerCase();
            }
        }
        // Default: return all-lowercase
        return replacement.toLowerCase();
    }

}
