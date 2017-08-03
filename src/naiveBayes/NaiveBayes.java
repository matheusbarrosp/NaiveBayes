package naiveBayes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.plaf.ProgressBarUI;

public class NaiveBayes {
	
	private HashMap<Integer, Probability> map = new HashMap<>();
	private long posTexts;
	private long neuTexts;
	private long negTexts;
	
	private NaiveBayes(HashMap<Integer, Probability> map, long posTexts, long neuTexts, long negTexts) {
		this.map = map;
		this.posTexts = posTexts;
		this.neuTexts = neuTexts;
		this.negTexts = negTexts;
	}

	private static void calculateProbabilities(Collection<Probability> probabilities, long totalPos, long totalNeu, long totalNeg, long uniqueWords) {
		for(Probability prob : probabilities) {
			prob.pos = (prob.pos + 1.0) / (totalPos + uniqueWords);
			prob.neu = (prob.neu + 1.0) / (totalNeu + uniqueWords);
			prob.neg = (prob.neg + 1.0) / (totalNeg + uniqueWords);
		}
	}
	
	public static NaiveBayes train(List<Vector> trainingSet) {
		HashMap<Integer, Probability> map = new HashMap<>();
		long totalPos = 0, totalNeu = 0, totalNeg = 0;
		long posTexts = 0, neuTexts = 0, negTexts = 0;
		for(Vector v : trainingSet) {
			for(Entry<Integer, Integer> entry : v.getDimensions().entrySet()) {
				if(!map.containsKey(entry.getKey())) {
					map.put(entry.getKey(), new Probability(entry.getKey()));
				}
				if(v.getClassification() == 1) {
					map.get(entry.getKey()).pos += entry.getValue();
					totalPos += entry.getValue();
				}else if(v.getClassification() == 0) {
					map.get(entry.getKey()).neu += entry.getValue();
					totalNeu += entry.getValue();
				}else {
					map.get(entry.getKey()).neg += entry.getValue();
					totalNeg += entry.getValue();
				}
			}
			if(v.getClassification() == 1) {
				posTexts++;
			}else if(v.getClassification() == 0) {
				neuTexts++;
			}else {
				negTexts++;
			}
		}
		
		calculateProbabilities(map.values(), totalPos, totalNeu, totalNeg, map.size());
		return new NaiveBayes(map, posTexts, neuTexts, negTexts);
	}
	
	public NaiveBayesResult predict(Vector vector) {
		long total = this.posTexts + this.negTexts + this.neuTexts;
		double pos = ((double) this.posTexts/ (double) total);
		double neu = ((double) this.neuTexts/ (double) total);
		double neg = ((double) this.negTexts/ (double) total);
		for(Entry<Integer, Integer> entry : vector.getDimensions().entrySet()) {
			if(this.map.containsKey(entry.getKey())) {
				pos *= this.map.get(entry.getKey()).pos;
				neu *= this.map.get(entry.getKey()).neu;
				neg *= this.map.get(entry.getKey()).neg;
			}
		}
		if(neu >= pos && neu >= neg){
			return new NaiveBayesResult(0, vector.getClassification());
		}else if(pos >= neg) {
			return new NaiveBayesResult(1, vector.getClassification());
		}else {
			return new NaiveBayesResult(-1, vector.getClassification());
		}		
	}
	
	public List<NaiveBayesResult> predict(List<Vector> testSet) {
		List<NaiveBayesResult> result = new ArrayList<>(testSet.size());
		for(Vector v : testSet) {
			result.add(this.predict(v));
		}
		return result;
	}
	
}
