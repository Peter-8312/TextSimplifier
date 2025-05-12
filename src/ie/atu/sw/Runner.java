package ie.atu.sw;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Peter Carroll
 * @version 1.0
 * @since 1.8
 * 
 * Runner is the main entry point for the console-based application.
 * It provides a menu for:
 * 1. Loading GloVE embeddings (using default vector size & delimiter)
 * 2. Loading the Google-1000 word list
 * 3. Building a subset map for Google-1000
 * 4. Setting input/output files
 * 5. Simplifying text
 * 6. Quitting
 */
public class Runner {
	
	// Defaults for GloVE
	private static final int    DEFAULT_VECTOR_SIZE = 50;   
    private static final String DEFAULT_DELIMITER   = ",\\s*";
	
	// Maps and lists to hold data
    private static Map<String, double[]> gloveEmbeddings;       // Big GloVE map
    private static List<String> googleWords;                    // Plain list of Google-1000 words
    private static Map<String, double[]> googleSubsetMap;       // Subset of GloVE only for Google-1000
    
    // File paths (the user can set them via the menu)
    private static String inputFilePath;
    private static String outputFilePath;
    
    // Helper Classes
    // For loading Google words and building subset
    private static GoogleWordsManager googleManager = new GoogleWordsManager();

    // For simplifying text
    private static TextSimplifier textSimplifier = new TextSimplifier();
    
