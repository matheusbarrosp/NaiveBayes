package naivebayes;

public class NaiveBayesResult {

	private int predicted;
	private Integer label;
	
	public NaiveBayesResult(int predicted, Integer label) {
		this.predicted = predicted;
		this.label = label;
	}
	
	public int getPredicted() {
		return this.predicted;
	}
	
	public Integer getLabel() {
		return this.label;
	}
}
