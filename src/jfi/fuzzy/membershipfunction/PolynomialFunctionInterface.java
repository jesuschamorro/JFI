package jfi.fuzzy.membershipfunction;

import java.util.List;

/**
 * Interface for a polynomial-based membership function. 
 * 
 * @param <D> the domain of the function.
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public interface PolynomialFunctionInterface<D> extends MembershipFunction<D> {
    /**
     * Returns the degree of the polynomial.
     *
     * @return the degree of the polynomial.
     */
    public int getPolynomialDegree();
    
    /**
     * Set new coeficients for this polynominal-based function.
     *
     * @param coeficients the new coeficients.
     */
    public void setCoeficients(double coeficients[]);

    /**
     * Returns the coeficients of the polynomial-based function.
     *
     * @return the coeficients of the polynomial-based function.
     */
    public double[] getCoeficients();
}
