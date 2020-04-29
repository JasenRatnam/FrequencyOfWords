package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author Jasen Ratnam 
 * 40094237
 * COEN 352, final project
 * Due date: December 2nd 2019
 * Given a dictionary of distinct words and an input file, find the frequency
 * of words in the text file that exist in the dictionary.
 */
public class Project 
{
    private static String partNumber = null;
    private static final Boolean DEBUG = false;
    
    /**
     * This method gets a BST dictionary and a string containing the path of the input file.
     * It outputs the a tree map containing the frequency of legal words found.
     * It also checks if some words are mutated, with different characters or missing spaces.
     * It adds the corrected words to the map to add the frequency of the word.
     * @param inputFile     Path of file
     * @param dictionary    BST dictionary
     * @return              a tree map containing the legal words and their frequencies
     * @throws IOException  if an error happened while reading the input file.
     */
    public static TreeMap<String, Integer> getInputFreqMap(String inputFile, BSTree dictionary) throws IOException
    {
        // initialize tree map to store frequency of legal words with the words as keys
        TreeMap<String, Integer> wordsInDictionary = new TreeMap<>();
        // list to store words not found in dictioanry to check for mutations later
        List<String> wordsNotDictionary= new ArrayList();
        
        //initialize scanner to read input file
        Scanner sc2 = null;
        try 
        {
            //used to debug
            if(DEBUG)
            {
                System.out.println("\nThe words from the input file in lower caps:");
            }
            
            //read file
            sc2 = new Scanner(new File(inputFile));
            while (sc2.hasNextLine()) 
            {
                Scanner s2 = new Scanner(sc2.nextLine());

                while (s2.hasNext()) 
                {
                    String s = s2.next();
                    // make all words be in lowercase
                    String s_low = s.toLowerCase();
                    
                    if(DEBUG)
                    {
                        //print words in input file
                        System.out.println(s_low);
                    }
                    
                    // remove punctions from words
                    s_low = s_low.replaceAll("\\p{Punct}",""); 
                        
                    // if word in input file is in dictionary
                    if(dictionary.lookup(s_low))
                    {
                        //If word is not already in map
                            //add word to map with 0 + 1 
                        //If word is  already in map
                            //replace word to map with its previous value + 1
                        //This works because Tree map does not allow duplicates
                        //Uses the getOrDefault method to get the previous value 
                            //or 0 if word is not already in map
                        wordsInDictionary.put(s_low, wordsInDictionary.getOrDefault(s_low, 0) + 1);
                    }
                    else
                    {
                        String s_low2 = s.toLowerCase();
                        //add word in list if its not in dicitonary to verify if its mutated later
                        wordsNotDictionary.add(s_low2);
                    }
                }
            }
        } 
        catch (FileNotFoundException e)   //Catch errors that may happen while reading file
        {
            e.printStackTrace();  
        }
        
        if(DEBUG)
        {   //Print map without mutated words
            System.out.println("\nPrinting the map with frequency of words in "
                    + "both dictionary and input file:(SORTED ALPHABETICALLY/ WITHOUT THE MUTATED WORDS)");
            for (String name: wordsInDictionary.keySet())
            {
                String value = wordsInDictionary.get(name).toString();  
                System.out.println(name + " " + value);  
            }
            
            System.out.println("\nPrinting the words not found in the dictionary:");
            for (String name: wordsNotDictionary)
            { 
                System.out.println(name);  
            }
        }
        //do part 2 (space removed correction)
        if(partNumber.equals("2"))
        {
            // if there is words in the input file that are not in the dictionary
            // verify for mutated words
            // add corrected words to map
            if(!wordsNotDictionary.isEmpty())
            {
                // use addSpaceRemovedWords
                wordsInDictionary = addSpaceRemovedWords(wordsInDictionary, wordsNotDictionary, dictionary);
            }

            if(DEBUG)
            {   //Print map with mutated words
                System.out.println("\nPrinting the map with frequency of words in "
                        + "both dictionary and input file:(SORTED ALPHABETICALLY/ WITH MUTATED WORDS)");
                for (String name: wordsInDictionary.keySet())
                {
                    String value = wordsInDictionary.get(name).toString();  
                    System.out.println(name + " " + value);  
                }
            }
        }
        
        //do part 3 (mutated word)
        if(partNumber.equals("3"))
        {
            if(!wordsNotDictionary.isEmpty())
            {
                wordsInDictionary = addMutatedWords(wordsInDictionary, wordsNotDictionary, dictionary);
            }
            
        }
        return wordsInDictionary;
    }
    
