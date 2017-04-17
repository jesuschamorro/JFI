package jfi.fuzzy;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Finite collection of fuzzy sets on a given domain.
 * 
 * <p>
 * There are not restrictions about the fuzzy sets (except that they have to
 * share the same domain) nor about the domain covering. In particular, it is no
 * mandatory to cover the whole domain (there may be elements in the domain with
 * zero membership degree to all the fuzzy sets in the collection), nor to be
 * disjoint (there may be elements with membership degree 1 to more than one
 * fuzzy set).
 * 
 * <p>
 * Based on the above, a fuzzy set collection is not necessarily a fuzzy
 * partition, since covering and disjointy are needed in that case.
 * Nevertheless, this class can be used for fuzzy partition modelling if the
 * above properties are guaranteed externally.
 *
 * @param <T> the type of the elements (fuzzy sets) in this collection. By 
 * default, {@link jfi.fuzzy.FuzzySet} is assumed (the more general case).
 * @param <D> the domain of the fuzzy sets.
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzySetCollection<T extends FuzzySet<D>,D> extends ArrayList<T> {
    // The type 'D' is not infered automatically if we use only the type
    // 'T extends FuzzySet<D>' in the class definition (so we have to duplicate
    // it, although it looks odd). We could think of using only 'D' and declare
    // the array list as 'ArrayList<FuzzySet<D>'; the problem is that, in any
    // new subclass, the methods working on the collection (add, foreach, etc.)
    // will require a generic FuzzySet (without being able to specify a more
    // concrete subclass and being often necessary a casting).
    
    /**
     * Constructs an empty fuzzy set collection.
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
     * @throws NullPointerException if the specified collection is null.
     */
    public FuzzySetCollection(Collection<T> fuzzySets) { //(Collection<FuzzySet<D>>
        super(fuzzySets);
    }

    /**
     * Returns the possibility distribution for the given element.
     * 
     * @param e the element.
     * @return the possibility distribution.
     */
    public ArrayList<PossibilityDistributionItem> getPossibilityDistribution(D e) {
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
     * Inner class for representing an item of a possibility distribution.
     */
    public class PossibilityDistributionItem {
        /**
         * Membership degree to the fuzzy set.
         */
        public double degree;
        
        /**
         * The fuzzy set
         */
        public FuzzySet fuzzySet;

        /**
         * Constructs a new possibility distribution item.
         * 
         * @param degree the degree.
         * @param fuzzySet the fuzzy set.
         */
        public PossibilityDistributionItem(double degree, FuzzySet fuzzySet) {
            this.degree = degree;
            this.fuzzySet = fuzzySet;
        }
        
        /**
         * Returns a string representation of this item.
         *
         * @return a string representation of this item
         */
        @Override
        public String toString() {
            return degree+"/"+fuzzySet.getLabel();
        }
    }
}
