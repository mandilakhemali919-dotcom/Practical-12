import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class AnagramsAlt {
    
    public static void main(String[] args) throws IOException {
        // Get input file path (same logic as original)
        String inputPath = args.length > 0 ? args[0] : "ulysses.text";
        Path filePath = Paths.get(inputPath);
        
        if (!Files.exists(filePath)) {
            filePath = Paths.get("Practical_15", inputPath);
        }
        
        if (!Files.exists(filePath)) {
            System.err.println("File not found: " + inputPath + 
                             " (tried current directory and Practical_15 directory.)");
            return;
        }
        
        // Step 1: Read all words and build anagram dictionary (exact same logic)
        Map<String, Set<String>> anagramDict = new TreeMap<>();
        
        List<String> allLines = Files.readAllLines(filePath, StandardCharsets.ISO_8859_1);
        
        for (String line : allLines) {
            if (line == null) continue;
            
            for (String rawWord : line.split("\\s+")) {
                // Clean word exactly as original
                String word = rawWord.replaceAll("^[...,;:_!\\-]+|[...,;:_!\\-]+$", "").toLowerCase();
                if (word.isEmpty()) continue;
                
                // Generate signature (exact same method)
                char[] chars = word.toCharArray();
                Arrays.sort(chars);
                String signature = new String(chars);
                
                // Add to dictionary (maintaining exact duplicate prevention)
                anagramDict.computeIfAbsent(signature, k -> new LinkedHashSet<>()).add(word);
            }
        }
        
        // Step 2: Filter only anagram groups with 2+ words and sort them (exact same)
        Map<String, List<String>> anagramsOnly = new TreeMap<>();
        
        for (Map.Entry<String, Set<String>> entry : anagramDict.entrySet()) {
            if (entry.getValue().size() >= 2) {
                List<String> sortedWords = new ArrayList<>(entry.getValue());
                Collections.sort(sortedWords);
                anagramsOnly.put(sortedWords.get(0), sortedWords);
            }
        }
        
        // Step 3: Generate all rotations (exact same algorithm)
        List<String> anagramLines = new ArrayList<>();
        
        for (List<String> words : anagramsOnly.values()) {
            String current = String.join(" ", words);
            anagramLines.add(current);
            
            for (int i = 1; i < words.size(); i++) {
                int spacePos = current.indexOf(' ');
                current = current.substring(spacePos + 1) + " " + current.substring(0, spacePos);
                anagramLines.add(current);
            }
        }
        
        // Step 4: Sort all lines (exact same)
        Collections.sort(anagramLines);
        
        // Step 5: Write output file (exact same formatting)
        Path outputPath = filePath.getParent() != null 
            ? filePath.getParent().resolve("theAnagrams.tex")
            : Paths.get("theAnagrams.tex");
        
        try (PrintWriter out = new PrintWriter(
                new OutputStreamWriter(Files.newOutputStream(outputPath), StandardCharsets.UTF_8))) {
            
            char currentLetter = 0;
            
            for (String line : anagramLines) {
                char firstChar = line.charAt(0);
                char lowerFirst = Character.toLowerCase(firstChar);
                
                if (lowerFirst != currentLetter) {
                    currentLetter = lowerFirst;
                    out.println();
                    out.println("\\vspace{14pt}");
                    out.println("\\noindent\\textbf{\\Large " + 
                               Character.toUpperCase(firstChar) + "}\\\\*[+12pt]");
                }
                
                out.print(line);
                out.println("\\\\");
            }
        }
        
        System.out.println("Anagram dictionary built. Found " + 
                          anagramsOnly.size() + " anagram groups.");
        System.out.println("Output written to " + outputPath);
    }
}
