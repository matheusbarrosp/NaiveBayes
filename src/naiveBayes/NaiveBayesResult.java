package naiveBayes;

public class NaiveBayesResult {

	private int predicted;
	private int label;
	
	public NaiveBayesResult(int predicted, int label) {
		this.predicted = predicted;
		this.label = label;
	}
	
	public int getPredicted() {
		return this.predicted;
	}
	
	public int getLabel() {
		return this.label;
	}
}
