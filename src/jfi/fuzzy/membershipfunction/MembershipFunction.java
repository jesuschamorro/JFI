package jfi.fuzzy.membershipfunction;

import java.util.function.Function;
/**
 * 
 * Class representing a membership function of a fuzzy set. 
 * 
 * For any set X, a membership function on X is any function from X to the real
 * unit interval [0,1]. The value 0 means that x is not a member of the fuzzy
 * set; the value 1 means that x is fully a member of the fuzzy set. The values
 * between 0 and 1 characterize fuzzy members, which belong to the fuzzy set
 * only partially.
 * 
 * @param <D> domain of the membership function
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public interface MembershipFunction<D> extends Function<D, Double> {
    /**
     * Returns an alpha-cut associated to the membership function
     *
     * @param <R> the result type.
     * @param alpha the alpha value
     * @return the alpha-cut
     */
    public <R> R getAlphaCut(double alpha);
}
