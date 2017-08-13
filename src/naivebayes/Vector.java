package naivebayes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Vector {

    private Integer classification;
    private HashMap<Integer, Integer> dimensions;

    public Vector(Integer classification) {
        this.classification = classification;
        this.dimensions = new HashMap<>();
    }

    public Vector(int classification, HashMap<Integer, Integer> dimensions) {
        this.classification = classification;
        this.dimensions = dimensions;
    }

    public Integer getClassification() {
        return classification;
    }

    public HashMap<Integer, Integer> getDimensions() {
        return dimensions;
    }

    public void addDimension(int index) {
        this.dimensions.put(index, 0);
    }

    public void incrementDimension(int index) {
        if (this.dimensions.containsKey(index)) {
            int value = this.dimensions.get(index);
            this.dimensions.put(index, value + 1);
        } else {
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

    public static Vector createVector(String text, HashMap<String, Integer> map, Integer classification) {
        int max = map.size();
        String[] words = text.split("\\s+");
        Vector vector = new Vector(classification);
        vector.addDimension(max);
        for (String word : words) {
            if(map.containsKey(word)){
                vector.incrementDimension(map.get(word));
            }
        }
        return vector;
    }
}
