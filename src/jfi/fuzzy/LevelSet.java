package jfi.fuzzy;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class LevelSet {
    
    private final DiscreteFuzzySet fuzzyset;
    private final int levels[];
    private ArrayList<Double> degreeList;
    private int numLevels = 0;
    
    /**
     * 
     * @param fuzzyset 
     */
    public LevelSet(DiscreteFuzzySet fuzzyset){
        this.fuzzyset = fuzzyset;
        this.levels = new int[fuzzyset.size()];
        computeLevels();
    }
    
    /**
     * 
     */
    private void computeLevels() {
        Double i_degree, level_degree;
        int index;

        // First, the set of degrees is obtained (with no repeated elements)
        LinkedHashSet<Double> degreeSet = new LinkedHashSet<>();
        for (Object e : fuzzyset) {
            degreeSet.add((Double) ((Entry)e).getValue());
        }
        degreeList = new ArrayList(degreeSet);
        degreeList.sort(null); //natural ordering
        //Then, each degree will correspond to a level
        this.numLevels = degreeList.size();  
        for (int level = 0; level < numLevels; level++) {
            level_degree = degreeList.get(level);
            index = 0;
            for (Object e : fuzzyset) {
                i_degree = (Double) ((Entry) e).getValue();
                if (i_degree.equals(level_degree)) {
                    levels[index] = level;
                }
                index++;
            }
        }
    }
    
    /**
     * 
     * @return 
     */
    public int levels(){
        return this.numLevels;
    } 
    
    /**
     * 
     * @param level
     * @return 
     */
    public ArrayList level(int level) {
        ArrayList output = new ArrayList();
        int index = 0;
        for (Object e : fuzzyset) {
            if (levels[index] >= level) {
                output.add(((Entry)e).getKey());
            }
            index++;
        }
        return output;
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString(){
        String output = "";
        for(int level=0;level<this.numLevels;level++){
            output+="\nLevel "+level;
            output+=" (d>="+degreeList.get(level)+") : ";
            output+=level(level);
        }
        return output;
    }
    
}
