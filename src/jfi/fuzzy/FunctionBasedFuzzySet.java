package jfi.fuzzy;

import jfi.fuzzy.membershipfunction.MembershipFunction;
import jfi.utils.Interval;
import jfi.utils.JFIMath;

/**
 * Class representing a fuzzy set defined on the basis of given membership
 * function. It is suitable for fuzzy sets on a continous domain (although it
 * can also be used for the discrete case with the appropiate function).
 *
 * <p>
 * The membership function of a fuzzy set is a generalization of the indicator
 * function in classical sets. They were introduced by Zadeh in the first paper
 * on fuzzy sets (1965), where he proposed using a membership function (with a
 * range covering the interval (0,1)) operating on the domain of all possible
 * values.
 * 
 * Thus, for any set X, a membership function on X is any function from X to the
 * real unit interval [0,1]. The membership function which represents a fuzzy
 * set ~A is usually denoted by μA. For an element x of X, the value μA(x) is
 * called the membership degree of x in the fuzzy set ~A. The value 0 means that
 * x is not a member of the fuzzy set; the value 1 means that x is fully a
 * member of the fuzzy set. The values between 0 and 1 characterize fuzzy
 * members, which belong to the fuzzy set only partially.
 * </p>
 * 
 * @see jfi.fuzzy.membershipfunction.MembershipFunction
 * 
 * @param <D> the domain of the fuzzy set.
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FunctionBasedFuzzySet<D> implements FuzzySet<D> {
    /**
     * The label associated to the fuzzy set.
     */
    protected String label;
    /**
     * The membership function associated to the fuzzy set.
     */
    protected MembershipFunction<D> mfunction;

    /**
     * Constructs a fuzzy set on the basis of a given membership fuction.
     * 
     * @param label the label associated to the fuzzy set.
     * @param mfunction the membership function associated to the fuzzy set.
     */
    public FunctionBasedFuzzySet(String label, MembershipFunction<D> mfunction) {
        this.label = label;
        this.mfunction = mfunction;
    }

    /**
     * Constructs a fuzzy set on the basis of a given membership fuction. By
     * default, an empty label is used.
     *
     * @param mfunction the membership function associated to the fuzzy set.
     */
    public FunctionBasedFuzzySet(MembershipFunction<D> mfunction) {
        this("", mfunction);
    }

    /**
     * Returns the membership function of the fuzzy set.
     *
     * @return the membership function of the fuzzy set.
     */
    public MembershipFunction getMembershipFunction() {
        return mfunction;
    }

    /**
     * Set a new membership function for the fuzzy set.
     *
     * @param mfunction the new membership function.
     */
    public void setMembershipFunction(MembershipFunction mfunction) {
        this.mfunction = mfunction;
    }

    /**
     * Returns the label associated to the fuzzy set.
     *
     * @return the label associated to the fuzzy set.
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Set the label associated to the fuzzy set.
     *
     * @param label the new label.
     */
    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the membership degree of the element <tt>e</tt> to this fuzzy
     * set.
     *
     * @param e an element of the fuzzy set domain.
     * @return the membership degree.
     */
    @Override
    public double membershipDegree(D e) {
        return mfunction.apply(e);

    }

    /**
     * Returns the alpha-cut of the fuzzy set for a given alpha.
     *
     * @param alpha the alpha.
     * @return the alpha-cut.
     */
    @Override
    public Interval<Number> alphaCut(double alpha) {
        return mfunction.getAlphaCut(alpha);
    }

    /**
     * Returns the kernel of the fuzzy set.
     *
     * @return the kernel of the fuzzy set.
     */
    @Override
    public Interval<Number> kernel() {
        return alphaCut(1.0f);
    }

    /**
     * Returns the support of the fuzzy set.
     *
     * @return the support of the fuzzy set.
     */
    @Override
    public Interval<Number> support() {
        return alphaCut(0.0 + JFIMath.EPSILON);
    }

}
