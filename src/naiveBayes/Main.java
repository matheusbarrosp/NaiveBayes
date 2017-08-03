package naiveBayes;

import util.CrossValidation;
import util.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Main {
	
	public static Vector createVector(String tweet, HashMap<String, Integer> map, String classification) {
		int max = map.size();
		String[] words = tweet.split("\\s+");
		Vector vector = new Vector(Integer.parseInt(classification.trim()));
		vector.addDimension(max);
		for(String word: words) {
			vector.incrementDimension(map.get(word));
		}
		return vector;
	}

	public static void createFile(String dataSetPath, String classificationPath, HashMap<String, Integer> map) {
		LinkedList<String> dataSet = util.Utils.readFileLinesProcessed(dataSetPath);
		LinkedList<String> classification = util.Utils.readFileLines(classificationPath);
		Vector[] vectors = new Vector[dataSet.size()];
		for(int i=0; i<vectors.length; i++) {
			vectors[i] = createVector(dataSet.get(i), map, classification.get(i));
		}
		try {
			FileWriter fw = new FileWriter(new File("inputVector.txt"));
			BufferedWriter bw = new BufferedWriter(fw);
			for(Vector vector: vectors) {
				bw.write(vector.toString());
				bw.newLine();
			}
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createFiles(String dataSetPath, String classificationPath, HashMap<String, Integer> map) {
		LinkedList<String> dataSet = util.Utils.readFileLinesProcessed(dataSetPath);
		LinkedList<String> classification = util.Utils.readFileLines(classificationPath);
		Vector[] vectors = new Vector[dataSet.size()];
		for(int i=0; i<vectors.length; i++) {
			vectors[i] = createVector(dataSet.get(i), map, classification.get(i));
		}
		List<Vector[]> partitions = CrossValidation.createPartitionsFromVectors(vectors, 10);
		CrossValidation.createTestFilesOfVector(partitions);
		CrossValidation.createTrainingFilesOfVector(partitions);
		
	}
	
	public static double[][] confusionMatrix(List<NaiveBayesResult> data){
		double[][] matrix = {{0.0, 0.0, 0.0},{0.0, 0.0, 0.0},{0.0, 0.0, 0.0}};
		for(NaiveBayesResult row: data) {
			if(row.getLabel() == -1.0 && row.getPredicted() == -1.0)
				matrix[0][0] += 1;
			else if(row.getLabel() == -1.0 && row.getPredicted() == 0.0)
				matrix[0][1] += 1;
			else if(row.getLabel() == -1.0 && row.getPredicted() == 1.0)
				matrix[0][2] += 1;
			else if(row.getLabel() == 0.0 && row.getPredicted() == -1.0)
				matrix[1][0] += 1;
			else if(row.getLabel() == 0.0 && row.getPredicted() == 0.0)
				matrix[1][1] += 1;
			else if(row.getLabel() == 0.0 && row.getPredicted() == 1.0)
				matrix[1][2] += 1;
			else if(row.getLabel() == 1.0 && row.getPredicted() == -1.0)
				matrix[2][0] += 1;
			else if(row.getLabel() == 1.0 && row.getPredicted() == 0.0)
				matrix[2][1] += 1;
			else
				matrix[2][2] += 1;
		}
		return matrix;
	}
	
	public static double[] calculatePrecision(double[][] matrix) {
		double[] precision = {0.0, 0.0, 0.0};
		for(int i=0; i<3; i++) {
			precision[i] = matrix[i][i] / (matrix[i][0] + matrix[i][1] + matrix[i][2]);
		}
		return precision;
	}
	
	public static double[] calculateRecall(double[][] matrix) {
		double[] recall = {0.0, 0.0, 0.0};
		for(int i=0; i<3; i++) {
			recall[i] = matrix[i][i] / (matrix[0][i] + matrix[1][i] + matrix[2][i]);
		}
		return recall;
	}
	
	public static double calculateMacroF1(double[] recall, double[] precision) {
		double[] f1 = {0.0, 0.0, 0.0};
		double macroF1 = 0;
		int counter = 0;
		for(int i=0; i<3; i++) {
			f1[i] = (2*precision[i]*recall[i]) / (precision[i] + recall[i]);
			if(!Double.isNaN(f1[i])) {
				macroF1 += f1[i];
				counter++;
			}
		}
		return macroF1 / counter;
	}
	
	public static void printConfusionMatrix(double[][] matrix) {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				System.out.print(matrix[i][j] + "\t");
			}
			System.out.println();
		}
		double[] precision = calculatePrecision(matrix);
		double[] recall = calculateRecall(matrix);
		double macroF1 = calculateMacroF1(precision, recall);
		System.out.println("Precision: -1: " + precision[0] + ", 0: " + precision[1] + ", 1: " + precision[2]);
		System.out.println("Recall: -1: " + recall[0] + ", 0: " + recall[1] + ", 1: " + recall[2]);
		System.out.println("MacroF1: " + macroF1);
 	}
	
	public static void main(String[] args) throws IOException {
		String dataSetPath = "stanford_tweets.txt";
		String classificationPath = "stanford_tweets_so_score.txt";
		//String dataSetPath = "sentistrength_youtube.txt";
		//String classificationPath = "sentistrength_youtube_so_score.txt";
		Set<String> uniqueWordsSet = util.Utils.readFileWordsToSet(dataSetPath);
		HashMap<String, Integer> map = new HashMap<>();
		int index = 1;
		for(String word: uniqueWordsSet) {
			map.put(word, index);
			index++;
		}
		createFiles(dataSetPath, classificationPath, map);
		//createFile(dataSetPath, classificationPath, map);
		
		
		String trainingDir = "training/";
		String testDir = "test/";
		
		double[] accuracy = new double[10];
		double[][] precision = new double[10][3];
		double[][] recall = new double[10][3];
		double[] macroF1 = new double[10];
		
		for(int i = 0; i < 10; i++) {
			List<Vector> trainingSet = Utils.loadVectorFile(trainingDir+"training"+i+".txt");
			List<Vector> testSet = Utils.loadVectorFile(testDir+"test"+i+".txt");
			NaiveBayes model = NaiveBayes.train(trainingSet); 
			List<NaiveBayesResult> predictionAndLabel = model.predict(testSet);
			double[][] matrix = confusionMatrix(predictionAndLabel);
			printConfusionMatrix(matrix);
			precision[i] = calculatePrecision(matrix);
			recall[i] = calculateRecall(matrix);
			macroF1[i] = calculateMacroF1(precision[i], recall[i]);
		}
		
		double meanAcc = 0;
		double meanMacroF1 = 0;
		double[] meanPrecision = {0.0, 0.0, 0.0};
		double[] meanRecall = {0.0, 0.0, 0.0};
		for(int i=0; i<10; i++) { 
			meanAcc += accuracy[i];
			meanMacroF1 += macroF1[i];
			for(int j=0; j<3; j++) {
				meanRecall[j] += recall[i][j];
				meanPrecision[j] += precision[i][j];
			}
		}
		meanAcc /= 10;
		meanMacroF1 /= 10;
		
		int classification;
		for(int j=0; j<3; j++) {
			meanRecall[j] /= 10;
			meanPrecision[j] /= 10;
			if(j==0) classification = -1;
			else if(j==1) classification = 0;
			else classification = 1;
			System.out.println("Mean Precision for class " + classification + ": " + meanPrecision[j]);
			System.out.println("Mean Recall for class " + classification + ": " + meanRecall[j]);
		}
		System.out.println("Mean MacroF1: " + meanMacroF1);
		
	}
}