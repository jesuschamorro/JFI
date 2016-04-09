package jfi.fuzzy;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Finite collection of fuzzy sets
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 * @param <Domain>
 */
public class FuzzySetCollection<Domain> extends ArrayList<FuzzySet<Domain>> {

    /**
     * Constructs an empty fuzzy set collection
     */
    public FuzzySetCollection() {
        super();
    }

    /**
     * Constructs a new fuzzy set collection containing the elements of the 
     * specified collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param fuzzySets the collection whose elements are to be placed into this 
     * fuzzy sets collection.
     * @throws NullPointerException if the specified collection is null
     */
    public FuzzySetCollection(Collection<FuzzySet<Domain>> fuzzySets) {
        super(fuzzySets);
    }

    /**
     * Return the possibility distribution of the specified element.
     * 
     * @param e the element
     * @return the possibility distribution of the specified element.
     */
    public ArrayList<PossibilityDistributionItem> getPossibilityDistribution(Domain e) {
        double degree;
        ArrayList<PossibilityDistributionItem> output = new ArrayList();

        for (FuzzySet fuzzySet : this) {
            degree = fuzzySet.membershipDegree(e);
            if (degree > 0.0) {
                output.add(new PossibilityDistributionItem(degree, fuzzySet));
            }
        }
        return output;
    }

    /**
     * Item of a possibility distribution
     */
    public class PossibilityDistributionItem {
        /**
         * Membership degree tho the fuzzy set
         */
        public double degree;
        
        /**
         * The fuzzy set
         */
        public FuzzySet fuzzySet;

        /**
         * Constructs a new possibility distribution item
         * 
         * @param degree degree 
         * @param fuzzySet fuzzy set
         */
        public PossibilityDistributionItem(double degree, FuzzySet fuzzySet) {
            this.degree = degree;
            this.fuzzySet = fuzzySet;
        }
    }
}
