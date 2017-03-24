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
     * Inner ordered list with all the (non-repeated) membeship values in the
     * source fuzzy set.
     */
    private ArrayList<Double> degreeList;
    /**
     * Number of levels in this level set. It will be corrrespond to the
     * {@link #degreeList} size.
     */
    private int numLevels;
    /**
     * Inner list where, for each element in the fuzzy set, we store "its
     * level". 
     * 
     * In this implementation, for efficiency reasons, we do not store an
     * alpha-cut "object" for each level (that is, we do not use a
     * <code>List<Set></code> in order to call the
     * {@link jfi.fuzzy.DiscreteFuzzySet#alphaCut(double)}. Instead, we will
     * store the (initial) level of each element (this information will be used
     * to generate, when required, the set associated to a given level).
     */
    private final int levels[];
    
    /**
     * Constructs a level set associated to the given fuzzy set.
     * 
     * @param fuzzyset the source fuzzy set. 
     */
    public LevelSet(DiscreteFuzzySet fuzzyset){
        this.fuzzyset = fuzzyset;
        this.levels = new int[fuzzyset.size()];
        computeLevels();
    }
   
    /**
     * Computes the level sets from the source fuzzy set.
     */
    private void computeLevels() {
        Double i_degree, level_degree;
        int index;

        // First, the set of degrees is obtained (with no repeated elements)
        LinkedHashSet<Double> degreeSet = new LinkedHashSet<>();
        for (Object e : this.fuzzyset) {
            degreeSet.add((Double) ((Entry)e).getValue());
        }
        degreeList = new ArrayList(degreeSet);
        degreeList.sort(null); // Natural ordering
        // Then, each different degree will correspond to a level (an alpha-cut)
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
     * Returns the number of levels in this level set.
     * 
     * @return the number of levels.
     */
    public int levels(){
        return this.numLevels;
    } 
    
    /**
     * Returns the set (alpha-cut) at the specified level in this level set.
     * 
     * @param level index of the level to return.
     * @return the set (alpha-cut) at the specified level.
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
     * Testear: si OK, no hace falta el vector levels[] (solo el cálculo de 
     * degreeList)
     * 
     * @param level
     * @return 
     */
    public Set<D> getLevel(int level){
        double degree = this.degreeList.get(level);
        return this.fuzzyset.alphaCut(degree);
    }
    
    /**
     * Returns a string representation of this objetc.
     *
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
        String output = "";
        for (int level = 0; level < this.numLevels; level++) {
            output += "\nLevel " + level;
            output += " (d>=" + degreeList.get(level) + ") : ";
            output += level(level);
        }
        return output;
    }
    
    public String toString2() {
        String output = "";
        for (int level = 0; level < this.numLevels; level++) {
            output += "\nLevel " + level;
            output += " (d>=" + degreeList.get(level) + ") : ";
            output += getLevel(level);
        }
        return output;
    }

}
