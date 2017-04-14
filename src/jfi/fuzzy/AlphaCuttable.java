package jfi.fuzzy;

import jfi.utils.JFIMath;

/**
 * Class representing an alpha-cut generator. Given a fuzzy set, an alpha-cut is
 * the set of domain elements which have a membership degree greater than or
 * equal to alpha. Thus, and alpha-cut generator should be a {@link FuzzySet}
 * object (the usual case) or be related to one (for example, a
 * {@link jfi.fuzzy.membershipfunction.MembershipFunction} object).
 * 
 * @see FuzzySet
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public interface AlphaCuttable {
    /**
     * Returns the alpha-cut of the fuzzy set.
     *
     * @param <R> the type of the alpha-cut.
     * @param alpha the alpha.
     * @return the alpha-cut.
     */
    public <R> R alphaCut(double alpha);

    /**
     * Returns the kernel of the fuzzy set.
     *
     * @param <R> the type of the alpha-cut.
     * @return the kernel of the fuzzy set.
     */
    default public <R> R kernel(){
        return alphaCut(1.0f);
    }

    /**
     * Returns the support of the fuzzy set.
     *
     * @param <R> the type of the alpha-cut.
     * @return the support of the fuzzy set.
     */
    default public <R> R support(){
        return alphaCut(0.0 + JFIMath.EPSILON);
    }
}
