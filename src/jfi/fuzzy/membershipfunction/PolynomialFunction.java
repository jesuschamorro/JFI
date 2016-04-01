package jfi.fuzzy.membershipfunction;

/**
 *
 * @author Jesús Chamorro Martínez <jesus@decsai.ugr.es>
 * @param <Domain>
 */
public interface PolynomialFunction<Domain> extends MembershipFunction<Domain> {

    /**
     * Return the degree of the polynomial
     *
     * @return the degree of the polynomial
     */
    public int getPolynomialDegree();

    /**
     * Set new coeficients for this polynominal function
     *
     * @param coeficients the new coeficients
     */
    public void setCoeficients(double coeficients[]);

    /**
     * Return the coeficients of the polynomial
     *
     * @return the coeficients of the polynomial
     */
    public double[] getCoeficients();
}
