package jfi.fuzzy.cardinal;

import jfi.fuzzy.DiscreteFuzzySet;

/**
 * Class representing the fuzzy cardinal sigma-count.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class SigmaCount {

    /**
     * Sigma-count value
     */
    private Double sigma;

    /**
     * Constructs a new sigma-count cardinal
     *
     * @param set the fuzzy set
     */
    public SigmaCount(DiscreteFuzzySet set) {
        sigma = 0.0;
        for (Object e : set) {
            sigma += set.membershipDegree(e);
        }
    }

    /**
     * Return the sigma-count value
     *
     * @return the sigma-count value
     */
    public Double getValue() {
        return sigma;
    }

    /**
     * Returns a string representation of this cardinal.
     *
     * @return a string representation of this cardinal.
     */
    @Override
    public String toString() {
        return sigma.toString();
    }
}
