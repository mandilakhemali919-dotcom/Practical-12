import java.io.*;
import java.util.*;

/**
 * Anagrams.java
 * 
 * Converts Python anagram finder to Java.
 * Reads text file, finds anagram groups, and generates LaTeX output.
 * 
 * Usage: java Anagrams ulysses.text
 */
public class Anagrams {
    
    public static void main(String[] args) {
        // Check command line arguments
        if (args.length != 1) {
            System.out.println("Usage: java Anagrams inputfile");
            System.out.println("You gave: " + Arrays.toString(args));
            System.exit(1);
        }
        
        String inputfile = args[0];
        System.out.println("Data file: " + inputfile);
        
        // Step 1: Read file and count word frequencies (like Python's D dictionary)
        Map<String, Integer> D = new HashMap<>();
        
        try {
            // Open file with latin-1 encoding
            BufferedReader f = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputfile), "ISO-8859-1"));
            
            String line;
            int linenumber = 0;
            int lines = 0;
            
            // Read line by line (like Python's while line != "")
            while ((line = f.readLine()) != null) {
                linenumber++;
                String[] words = line.split("\\s+");
                
                for (String w : words) {
                    // Clean word: remove digits and punctuation but keep apostrophes
                    // Matches Python's: w.strip('[0123456789(,.,.;:_.!?---)]')
                    String W = w.replaceAll("[0-9(),.;:!?---]", "").trim();
                    
                    if (!W.isEmpty()) {
                        // Make lowercase
                        W = W.toLowerCase();
                        
                        // Update word count (Python's D[w] += 1)
                        if (D.containsKey(W)) {
                            D.put(W, D.get(W) + 1);
                        } else {
                            D.put(W, 1);
                        }
                    }
                }
                lines++;
            }
            
            f.close();
            System.out.println("Lines read: " + lines);
            System.out.println("Unique words: " + D.size());
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
        
        // Step 2: Build anagram dictionary (like Python's A dictionary)
        Map<String, List<String>> A = new HashMap<>();
        
        for (String w : D.keySet()) {
            // Create signature by sorting letters
            String a = signature(w);
            
            if (!A.containsKey(a)) {
                List<String> wordList = new ArrayList<>();
                wordList.add(w);
                A.put(a, wordList);
            } else {
                A.get(a).add(w);
            }
        }
        
        // Step 3: Write anagram lists to file
        int anagramCount = 0;
        
        try {
            PrintWriter f = new PrintWriter("anagrams", "ISO-8859-1");
            
            // Sort keys for consistent output
            List<String> keys = new ArrayList<>(A.keySet());
            Collections.sort(keys);
            
            for (String key : keys) {
                List<String> wordList = A.get(key);
                
                // Only output if more than one word (actual anagrams)
                if (wordList.size() > 1) {
                    anagramCount++;
                    
                    // Sort words alphabetically
                    Collections.sort(wordList);
                    
                    // Build the anagram list string
                    String anagramlist = "";
                    for (String word : wordList) {
                        if (anagramlist.isEmpty()) {
                            anagramlist = word;
                        } else {
                            anagramlist += " " + word;
                        }
                    }
                    
                    // Write original list with LaTeX line break
                    f.println(anagramlist + "\\\\");
                    
                    // Generate all rotations (like Python's repeat loop)
                    String currentList = anagramlist;
                    for (int repeat = 0; repeat < wordList.size() - 1; repeat++) {
                        int space = currentList.indexOf(' ');
                        if (space > 0) {
                            currentList = currentList.substring(space + 1) + " " + 
                                         currentList.substring(0, space);
                            f.println(currentList + "\\\\");
                        }
                    }
                }
            }
            
            f.close();
            System.out.println("Anagram groups found: " + anagramCount);
            
        } catch (IOException e) {
            System.err.println("Error writing anagrams file: " + e.getMessage());
            System.exit(1);
        }
        
        // Step 4: Read and sort the anagrams file
        List<String> aa = new ArrayList<>();
        
        try {
            BufferedReader asf = new BufferedReader(
                new InputStreamReader(new FileInputStream("anagrams"), "ISO-8859-1"));
            
            String line;
            while ((line = asf.readLine()) != null) {
                aa.add(line);
            }
            
            asf.close();
            Collections.sort(aa);  // Like Python's sorted(asf.readlines())
            
        } catch (IOException e) {
            System.err.println("Error reading anagrams file: " + e.getMessage());
            System.exit(1);
        }
        
        // Step 5: Create latex directory if it doesn't exist
        File latexDir = new File("latex");
        if (!latexDir.exists()) {
            latexDir.mkdirs();
        }
        
        // Step 6: Generate LaTeX file with proper formatting
        try {
            PrintWriter asftex = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream("latex/theAnagrams.tex"), "UTF-8"));
            
            char letter = 'X';
            
            for (String lemma : aa) {
                if (!lemma.isEmpty()) {
                    char initial = lemma.charAt(0);
                    
                    if (Character.toLowerCase(initial) != Character.toLowerCase(letter)) {
                        letter = initial;
                        asftex.println("\n\\vspace{14pt}");
                        asftex.println("\\noindent\\textbf{\\Large " + 
                                     Character.toUpperCase(initial) + "}\\\\*[+12pt]");
                    }
                    
                    asftex.println(lemma);
                }
            }
            
            asftex.close();
            System.out.println("LaTeX file created: latex/theAnagrams.tex");
            
        } catch (IOException e) {
            System.err.println("Error writing LaTeX file: " + e.getMessage());
            System.exit(1);
        }
        
        // Step 7: Clean up temporary file (like Python's os.remove("anagrams"))
        File anagramsFile = new File("anagrams");
        if (anagramsFile.exists()) {
            anagramsFile.delete();
        }
        
        System.out.println("\nProcessing complete!");
        System.out.println("To create PDF:");
        System.out.println("1. Install LaTeX (MiKTeX/TeX Live/MacTeX)");
        System.out.println("2. Create a main LaTeX document including latex/theAnagrams.tex");
        System.out.println("3. Run: lualatex main.tex");
    }
    
    /**
     * Creates signature by sorting letters alphabetically
     * Like Python's: return ''.join(sorted(word))
     */
    private static String signature(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}
