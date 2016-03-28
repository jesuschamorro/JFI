package jfi.fuzzy;

import jfi.fuzzy.membershipfunction.MembershipFunction;
import jfi.utils.Interval;

/**
 *
 * @author Jesús
 * @param <Domain>
 */
public class ContinuousFuzzySet<Domain extends Number> implements FuzzySet<Domain> {

    private String label;
    private final MembershipFunction<Domain> mfunction;

    /**
     *
     * @param label
     * @param mfunction
     */
    public ContinuousFuzzySet(String label, MembershipFunction<Domain> mfunction) {
        this.label = label;
        this.mfunction = mfunction;
    }

    /**
     *
     * @param mfunction
     */
    public ContinuousFuzzySet(MembershipFunction<Domain> mfunction) {
        this("",mfunction);
    }
    
    /**
     * Return the membership function of the fuzzy set
     *
     * @return the membership function of the fuzzy set
     */
    public MembershipFunction getMembershipFunction() {
        return mfunction;
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
     * Return the membership degree of the element <tt>e</tt> to the fuzzy set
     *
     * @param e an element of the fuzzy set domain
     * @return the membership degree
     */
    @Override
    public double getMembershipValue(Domain e) {
        return mfunction.apply(e);

    }

    /**
     * Return the alpha-cut of the fuzzy set for a given alpha
     *
     * @param alpha the alpha
     * @return the alpha-cut
     */
    @Override
    public Interval<Domain> getAlphaCut(double alpha) {
        //TODO. It will depend on the membership function
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Return the kernel of the fuzzy set
     *
     * @return the kernel of the fuzzy set
     */
    @Override
    public Interval<Domain> getKernel() {
        return getAlphaCut(1.0f);
    }

    /**
     * Return the support of the fuzzy set
     *
     * @return the support of the fuzzy set
     */
    @Override
    public Interval<Domain> getSupport() {
        return getAlphaCut(0.0f);
    }

}
