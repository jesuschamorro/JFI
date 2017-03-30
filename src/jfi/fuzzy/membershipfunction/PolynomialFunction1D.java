package jfi.fuzzy.membershipfunction;

import java.security.InvalidParameterException;
import jfi.utils.Interval;

/**
 * Class representing an unidimensional polynomial-based membership function. It 
 * depends on the set of coefficients of the polynomial function, as given by:
 * <br><br>
 * <pre>
 *                  | 0, polyn(x)&lt;0
 * f(x;an,...,a0) = | polyn(x), 0&lt;polyn(x)&lt;1  
 *                  | 1, polyn(x)&gt;1
 * </pre>
 * 
 * with 'polyn' being a polynomial function:
 * <br><br>
 * <pre>
 * polyn(x;an,...,a0) = an·x^n +...+a1·x^1 + a0 
 * </pre>
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class PolynomialFunction1D implements PolynomialFunction<Double>{

    /**
     * The degree of the polynomial
     */
    private int polynomial_degree;
    /**
     * The set of polynomial coefficients.
     */
    private double coeficients[];
    
    /**
     * Constructs a polynominal-based membership function.
     * 
     * @param coeficients the coeficients of the polynominal function. They 
     * should be provided in the order a0, a1, ..., an.
     */
    public PolynomialFunction1D(double coeficients[]){
        this.setCoeficients(coeficients);
    }
    
    /**
     * Set new coeficients for this polynominal-based function.
     * 
     * @param coeficients the new coeficients.
     */
    @Override
    public final void setCoeficients(double coeficients[]){
        if(coeficients==null || coeficients.length==0){
            throw new InvalidParameterException("Empty coeficient set.");
        }
        polynomial_degree = coeficients.length-1;
        this.coeficients = coeficients;
    }    
    
    /**
     *  Returns the coeficients of the polynomial.
     *
     * @return the coeficients of the polynomial.
     */
    @Override
    public double[] getCoeficients() {
        return this.coeficients;
    }
    
    /**
     * Return the degree of the polynomial
     *
     * @return the degree of the polynomial
     */
    @Override
    public int getPolynomialDegree() {
        return this.polynomial_degree;
    }
    
    /**
     * Applies this membership function to the given argument.
     * 
     * @param x the function argument.
     * @return the function result.
     */
    @Override
    public Double apply(Double x) {
        Double output = polynomial(x);
        if(output<0) return 0.0;
        if(output>1) return 1.0;
        return output;       
    }
    
    /**
     * Applies the polynomial function to the given argument.
     * 
     * @param x the function argument
     * @return the function result
     */
    protected double polynomial(Double x){
        double result = 0.0;
        for(int n=0; n<=polynomial_degree; n++){
            result += coeficients[n]*Math.pow(x, n);
        }
        return result;
    }
    
    /**
     * Returns an alpha-cut associated to the membership function. It is only 
     * supported if the degree of the polynomial is one (in other case, an 
     * exception is thrown).
     *
     * @param alpha the alpha value.
     * @return the alpha-cut.
     */
    @Override
    public Interval<Double> getAlphaCut(double alpha) {
        if(this.polynomial_degree>1){
            throw new UnsupportedOperationException("Alpha-cut is not supported in "+this.getClass().getSimpleName()+"."); 
        }
        return alphaCutN1(alpha);
    }
    
    /**
     * Calculates the alpha-cut for the case n=1.
     * 
     * @param the alpha value.
     * @return the alpha-cut.
     */
    private Interval<Double> alphaCutN1(double alpha){
        double x = (alpha - coeficients[0]) / coeficients[1]; 
        boolean increasing =  coeficients[1]>0;
        return new Interval(increasing?x:Double.MIN_VALUE , increasing?Double.MAX_VALUE:x);    
    }
}