    /**
     * This methods checks the list of words not detected by the dictionary and
     * corrects the mutations.
     * It adds the corrected words the map with its frequency
     * It outputs a file containing the original word and its corrected version
     * @param inputMap                  Map containing the legal words and its frequency
     * @param wordsNotDictionary        list of words not in the dictionary from the input 
     * @param dictionary                BST dictionary
     * @return                          return the updated map with corrected words
     * @throws IOException              catches error while writing output file with corrected words
     */
    public static TreeMap<String, Integer> addMutatedWords(TreeMap<String, Integer> inputMap, List<String> wordsNotDictionary, BSTree dictionary) throws IOException
    {
        // intialize writer to write a file named "corrected_words_detected.txt"
        PrintWriter writer = new PrintWriter(new FileWriter("corrected_words_detected.txt"));
        
        // initialize list words to save all possible words of the current string from list
        List<String> words = new ArrayList<String>();
        
        //go trough every word of the list
        for (String s : wordsNotDictionary) 
        {
            if(DEBUG)
            {
                System.out.println("\nPrinting all possible words for the word: " + s +"\n");
            }
            // go through every character of the word
            for (int i = 0; i < s.length(); i++) 
            {
                // change the current character of the word with every letter of
                // the alphabet and check if the reuslt is a word of the dictionary
                for (char character = 'a'; character <= 'z'; character++) 
                {
                    // replace cahracter and build new string
                    StringBuilder sb = new StringBuilder(s);
                    sb.setCharAt(i, character);
                    
                    // check if new word is in the dictionary
                    if(dictionary.lookup(sb.toString()))
                    {
                        if(DEBUG)
                        {
                            System.out.println(sb.toString() +"\n");
                        }
                        // add to list if its in the dictionary
                        // add only one word because theres no way to know which
                        // one is the right choice.
                        if(words.isEmpty())
                        {
                            if(DEBUG)
                            {
                                System.out.println("Choosen word: " + sb.toString() + "\n");
                            }
                            // add new word to word list
                            words.add(sb.toString());
                            // add new word to map
                            inputMap.put(sb.toString(), inputMap.getOrDefault(sb.toString(), 0) + 1);
                        }
                    }
                }
            }
            
            // create ouput file with original word and corrected word
            if(!words.isEmpty())
            {
                writer.printf( "%-15s %15s %n", s,words);
                if(DEBUG)
                {
                    System.out.println("Original words vs corrected word");
                    System.out.printf( "%-15s %15s %n", s,words);
                }
            }
            
            //empty list for next word
            words.clear();
        }
        writer.close();
        return inputMap;
}
    
