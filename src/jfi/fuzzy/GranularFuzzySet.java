package jfi.fuzzy;

import java.util.Arrays;
import jfi.fuzzy.operators.TConorm;

/**
 * Class representing a fuzzy set defined as the union of other fuzzy sets.
 * 
 * There are not restrictions about the fuzzy sets, except that they have to
 * share the same domain. The union is performed by using a t-conorm, defined as
 * a data member of the object (by default, the maximum is used).
 *
 * @param <D> the domain of the fuzzy set.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class GranularFuzzySet<D> extends FuzzySetCollection<FuzzySet<D>,D> implements FuzzySet<D> {
    /**
     * The label associated to the fuzzy set.
     */
    protected String label;
    /**
     * The t-conorm used for the union of the single fuzzy sets.
     */
    protected TConorm tconorm;
    /**
     * The default t-norm used for the union.  
     */
    private static TConorm DEFAULT_TCONORM = TConorm.MAX;
        
    /**
     * Constructs an empty fuzzy set.
     *
     * @param label label of the fuzzy set.
     * @param tconorm t-conorm used for the union
     */
    public GranularFuzzySet(String label, TConorm tconorm) {
        this.label = label; 
        this.tconorm = tconorm;
    }
    
    /**
     * Constructs an empty fuzzy set with the
     * {@link jfi.fuzzy.operators.TConorm#MAX} as t-conorm.
     *
     * @param label label of the fuzzy set.
     */
    public GranularFuzzySet(String label) {
        this(label,DEFAULT_TCONORM);      
    }

    /**
     * Constructs an empty fuzzy set with an empty label and the
     * {@link jfi.fuzzy.operators.TConorm#MAX} as t-conorm.
     */
    public GranularFuzzySet() {
        this("", DEFAULT_TCONORM);
    }
    
    /**
     * Constructs a new granular fuzzy set containing the single fuzzy sets of
     * the specified collection.
     *
     * @param label label of the fuzzy set.
     * @param tconorm t-conorm used for the union
     * @param collection list of atomic fuzzy set to be placed into this
     * granular fuzzy set
     */
    public GranularFuzzySet(String label, TConorm tconorm, FuzzySet<D>... collection) {
        this(label,tconorm);
        this.addAll(Arrays.asList(collection));
    }
    
    /**
     * Constructs a new granular fuzzy set containing the atomic fuzzy sets of
     * the specified collection. By default, the empty label and the
     * {@link jfi.fuzzy.operators.TConorm#MAX} t-conorm are used.
     *
     * @param collection list of atomic fuzzy set to be placed into this
     * granular fuzzy set
     */
    public GranularFuzzySet(FuzzySet<D>... collection) {
        this("", DEFAULT_TCONORM,collection);
    }
    
    /**
     * Return the label associated to the fuzzy set
     *
     * @return the label associated to the fuzzy set
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Set the label associated to the fuzzy set
     *
     * @param label the new label
     */
    @Override
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Returns the t-conorm associated to the this granular fuzzy set.
     *
     * @return the the t-conorm associated to the this granular fuzzy set.
     */
    public TConorm getTConorm() {
        return tconorm;
    }

    /**
     * Set the t-conorm associated to the this granular fuzzy set.
     *
     * @param tconorm the new t-conorm
     */
    public void setTConorm(TConorm tconorm) {
        this.tconorm = tconorm;
    }

    /**
     * Returns the membership degree of the element <tt>e</tt> to the fuzzy set.
     *
     * @param e an element of the fuzzy set domain
     * @return the membership degree
     */
    @Override
    public double membershipDegree(D e) {
        double degree,output=0.0;
        for (FuzzySet fuzzySet : this) {
            degree = fuzzySet.membershipDegree(e);
            output = tconorm.apply(output, degree);
        }
        return output;
    }

}
