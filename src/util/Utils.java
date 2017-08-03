package util;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import naiveBayes.Vector;

public class Utils {
	
	private static final String PUNCTUATION_REGEX = "[!\"#$%&'()\\*\\+,-\\./:;<=>?@\\[\\]\\^_`{|}~]";
	
	public static List<Vector> loadVectorFile(String path) throws IOException{
		BufferedReader br =  new BufferedReader(new FileReader(new File(path)));
		List<Vector> vectors = new ArrayList<>();
		while(br.ready()) {
			vectors.add(stringToVector(br.readLine()));
		}
		br.close();
		return vectors;
	}
	
	private static Vector stringToVector(String stringVector){
		int classification;
		HashMap<Integer, Integer> dimensions = new HashMap<>();
		String splited[] = stringVector.split("\\s|:");
		classification = Integer.parseInt(splited[0]);
		try {
			for(int i = 1; i < splited.length; i+= 2) {
				if(i < splited.length) {
					dimensions.put(Integer.parseInt(splited[i]), Integer.parseInt(splited[i+1]));
				}else {
					//throws
				}
			}
		}catch(NumberFormatException ex) {
			//throws
		}
		return new Vector(classification, dimensions);
	}
	
	public static Set<String> readFileWordsToSet(final String fileName) {
		Set<String> set = new HashSet<>();
		try {
			//InputStream in = Utils.class.getResourceAsStream(fileName);
			File f = new File(fileName);			
			FileReader fr = new FileReader(f);
			BufferedReader input = new BufferedReader(fr);
			String line;
			while((line = input.readLine()) != null) {
				line = removePunctuation(line.trim().toLowerCase());
				for(String word: line.split("\\s+")){
					set.add(word);
				}
			}
			input.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
			//System.err.println("Error opening file: " + fileName);
			//System.exit(1989);
		}
		
		
		return set;
	}
		
	public static String removePunctuation(String text) {

		return text.replaceAll(PUNCTUATION_REGEX, " ");
	}
	
	public static LinkedList<String> readFileLinesProcessed(final String fileName) {
		LinkedList<String> list = new LinkedList<>();
		try {
			//InputStream in = Utils.class.getResourceAsStream(fileName);
			File f = new File(fileName);			
			FileReader fr = new FileReader(f);
			BufferedReader input = new BufferedReader(fr);
			String line;
			while((line = input.readLine()) != null) {
				line = removePunctuation(line.trim().toLowerCase());
				list.add(line);
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
			//System.err.println("Error opening file: " + fileName);
			//System.exit(1989);
		}
		
		
		return list;
	}
	
	public static LinkedList<String> readFileLines(final String fileName) {
		LinkedList<String> list = new LinkedList<>();
		try {
			//InputStream in = Utils.class.getResourceAsStream(fileName);
			File f = new File(fileName);			
			FileReader fr = new FileReader(f);
			BufferedReader input = new BufferedReader(fr);
			String line;
			while((line = input.readLine()) != null) {
				line = line.trim();
				list.add(line);
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
			//System.err.println("Error opening file: " + fileName);
			//System.exit(1989);
		}
		
		
		return list;
	}
}
