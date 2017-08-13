package util;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import naivebayes.TrainningSet;

import naivebayes.Vector;

public class Utils {

    private static final String PUNCTUATION_REGEX = "[!\"#$%&'()\\*\\+,-\\./:;<=>?@\\[\\]\\^_`{|}~]";

    public static String removePunctuation(String text) {

        return text.replaceAll(PUNCTUATION_REGEX, " ");
    }
    
    private static HashMap<String, Integer> createIndexedUniqueWords(Set<String> uniqueWords){
        HashMap<String, Integer> map = new HashMap<>();
        int index = 1;
        for (String word : uniqueWords) {
            map.put(word, index);
            index++;
        }
        return map;
    }
    
    public static TrainningSet createVectorsFromDataset(File datasetFile, File scoreFile) throws IOException{
        /* generate unique words, list of text */
        List<Integer> scores = Utils.readClassifications(scoreFile);
        Set<String> uniqueWords = new HashSet<>();
        List<String> texts = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(datasetFile))) {
            String line;
            while(br.ready()){
                line = removePunctuation(br.readLine().trim().toLowerCase());
                texts.add(line);
                uniqueWords.addAll(Arrays.asList(line.split("\\s+")));
            }
        }
        /* generate list of vectors */
        HashMap<String, Integer> indexedUniqueWords = Utils.createIndexedUniqueWords(uniqueWords);
        Iterator<String> textIterator = texts.iterator();
        Iterator<Integer> scoreIterator = scores.iterator();
        List<Vector> vectors = new LinkedList<>();
        while(textIterator.hasNext()){
            String text = textIterator.next();
            int score = scoreIterator.next();
            vectors.add(Vector.createVector(text, indexedUniqueWords, score));
        }
        return new TrainningSet(indexedUniqueWords, vectors);
    }
            
    public static List<Integer> readClassifications(File scoreFile) throws IOException{
        List<Integer> classifications = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(scoreFile))) {
            while(br.ready()){
                classifications.add(Integer.parseInt(br.readLine()));
            }
        }
        return classifications;
    }
}
