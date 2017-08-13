package naivebayes.crossvalidation.metrics;

import java.util.Iterator;
import java.util.List;
import naivebayes.NaiveBayes;
import naivebayes.NaiveBayesResult;

public class ConfusionMatrix {
    private int matrix[][];
    private double[] precision;
    private boolean calculatedPrecision;
    private double[] recall;
    private boolean calculatedRecall;
    private double macroF1;
    private boolean calculatedMacroF1;
    private double accuracy;
    private boolean calculatedAccuracy;

    public ConfusionMatrix() {
        this.matrix = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        this.calculatedPrecision = false;
        this.calculatedRecall = false;
        this.calculatedMacroF1 = false;
        this.calculatedAccuracy = false;
    }

    public void increment(int i, int j) {
        this.matrix[i][j]++;
        this.calculatedPrecision = false;
        this.calculatedRecall = false;
        this.calculatedMacroF1 = false;
        this.calculatedAccuracy = false;
    }

    private double calculeAccuracy() {
        int diagonalPrincipal = 0;
        int total = 0;
        for (int i = 0; i < 3; i++) {
            diagonalPrincipal += matrix[i][i];
            total += matrix[i][0] + matrix[i][1] + matrix[i][2];
        }
        return (double) diagonalPrincipal / (double) total;
    }

    private double[] calculePrecision() {
        double[] precision = {0.0, 0.0, 0.0};
        for (int i = 0; i < 3; i++) {
            precision[i] = (double) matrix[i][i] / (double) (matrix[i][0] + matrix[i][1] + matrix[i][2]);
        }
        return precision;
    }

    private double[] calculeRecall() {
        double[] recall = {0.0, 0.0, 0.0};
        for (int i = 0; i < 3; i++) {
            recall[i] = (double) matrix[i][i] / (double) (matrix[0][i] + matrix[1][i] + matrix[2][i]);
        }
        return recall;
    }

    private double calculeMacroF1(double precision[], double recall[]) {
        double[] f1 = {0.0, 0.0, 0.0};
        double macroF1 = 0;
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            f1[i] = (double) (2 * precision[i] * recall[i]) / (double) (precision[i] + recall[i]);
            if (!Double.isNaN(f1[i])) {
                macroF1 += f1[i];
                counter++;
            }
        }
        return macroF1 / (double) counter;
    }

    public double[] getPrecision() {
        if (!this.calculatedPrecision) {
            this.precision = calculePrecision();
        }
        return this.precision;
    }

    public double[] getRecall() {
        if (!this.calculatedRecall) {
            this.recall = calculeRecall();
        }
        return this.recall;
    }

    public double getMacroF1() {
        if (!this.calculatedMacroF1) {
            this.macroF1 = calculeMacroF1(getPrecision(), getRecall());
        }
        return this.macroF1;
    }

    public double getAccuracy() {
        if (!this.calculatedAccuracy) {
            this.accuracy = calculeAccuracy();
        }
        return this.accuracy;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                string.append(matrix[i][j]).append("\t");
            }
            string.append("\n");
        }
        double[] precision = getPrecision();
        double[] recall = getRecall();
        double macroF1 = getMacroF1();
        string.append("Precision to Negative: " + precision[0] + ", Neutral: " + precision[1] + ", Positive: " + precision[2]).append("\n");
            string.append("Recall to Negative:" + recall[0] + ", Neutral: " + recall[1] + ", Positive: " + recall[2]).append("\n");
        string.append("MacroF1: " + macroF1).append("\n");
        return string.toString();
    }

    public static ConfusionMatrix create(List<NaiveBayesResult> results) {
        ConfusionMatrix confusionMatrix = new ConfusionMatrix();
        Iterator<NaiveBayesResult> resultIterable = results.iterator();
        while (resultIterable.hasNext()) {
            NaiveBayesResult result = resultIterable.next();
            int predicted = result.getPredicted();
            int label = result.getLabel();
            if (predicted == NaiveBayes.NEGATIVE && label == NaiveBayes.NEGATIVE) {
                confusionMatrix.increment(0, 0);
            } else if (predicted == NaiveBayes.NEGATIVE && label == NaiveBayes.NEUTRAL) {
                confusionMatrix.increment(0, 1);
            } else if (predicted == NaiveBayes.NEGATIVE && label == NaiveBayes.POSITIVE) {
                confusionMatrix.increment(0, 2);
            } else if (predicted == NaiveBayes.NEUTRAL && label == NaiveBayes.NEGATIVE) {
                confusionMatrix.increment(1, 0);
            } else if (predicted == NaiveBayes.NEUTRAL && label == NaiveBayes.NEUTRAL) {
                confusionMatrix.increment(1, 1);
            } else if (predicted == NaiveBayes.NEUTRAL && label == NaiveBayes.POSITIVE) {
                confusionMatrix.increment(1, 2);
            } else if (predicted == NaiveBayes.POSITIVE && label == NaiveBayes.NEGATIVE) {
                confusionMatrix.increment(2, 0);
            } else if (predicted == NaiveBayes.POSITIVE && label == NaiveBayes.NEUTRAL) {
                confusionMatrix.increment(2, 1);
            } else {
                confusionMatrix.increment(2, 2);
            }
        }
        return confusionMatrix;
    }
}