    /**
     * This method gets a BST dictionary and a list of words that were in the input but not in the dictionary.
     * It verifies if the words not found in the dictionary are words with missing spaces
     * add separated word to the map of frequencies
     * It also output a files with the corrected words and its original space missing word
     * @param inputMap                  Map containing the legal words and its frequency
     * @param wordsNotDictionary        list of words not in the dictionary from the input 
     * @param dictionary                BST dictionary
     * @return                          return the updated map with corrected words
     * @throws IOException              catches error while writing output file with corrected words
     */
    public static TreeMap<String, Integer> addSpaceRemovedWords(TreeMap<String, Integer> inputMap, List<String> wordsNotDictionary, BSTree dictionary) throws IOException
    {
        
        /* 
        BRAIN STORM: IGNORE
        Get a word from the list of words not in the dictionary
        Get first letter of that word
        Check if letter is a word in dictionary
        Add second letter of word to first letter and second if both letters forms a word in dictionary
            if in dictionary add to map
            Save corrected words to output in a text file later
        Continue adding letters and checking dictionary until last letter of words
        Restart everything by starting from second letter of word
        Restart everything by starting from third letter of word
        Continue doing this until you start the algorithm from last letter of word.
        return updated map with corrected words added
        output a text file with words corrected
        */
        
        // intialize writer to write a file named "corrected_words_detected.txt"
        PrintWriter writer = new PrintWriter(new FileWriter("corrected_words_detected.txt"));
        
        // initialize list words to save all possible words of the current string from list
        List<String> words = new ArrayList();
        
        
        //go trough evry word of the list
        for (String s : wordsNotDictionary) 
        {
            // remove punctions from words
            s = s.replaceAll("\\p{Punct}",""); 
            if(DEBUG)
            {
                System.out.println("\nPrinting all possible substrings for the word " + s);
            }
            //go trough every single possible substring of the string
            for (int i = 0; i < s.length() + 1; i++) 
            {
                for (int j = 0; j < i; j++) 
                {
                    //possibility of substring
                    String substring = s.substring(j,i);
                    
                    // Check if this substring is a word in the dictionary and if its at the end or start of the string
                    // This ignores any substring that would be found in the middle of the word that wont make sense
                    if(dictionary.lookup(substring) &&(s.startsWith(substring)||s.endsWith(substring)))
                    {
                        //all substring here are possible words in dictionary
                        
                        if(!words.contains(substring))  // if substring is not already in the list of possible words for this string
                        {
                            // add word to list
                            words.add(substring);
                        }
                       
                        if(DEBUG)
                        {
                             System.out.println(s.substring(j,i));
                        }
                    }    
                }
            }
            
            
            // check if one of the possible words is a part of another word
            // delete word if it is
            for (int i = 0; i < words.size(); i++) 
            {
                for(int j = 0; j <words.size(); j++)
                {
                    //if a word is made of another possible word, and its a different word, deelete it
                    if(words.get(i).contains(words.get(j)) && !words.get(j).equals(words.get(i)))
                    {
                        words.remove(j);
                    }
                }
            }
            
            //final choice of words done.
            //output it in the file using writer
            if(!words.isEmpty())
            {
                writer.printf( "%-15s %15s %n", s,words);
                if(DEBUG)
                {
                     System.out.println("FINAL CHOICE: " + words);
                }
            }
            
            // add words to the map with its freqeucny added.
            for(int i = 0; i < words.size(); i++)
            {
                inputMap.put(words.get(i), inputMap.getOrDefault(words.get(i), 0) + 1);
            }
            
            // clear words list for next string from list
            words.clear();
        }
        writer.close();
        
        return inputMap;
    }
    
    /**
     * Method to sort the map with legal words of input file and their frequency
     * The map gets sorted by its value (frequency) and then by its key(words/ alphabetically)
     * @param inputMap  Map with frequencies to be sorted
     * @return          Returns a map with the same values as inputMap but sorted by its values 
     *                  then alphabetically
     */
    public static Map<String, Integer> getSortedFreqMap(TreeMap<String, Integer> inputMap)
    {
        // use map functions to sort by its values
        Map<String, Integer> sortedMap = inputMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
       
        //Source to sort by value: https://dzone.com/articles/how-to-sort-a-map-by-value-in-java-8
        
        if(DEBUG)
        {    // outputs words in text that are legal, and their frequencies
            System.out.println("\nPrinting the map with frequency of words in "
                    + "both dictionary and input file:(SORTED BY FREQUENCY AND"
                    + " THEN ALPHABETICALLY IN ASCENDING ORDER)");

            //Print every key with their value, formated using printf.
            //Output will be sorted since map is sorted
            for (String name: sortedMap.keySet())
            {
                String value = sortedMap.get(name).toString();  
                System.out.printf( "%-15s %15s %n", name, value);
            }
        }
        
        return sortedMap;
    }
    
    /**
     * Method to write a file in the folder of the project containing the legal
     * words and their frequency formated using printf.
     * @param sortedFreqMap     Map to output as a file
     * @throws IOException      Catches any errors that may happen while writing file
     */
    public static void writeFrequencyFile(Map<String, Integer> sortedFreqMap) throws IOException
    {
        PrintWriter freqWriter = new PrintWriter(new FileWriter("frequencies.txt"));
        
        // prints the word and then its number of frequency
        for (String name: sortedFreqMap.keySet())
        {
            String value = sortedFreqMap.get(name).toString();  
            freqWriter.printf( "%-15s %15s %n", name, value);
        }
        freqWriter.close();
    }
    
    /**
     * Method to write a file in the folder of the project containing the legal
     * words and their frequency formated using printf.
     * It prints the word the number of frequency times
     * @param sortedFreqMap
     * @throws IOException
     */
    public static void writeRepeatedFile(Map<String, Integer> sortedFreqMap) throws IOException
    {
        PrintWriter freqWriter = new PrintWriter(new FileWriter("repeated.txt"));
        
        int k = 1;
        // print the word for the lenght of its frequency as long that its bigger than 1
        for (String name: sortedFreqMap.keySet())
        {
            int value = sortedFreqMap.get(name);  
            
            if(value > 1)
            {
                for(int i = 0; i < value;i++)
                {
                    //new line after 20 elements
                    if(k == 20)
                    {
                        freqWriter.print("\n");
                        k = 0;
                    }
                    freqWriter.print(name + " ");
                    k++;
                }
            }
        }
        freqWriter.close();
    }
    
