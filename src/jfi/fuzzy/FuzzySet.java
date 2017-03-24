package jfi.fuzzy;

import jfi.utils.JFIMath;

/**
 * An interface representing a fuzzy set on a given domain.
 *
 * Fuzzy sets are sets whose elements have degrees of membership. They were
 * introduced by Lotfi A. Zadeh in 1965 as an extension of the classical notion
 * of set. In classical set theory, the membership of elements in a set is
 * assessed in binary terms according to a bivalent condition (i.e, an element
 * either belongs or does not belong to the set). By contrast, fuzzy set theory
 * permits the gradual assessment of the membership of elements in a set; this
 * is described with the aid of a membership function valued in the real unit
 * interval [0, 1]. Fuzzy sets generalize classical sets, since the indicator
 * functions of classical sets are special cases of the membership functions of
 * fuzzy sets, if the latter only take values 0 or 1. In fuzzy set theory,
 * classical bivalent sets are usually called crisp sets.
 *
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 * @param <D> the domain of the fuzzy set.
 */
public interface FuzzySet<D> {

    /**
     * Returns the label associated to the fuzzy set.
     *
     * @return the label associated to the fuzzy set.
     */
    public String getLabel();

    /**
     * Set the label associated to the fuzzy set.
     *
     * @param label the new label.
     */
    public void setLabel(String label);

    /**
     * Returns the membership degree of the element <tt>e</tt> to the fuzzy set.
     *
     * @param e an element of the fuzzy set domain.
     * @return the membership degree.
     */
    public double membershipDegree(D e);

    /**
     * Returns the alpha-cut of the fuzzy set for a given alpha.
     *
     * @param <R> the type of the alpha-cut.
     * @param alpha the alpha.
     * @return the alpha-cut.
     */
    public <R> R alphaCut(double alpha);

    /**
     * Returns the kernel of the fuzzy set.
     *
     * @param <R> the type of the kernel.
     * @return the kernel of the fuzzy set.
     */
    default public <R> R kernel(){
        return alphaCut(1.0f);
    }

    /**
     * Returns the support of the fuzzy set.
     *
     * @param <R> the type of the support.
     * @return the support of the fuzzy set.
     */
    default public <R> R support(){
        return alphaCut(0.0 + JFIMath.EPSILON);
    }
}
