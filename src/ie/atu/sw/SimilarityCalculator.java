package ie.atu.sw;

/**
 * @author Peter Carroll
 * @version 1.0
 * @since 1.8
 * 
 * SimilarityCalculator provides methods to compute cosine similarity
 * between two embedding vectors.
 */
public class SimilarityCalculator {
	
	 /**
     * Computes the cosine similarity between two vectors.
     * 
     * @param vectorA The first vector.
     * @param vectorB The second vector.
     * @return A double representing the cosine similarity, 
     *         where 1.0 is maximum similarity, 
     *         and -1.0 is maximum dissimilarity.
     */
	
	// Big O for cosineSimilarity = O(n)
    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;  // Sum of products
        double normA = 0.0;       // Sum of squares for vectorA
        double normB = 0.0;       // Sum of squares for vectorB
        
        // Ensure both vectors have the same length
        int length = Math.min(vectorA.length, vectorB.length);
        
        for (int i = 0; i < length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        // Avoid dividing by zero if either vector is zero-length
        double denominator = (Math.sqrt(normA) * Math.sqrt(normB));
        if (denominator == 0.0) {
            // If one vector is all zeros, similarity is undefined. 
            return 0.0;
        }

        return dotProduct / denominator;
    }

}
