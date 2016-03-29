package jfi.fuzzy.membershipfunction;

import java.security.InvalidParameterException;
import jfi.utils.Interval;

/**
 *
 * @author Jesús Chamorro
 */
public class PolynomialFunction1D implements MembershipFunction<Double>{

    private int polynomial_degree;
    double coeficients[];
    double alpha, beta;
    
    /**
     * Constructs a polynominal membership function 
     * 
     * @param coeficients the coeficients of the polynominal function
     * @param alpha lower boundary
     * @param beta upper boundary
     */
    public PolynomialFunction1D(double coeficients[], double alpha, double beta){
        if(alpha>=beta){
            throw new InvalidParameterException("Invalid values of alpha and beta (they must satisfy the condition alpha<beta).");
        }
        this.setCoeficients(coeficients);
        this.alpha = alpha;
        this.beta = beta;
    }
    
    /**
     * Set new coeficients for this polynominal function
     * 
     * @param coeficients the new coeficients
     */
    public final void setCoeficients(double coeficients[]){
        if(coeficients==null || coeficients.length==0){
            throw new InvalidParameterException("Empty coeficient set.");
        }
        polynomial_degree = coeficients.length-1;
        this.coeficients = coeficients;
    }
    
    /**
     * Applies this membership function to the given argument.
     * @param x the function argument
     * @return the function result
     */
    @Override
    public Double apply(Double x) {
        if(x<alpha) 
            return (isDecreasing()?1.0:0.0);
        if(x>beta) 
            return (isDecreasing()?0.0:1.0);
        return polynomial(x);
    }
    
    /**
     * Applies the polynomial function to the given argument.
     * @param x the function argument
     * @return the function result
     */
    private double polynomial(Double x){
        double result = 0.0;
        for(int n=0; n<=polynomial_degree; n++){
            result += coeficients[n]*Math.pow(x, n);
        }
        return result;
    }
    
    /**
     * Check if the fucntion is decreasing or not.
     * 
     * @return <tt>true</tt> if the function is decreasing
     */
    private boolean isDecreasing(){
        //TODO
        return true;       
    }
    
    @Override
    public Interval<Number> getAlphaCut(double alpha) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