    /**
     * Main method that starts the program.
     * @param args 
     * @throws java.io.IOException 
     */
    public static void main(String[] args) throws IOException 
    {  
        BSTree.setDebug(DEBUG);
        BST_NODE.setDebug(DEBUG);
       
        //Get dictionary file
        System.out.println("Please enter the name of dictionary text file: ");
        Scanner keyboard = new Scanner(System.in);
        String filename = keyboard.nextLine();
        String dictionaryFile;
        if(filename.endsWith(".txt"))
        {
            dictionaryFile = filename;
        }
        else
        {
            dictionaryFile = filename + ".txt";
        }
        // make dictioanry BST using BSTree class
        BSTree dictionary;
        dictionary = BSTree.createTree(dictionaryFile);
        
        // used to debug code
        if(DEBUG)
        {
            String test = "a";
            System.out.println("\nThis is the dictionary stored in the BST:");
            dictionary.traversePreOrder();
            System.out.println("\nChecking if the word '" + test + "' is in the dictionary:");
            System.out.println(dictionary.lookup("a"));
            System.out.println("\n");
            
            // example to verify substring propreties
            //String s = "Jasen";
            //System.out.println(s.substring(0, 0));
        }
        
        // Get input text file
        System.out.println("Please enter the name of input text file: ");
        String filename2 = keyboard.nextLine();
        String inputFile;
        
        if(filename2.endsWith(".txt"))
        {
            inputFile = filename2;
        }
        else
        {
            inputFile = filename2 + ".txt";
        }
        
        File f = new File(inputFile);
        while(!f.exists())
        {
            System.out.println("ERROR: Please enter the name of input text file: ");
            keyboard = new Scanner(System.in);
            filename = keyboard.nextLine();
            inputFile = filename + ".txt";
            f = new File(inputFile);
        }
        
        //get which part to do
        System.out.println("\nPlease enter the name of input text file:\n "
                + "Enter '1' for part 1, Enter '2' for part 2, Enter '3' for part 3,");
        partNumber = keyboard.nextLine();
        Boolean correct;
        if(partNumber.equals("1") ||partNumber.equals("2")||partNumber.equals("3"))
        {
            correct = true;
        }
        else
        {
            correct = false;
        }
        while(!correct)
        {
            System.out.println("Please enter an integer between 1 and 3.");
            partNumber = keyboard.nextLine();
            if(partNumber.equals("1") ||partNumber.equals("2")||partNumber.equals("3"))
            {
                correct = true;
            }
        }
        
        // start timer
        System.out.println("timer started");
        long startTime = System.nanoTime();
        
        // Use tree-map to store words in text file and its occurences & create 
        // a text file with the frequency of every legal word
        
        // Initialize tree-map
        TreeMap<String, Integer> wordsInDictionary = new TreeMap<>();
        
        try
        {
            //get the freqeucny map using the method: getInputFreqMap(inputFile, dictionary)
            // this is sorted in alphabetical order since its a tree map
            wordsInDictionary = getInputFreqMap(inputFile, dictionary);
        }
        catch (IOException e) //catch any errors that may happen by reading input file
        {
            e.printStackTrace(); 
        }
        
        // sorte the map by its values and then keys by alphabetical order
        Map<String, Integer> sortedMap = getSortedFreqMap(wordsInDictionary);
        
        // outputs words in text that are legal, and their frequencies
        System.out.println("\nFrequency of words in the input file that exists "
                + "in the dictionary,\nsorted by their frequencies and then "
                + "alphabetically in ascending order:");
        
        //Print every key with their value, formated using printf.
        //Output will be sorted since map is sorted
        for (String name: sortedMap.keySet())
        {
            String value = sortedMap.get(name).toString();  
            System.out.printf( "%-15s %15s %n", name, value);
        }
        
        //write output files wanted
        try 
        {
            //file that contains the frequency of legal words
            writeFrequencyFile(sortedMap);
            //file that contains the repeated words printed its frequency time
            writeRepeatedFile(sortedMap);
        }
        catch(IOException e) //catch any errors that may happen by writing input file
        {
            e.printStackTrace();  
        }
        
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }
}

