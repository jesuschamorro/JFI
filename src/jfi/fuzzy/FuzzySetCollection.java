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
     *
     */
    public FuzzySetCollection() {
        super();
    }

    /**
     *
     * @param fuzzySets
     */
    public FuzzySetCollection(Collection<FuzzySet<Domain>> fuzzySets) {
        super(fuzzySets);
    }

    /**
     *
     * @param e
     * @return
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

        public double degree;
        public FuzzySet fuzzySet;

        public PossibilityDistributionItem(double degree, FuzzySet fuzzySet) {
            this.degree = degree;
            this.fuzzySet = fuzzySet;
        }
    }
}
