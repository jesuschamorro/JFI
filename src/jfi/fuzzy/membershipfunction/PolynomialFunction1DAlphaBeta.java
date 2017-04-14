package jfi.fuzzy.membershipfunction;

import java.security.InvalidParameterException;

/**
 * Class representing an unidimensional polynomial-based membership function. It 
 * depends on two scalar parameters <tt>alpha</tt> and <tt>beta</tt>, and the 
 * set of coefficients of the polynomial function, as given by:
 * <br><br>
 * <pre>
 *                             | 0, x&lt;alpha
 * f(x;an,...,a0,alpha,beta) = | polyn(x;an,...,a0), alpha≤x≤beta 
 *                             | 1, x&gt;beta
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
public class PolynomialFunction1DAlphaBeta extends PolynomialFunction1D{
    /**
     * The parameters alpha and beta.
     */
    private double alpha, beta;
    
    /**
     * Constructs a polynominal-based membership function.
     * 
     * @param coeficients the coeficients of the polynominal.
     * @param alpha the lower boundary (alpha parameter).
     * @param beta the upper boundary (beta parameter).
     * @throws InvalidParameterException if alpha greater than or equal to beta.
     */
    public PolynomialFunction1DAlphaBeta(double coeficients[], double alpha, double beta){
        super(coeficients);
        if(alpha>=beta){
            throw new InvalidParameterException("Invalid values of alpha and beta (they must satisfy the condition alpha<beta).");
        }
        this.alpha = alpha;
        this.beta = beta;
    }
    
    /**
     * Set the lower boundary (alpha parameter) of this membership function.
     * 
     * @param alpha the lower boundary.
     */
    public void setAlpha(double alpha){
        if(alpha>=beta){
            throw new InvalidParameterException("Invalid value of alpha.");
        }
        this.alpha = alpha;
    }
    
    /**
     * Returns the lower boundary (alpha parameter) of this membership function.
     * 
     * @return the lower boundary of this membership function.
     */
    public double getAlpha(){
        return alpha;
    }
    
    /**
     * Set the upper boundary (beta parameter) of this membership function.
     * 
     * @param beta the upper boundary.
     */
    public void setBeta(double beta){
        if(beta<=alpha){
            throw new InvalidParameterException("Invalid value of beta.");
        }
        this.beta = beta;
    }
    
    /**
     * Returns the upper boundary (beta parameter) of this membership function.
     * 
     * @return the upper boundary of this membership function.
     */
    public double getBeta(){
        return beta;
    }
    
    /**
     * Applies this membership function to the given argument.
     * 
     * @param x the function argument.
     * @return the function result.
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
     * Checks if the fucntion is decreasing or not.
     * 
     * @return <tt>true</tt> if the function is decreasing
     */
    private boolean isDecreasing(){
        return polynomial(this.alpha)<polynomial(this.beta);       
    }
     
}