    // Big O for main is O(1) or amount of user interactions
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu(); // Display the main menu
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    loadGloveEmbeddings(scanner);
                    break;
                case "2":
                    loadGoogleWords(scanner);
                    break;
                case "3":
                    buildGoogleSubset();
                    break;
                case "4":
                    setInputFile(scanner);
                    break;
                case "5":
                    setOutputFile(scanner);
                    break;
                case "6":
                    simplifyText(); 
                    break;
                case "7":
                    running = false;
                    System.out.println(ConsoleColour.CYAN_BOLD + 
                        "Exiting application..." + 
                        ConsoleColour.RESET);
                    break;
                default:
                    System.out.println(ConsoleColour.RED_BOLD + 
                        "Invalid option. Please select a valid option [1-7]." + 
                        ConsoleColour.RESET);
            }
        }

        scanner.close();
    }

    /**
     * Prints the menu options to the console.
     */
    // Big O for printMenu is O(1)
    private static void printMenu() {
        System.out.println(ConsoleColour.WHITE_BOLD);
        System.out.println("************************************************************");
        System.out.println("*       ATU - Dept. of Computer Science & Applied Physics  *");
        System.out.println("*                                                          *");
        System.out.println("*   Text Simplifier (GloVE + Google-1000) - Menu           *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
        System.out.println(ConsoleColour.RESET);
        System.out.println("(1) Load GloVE Embeddings");
        System.out.println("(2) Load Google-1000 Words");
        System.out.println("(3) Build Google Subset Map");
        System.out.println("(4) Set Input Text File");
        System.out.println("(5) Set Output File");
        System.out.println("(6) Simplify Text");
        System.out.println("(7) Quit");
        System.out.print(ConsoleColour.BLUE_BOLD);
        System.out.print("Select Option [1-7]>");
        System.out.println(ConsoleColour.RESET);
    }

    /**
     * Menu option (1): Load GloVE embeddings by prompting for a file path,
     * Uses default vector size and delimiter, then parsing via EmbeddingParser.
     */
    private static void loadGloveEmbeddings(Scanner scanner) {
        System.out.println(ConsoleColour.YELLOW_BOLD + "Load GloVE Embeddings" + ConsoleColour.RESET);

        // 1) Prompt for file path
        System.out.print("Enter GloVE file path: ");
        String filePath = scanner.nextLine().trim();

        // Using DEFAULT_VECTOR_SIZE (50) and DEFAULT_DELIMITER (",\\s*")
        try {
            gloveEmbeddings = EmbeddingParser.parseFile(
                filePath, 
                DEFAULT_VECTOR_SIZE, 
                DEFAULT_DELIMITER
            );

            System.out.println(ConsoleColour.GREEN_BOLD +
                "Loaded " + gloveEmbeddings.size() + " embeddings from " + filePath +
                ConsoleColour.RESET);

        } catch (IOException e) {
            System.out.println(ConsoleColour.RED_BOLD + 
                "Error loading GloVE: " + e.getMessage() + 
                ConsoleColour.RESET);
        }
    }

    /**
     * Menu option (2): Load Google-1000 words from a plain list (one word per line).
     */
    private static void loadGoogleWords(Scanner scanner) {
        System.out.println(ConsoleColour.YELLOW_BOLD + "Load Google-1000 Words" + ConsoleColour.RESET);
        System.out.print("Enter Google-1000 file path: ");
        String googlePath = scanner.nextLine().trim();

        try {
            googleWords = googleManager.loadGoogleWordList(googlePath);
            System.out.println(ConsoleColour.GREEN_BOLD + 
                "Loaded " + googleWords.size() + " Google-1000 words from " + googlePath +
                ConsoleColour.RESET);

            // If gloveEmbeddings is already loaded, user can build the subset map next
        } catch (IOException e) {
            System.out.println(ConsoleColour.RED_BOLD + 
                "Error loading Google-1000 words: " + e.getMessage() + 
                ConsoleColour.RESET);
        }
    }

    /**
     * Menu option (3): Build the Google subset map (googleWords -> gloveEmbeddings).
     */
    private static void buildGoogleSubset() {
        System.out.println(ConsoleColour.YELLOW_BOLD + "Build Google Subset Map" + ConsoleColour.RESET);
        if (gloveEmbeddings == null || gloveEmbeddings.isEmpty()) {
            System.out.println(ConsoleColour.RED_BOLD + 
                "Error: GloVE embeddings not loaded yet." + 
                ConsoleColour.RESET);
            return;
        }
        if (googleWords == null || googleWords.isEmpty()) {
            System.out.println(ConsoleColour.RED_BOLD + 
                "Error: Google-1000 words not loaded yet." + 
                ConsoleColour.RESET);
            return;
        }

        googleSubsetMap = googleManager.buildGoogleSubsetMap(gloveEmbeddings, googleWords);
        System.out.println(ConsoleColour.GREEN_BOLD + 
            "Built subset map with " + googleSubsetMap.size() + " entries." + 
            ConsoleColour.RESET);
    }

    /**
     * Menu option (4): Set the input text file path.
     */
    private static void setInputFile(Scanner scanner) {
        System.out.println(ConsoleColour.YELLOW_BOLD + "Set Input Text File" + ConsoleColour.RESET);
        System.out.print("Enter path to the text file to simplify: ");
        inputFilePath = scanner.nextLine().trim();

        if (!inputFilePath.isEmpty()) {
            System.out.println(ConsoleColour.GREEN_BOLD + 
                "Input file set: " + inputFilePath + 
                ConsoleColour.RESET);
        } else {
            System.out.println(ConsoleColour.RED_BOLD + 
                "Input file path cannot be empty." + 
                ConsoleColour.RESET);
            inputFilePath = null;
        }
    }

    /**
     * Menu option (5): Set the output file path.
     */
    private static void setOutputFile(Scanner scanner) {
        System.out.println(ConsoleColour.YELLOW_BOLD + "Set Output File" + ConsoleColour.RESET);
        System.out.print("Enter path for the output file: ");
        outputFilePath = scanner.nextLine().trim();

        if (!outputFilePath.isEmpty()) {
            System.out.println(ConsoleColour.GREEN_BOLD + 
                "Output file set: " + outputFilePath + 
                ConsoleColour.RESET);
        } else {
            System.out.println(ConsoleColour.RED_BOLD +
                "Output file path cannot be empty." +
                ConsoleColour.RESET);
            outputFilePath = null;
        }
    }

    /**
     * Menu option (6): Simplify the text (using the loaded GloVE embeddings,
     * the built Google-1000 subset, and the user-specified input/output files).
     */
    private static void simplifyText() {
        System.out.println(ConsoleColour.YELLOW_BOLD + "Simplify Text" + ConsoleColour.RESET);
        
        // Validate that we have everything we need
        if (gloveEmbeddings == null || gloveEmbeddings.isEmpty()) {
            System.out.println(ConsoleColour.RED_BOLD + 
                "Error: GloVE not loaded." + 
                ConsoleColour.RESET);
            return;
        }
        if (googleSubsetMap == null || googleSubsetMap.isEmpty()) {
            System.out.println(ConsoleColour.RED_BOLD +
                "Error: Google subset map not built. Load Google-1000 words and build the subset." +
                ConsoleColour.RESET);
            return;
        }
        if (inputFilePath == null || inputFilePath.isEmpty()) {
            System.out.println(ConsoleColour.RED_BOLD +
                "Error: Input file path not set." +
                ConsoleColour.RESET);
            return;
        }
        if (outputFilePath == null || outputFilePath.isEmpty()) {
            System.out.println(ConsoleColour.RED_BOLD +
                "Error: Output file path not set." +
                ConsoleColour.RESET);
            return;
        }

        // All good, run the simplification
        try {
            textSimplifier.simplifyTextFile(inputFilePath, outputFilePath, gloveEmbeddings, googleSubsetMap);
            System.out.println(ConsoleColour.GREEN_BOLD + 
                "Text simplified! Output written to: " + outputFilePath +
                ConsoleColour.RESET);
        } catch (IOException e) {
            System.out.println(ConsoleColour.RED_BOLD + 
                "Error simplifying text: " + e.getMessage() + 
                ConsoleColour.RESET);
        }
    }

}
