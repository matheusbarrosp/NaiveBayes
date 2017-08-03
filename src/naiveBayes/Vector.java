package naiveBayes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Vector {
	private int classification;
	private HashMap<Integer, Integer> dimensions;
	
	public Vector(int classification) {
		this.setClassification(classification);
		this.setDimensions(new HashMap<>());
	}
	
	public Vector(int classification, HashMap<Integer, Integer> dimensions) {
		this.classification = classification;
		this.dimensions = dimensions;
	}

	public int getClassification() {
		return classification;
	}

	public void setClassification(int classification) {
		this.classification = classification;
	}

	HashMap<Integer, Integer> getDimensions() {
		return dimensions;
	}

	void setDimensions(HashMap<Integer, Integer> dimensions) {
		this.dimensions = dimensions;
	}
	
	public void addDimension(int index) {
		this.dimensions.put(index, 0);
	}
	
	public void incrementDimension(int index) {
		if(this.dimensions.containsKey(index)) {
			int value = this.dimensions.get(index);
			this.dimensions.put(index, value+1);
		}else {
			this.dimensions.put(index, 1);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.classification);       
		Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>(this.dimensions);
		for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
		    builder.append(" ").append(entry.getKey()).append(":").append(entry.getValue());
		}
		return builder.toString();
	}
}
