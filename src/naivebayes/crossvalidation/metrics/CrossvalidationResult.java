package naivebayes.crossvalidation.metrics;

import java.util.ArrayList;

public class CrossvalidationResult {

    private ArrayList<ConfusionMatrix> confusionMatrixs;
    private double meanAcc;
    private double meanMacroF1;
    private double meanPrecision[];
    private double meanRecall[];
    private boolean meanCalculated;

    public CrossvalidationResult() {
        this.confusionMatrixs = new ArrayList<>();
        this.meanCalculated = false;
    }

    public void addConfusionMatrix(ConfusionMatrix confusionMatrix) {
        this.confusionMatrixs.add(confusionMatrix);
        this.meanCalculated = false;
    }

    public ArrayList<ConfusionMatrix> getConfusionMatrixs() {
        return this.confusionMatrixs;
    }

    private void initMeanValues(){
        this.meanPrecision = new double [] {0.0, 0.0, 0.0};
        this.meanRecall = new double [] {0.0, 0.0, 0.0};
        this.meanAcc = 0.0;
        this.meanMacroF1 = 0.0;
    }
    
    public void calculeMeans() {
        initMeanValues();
        for (ConfusionMatrix matrix : this.confusionMatrixs) {
            if (!Double.isNaN(matrix.getAccuracy())) {
                this.meanAcc += matrix.getAccuracy();
            }
            if (!Double.isNaN(matrix.getMacroF1())) {
                this.meanMacroF1 += matrix.getMacroF1();
            }
            double recall[] = matrix.getRecall();
            double precision[] = matrix.getPrecision();
            for (int i = 0; i < 3; i++) {
                if (!Double.isNaN(recall[i])) {
                    this.meanRecall[i] += recall[i];
                }
                if (!Double.isNaN(precision[i])) {
                    this.meanPrecision[i] += precision[i];
                }
            }
        }
        meanAcc /= 10;
        meanMacroF1 /= 10;
        for (int i = 0; i < 3; i++) {
            meanRecall[i] /= 10;
            meanPrecision[i] /= 10;
        }
    }

    public double getAccuracy(){
        return this.meanAcc;
    }
    
    public double getMacroF1(){
        return this.meanMacroF1;
    }
    
    @Override
    public String toString(){
        StringBuilder string = new StringBuilder();
        int index = 1;
        for (ConfusionMatrix matrix : this.confusionMatrixs) {
            string.append("========== CROSS VALIDATION " + index + " ==========\n");
            string.append(matrix);
            index++;
        }
        string.append("========== MEAN RESULTS ==========\n");
        String classification;
        for (int j = 0; j < 3; j++) {
            if (j == 0) {
                classification = "negative";
            } else if (j == 1) {
                classification = "neutral";
            } else {
                classification = "positive";
            }
            string.append("Mean Precision for class " + classification + ": " + this.meanPrecision[j]+"\n");
            string.append("Mean Recall for class " + classification + ": " + this.meanRecall[j]+"\n");
        }
        string.append("Mean Accuracy: " + meanAcc+"\n");
        string.append("Mean MacroF1: " + meanMacroF1+"\n");
        return string.toString();
    }
}
