package naivebayes.crossvalidation;
import java.util.LinkedList;
import java.util.List;
import naivebayes.Vector;
import naivebayes.crossvalidation.metrics.CrossvalidationResult;
import naivebayes.NaiveBayes;
import naivebayes.NaiveBayesResult;
import naivebayes.TrainningSet;
import naivebayes.crossvalidation.metrics.ConfusionMatrix;

public class CrossValidation {

    private static List<List<Vector>> createPartitionsFromVectors(List<Vector> vectors, int n) {
        List<List<Vector>> partitions = new LinkedList<>();
        int partitionSize = vectors.size() / n;
        int resto = vectors.size() - (partitionSize * n);
        int inicio = 0, fim = partitionSize;
        for (int i = 0; i < n; i++) {
            if (i < resto) {
                fim++;
            }
            List<Vector> partition = new LinkedList<>();
            for (int j = inicio; j < fim; j++) {
                partition.add(vectors.get(j));
            }
            inicio = fim;
            fim += partitionSize;
            partitions.add(partition);
        }
        return partitions;
    }
    
    public static CrossvalidationResult executeCrossvalidation(List<Vector> vectors){
        List<List<Vector>> partitions = CrossValidation.createPartitionsFromVectors(vectors, 10);
        CrossvalidationResult crossvalidationResult  = new CrossvalidationResult();
        for(int i = 0; i < partitions.size(); i++){
            List<Vector> trainning = new LinkedList<>();
            List<Vector> test = partitions.get(i);
            for(int j = 0; j < partitions.size(); j++){
                if(i != j){
                    trainning.addAll(partitions.get(j));
                }
            }
            NaiveBayes model = NaiveBayes.train(new TrainningSet(null, trainning), false);
            List<NaiveBayesResult> result = model.predict(test);
            crossvalidationResult.addConfusionMatrix(ConfusionMatrix.create(result));
        }
        return crossvalidationResult;
    }
}
