/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naivebayes;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author elderjr
 */
public class TrainningSet {

    private HashMap<String, Integer> uniqueWordsIndexed;
    private List<Vector> vectors;
    
    public TrainningSet(HashMap<String, Integer> uniqueWordsIndexed, List<Vector> vectors){
        this.uniqueWordsIndexed = uniqueWordsIndexed;
        this.vectors = vectors;
    }

    /**
     * @return the uniqueWordsIndexed
     */
    public HashMap<String, Integer> getUniqueWordsIndexed() {
        return uniqueWordsIndexed;
    }

    /**
     * @return the vectors
     */
    public List<Vector> getVectors() {
        return vectors;
    }
    
    
}
