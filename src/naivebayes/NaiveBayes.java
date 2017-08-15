package naivebayes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import naivebayes.crossvalidation.*;
import naivebayes.crossvalidation.metrics.CrossvalidationResult;

public class NaiveBayes {

    public static final int POSITIVE = 1;
    public static final int NEUTRAL = 0;
    public static final int NEGATIVE = -1;

    private CrossvalidationResult crossvalidation;
    private TrainningSet trainningSet;
    
    private HashMap<Integer, Probability> map = new HashMap<>();
    private long posTexts;
    private long neuTexts;
    private long negTexts;

    private NaiveBayes(HashMap<Integer, Probability> map, long posTexts, long neuTexts, long negTexts, TrainningSet trainningSet, CrossvalidationResult crossvalidation) {
        this.map = map;
        this.posTexts = posTexts;
        this.neuTexts = neuTexts;
        this.negTexts = negTexts;
        this.crossvalidation = crossvalidation;
        this.trainningSet = trainningSet;
    }
    
    private NaiveBayes(HashMap<Integer, Probability> map, long posTexts, long neuTexts, long negTexts, TrainningSet trainingSet) {
        this(map, posTexts, neuTexts, negTexts, trainingSet, null);
    }
    
    public CrossvalidationResult getCrossvalidationResult(){
        return this.crossvalidation;
    }
    private static void calculateProbabilities(Collection<Probability> probabilities, long totalPos, long totalNeu, long totalNeg, long uniqueWords) {
        for (Probability prob : probabilities) {
            prob.pos = (prob.pos + 1.0) / (totalPos + uniqueWords);
            prob.neu = (prob.neu + 1.0) / (totalNeu + uniqueWords);
            prob.neg = (prob.neg + 1.0) / (totalNeg + uniqueWords);
        }
    }

    public static NaiveBayes train(TrainningSet trainningSet, boolean createsCrossvalidation) {
        HashMap<Integer, Probability> map = new HashMap<>();
        long totalPos = 0, totalNeu = 0, totalNeg = 0;
        long posTexts = 0, neuTexts = 0, negTexts = 0;
        for (Vector v : trainningSet.getVectors()) {
            for (Entry<Integer, Integer> entry : v.getDimensions().entrySet()) {
                if (!map.containsKey(entry.getKey())) {
                    map.put(entry.getKey(), new Probability(entry.getKey()));
                }
                if (v.getClassification() == 1) {
                    map.get(entry.getKey()).pos += entry.getValue();
                    totalPos += entry.getValue();
                } else if (v.getClassification() == 0) {
                    map.get(entry.getKey()).neu += entry.getValue();
                    totalNeu += entry.getValue();
                } else {
                    map.get(entry.getKey()).neg += entry.getValue();
                    totalNeg += entry.getValue();
                }
            }
            if (v.getClassification() == 1) {
                posTexts++;
            } else if (v.getClassification() == 0) {
                neuTexts++;
            } else {
                negTexts++;
            }
        }
        calculateProbabilities(map.values(), totalPos, totalNeu, totalNeg, map.size());
        if(createsCrossvalidation){
            CrossvalidationResult crossvalidation = CrossValidation.executeCrossvalidation(trainningSet.getVectors());
            crossvalidation.calculeMeans();
            return new NaiveBayes(map, posTexts, neuTexts, negTexts, trainningSet, crossvalidation);
        }
        return new NaiveBayes(map, posTexts, neuTexts, negTexts, trainningSet);
    }

    

    public NaiveBayesResult predict(Vector vector) {
        long total = this.posTexts + this.negTexts + this.neuTexts;
        double pos = Math.log(((double) this.posTexts / (double) total));
        double neu = Math.log(((double) this.neuTexts / (double) total));
        double neg = Math.log(((double) this.negTexts / (double) total));
        for (Entry<Integer, Integer> entry : vector.getDimensions().entrySet()) {
            if (this.map.containsKey(entry.getKey())) {
                pos +=  Math.log(this.map.get(entry.getKey()).pos);
                neu += Math.log(this.map.get(entry.getKey()).neu);
                neg += Math.log(this.map.get(entry.getKey()).neg);
            }
        }
        if (neu >= pos && neu >= neg) {
            return new NaiveBayesResult(NEUTRAL, vector.getClassification());
        } else if (pos >= neg) {
            return new NaiveBayesResult(POSITIVE, vector.getClassification());
        } else {
            return new NaiveBayesResult(NEGATIVE, vector.getClassification());
        }
    }
    
    public List<NaiveBayesResult> predict(List<Vector> testSet) {
        List<NaiveBayesResult> result = new ArrayList<>(testSet.size());
        for (Vector v : testSet) {
            result.add(this.predict(v));
        }
        return result;
    }
    
    public int classify(String text){
        Vector vector = Vector.createVector(text, this.trainningSet.getUniqueWordsIndexed(), null);
        return predict(vector).getPredicted();
    }

}
