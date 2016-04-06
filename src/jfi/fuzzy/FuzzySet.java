package jfi.fuzzy;
/**
 * An interface representing a fuzzy set on a given domain
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 * @param <Domain> 
 */
public interface FuzzySet<Domain> {

    /**
     * Return the label associated to the fuzzy set
     *
     * @return the label associated to the fuzzy set
     */
    public String getLabel();

    /**
     * Set the label associated to the fuzzy set
     *
     * @param label the new label
     */
    public void setLabel(String label);

    /**
     * Return the membership degree of the element <tt>e</tt> to the fuzzy set
     *
     * @param e an element of the fuzzy set domain
     * @return the membership degree
     */
    public double getMembershipValue(Domain e);

    /**
     * Return the alpha-cut of the fuzzy set for a given alpha
     *
     * @param alpha the alpha
     * @return the alpha-cut
     */
    public Object getAlphaCut(double alpha);

    /**
     * Return the kernel of the fuzzy set
     *
     * @return the kernel of the fuzzy set
     */
    public Object getKernel();

    /**
     * Return the support of the fuzzy set
     *
     * @return the support of the fuzzy set
     */
    public Object getSupport();

}
