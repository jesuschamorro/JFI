package jfi.fuzzy;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Level set representation of a {@link jfi.fuzzy.DiscreteFuzzySet}, where each
 * level will correspond to an alpha-cut. The number of levels will be given by
 * the number of different membership degrees in the discrete fuzzy set.
 * 
 * @param <D> the domain of the fuzzy set.
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class LevelSet<D> {
    /**
     * The source fuzzy set associated to this level set.
     */
    private final DiscreteFuzzySet<D> fuzzyset;
    /**
     * Ordered list with all the (non-repeated) membeship values in the source
     * fuzzy set.
     */
    private ArrayList<Double> degreeList;
    
    /**
     * Constructs a level set associated to the given fuzzy set.
     * 
     * @param fuzzyset the source fuzzy set. 
     */
    public LevelSet(DiscreteFuzzySet fuzzyset){
        this.fuzzyset = fuzzyset;
        this.setDegreeList();
    }
   
    /**
     * Calculates the ordered list with all the (non-repeated) membeship values
     * in the source fuzzy set
     */
    private void setDegreeList() {        
        // First, the set of degrees is obtained (with no repeated elements)
        LinkedHashSet<Double> degreeSet = new LinkedHashSet<>();
        for (Object e : this.fuzzyset) {
            degreeSet.add((Double) ((Entry)e).getValue());
        }
        // Then, it is sorted using the natural ordering
        degreeList = new ArrayList(degreeSet);
        degreeList.sort(null); // Natural ordering
    }
    
    /**
     * Returns the number of levels in this level set.
     * 
     * @return the number of levels.
     */
    public int size(){
        return degreeList.size();
    } 
    
    /**
     * Returns the set (alpha-cut) at the specified level in this level set.
     * 
     * @param level index of the level to return.
     * @return the set (alpha-cut) at the specified level.
     */
    public Set<D> getLevel(int level){
        double degree = degreeList.get(level);
        return fuzzyset.alphaCut(degree);
    }
    
    /**
     * Returns a string representation of this objetc.
     *
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
        String output = "";
        for (int level = 0; level < this.size(); level++) {
            output += "\nLevel " + level;
            output += " (d>=" + degreeList.get(level) + ") : ";
            output += getLevel(level);
        }
        return output;
    }

}
