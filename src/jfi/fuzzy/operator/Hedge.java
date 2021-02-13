package jfi.fuzzy.operator;

import java.security.InvalidParameterException;
import java.util.function.UnaryOperator;

/**
 * Class representing a linguistic hedge.
 * 
 * An hedge is implemented as a functional object, so we can assign a function
 * to a <code>Hedge</code> object as, for example, in the following code:
 *
 * <p>
 * <ul>
 * <li><code>Hedge h = (a) -&gt; Math.pow(a,2.0);</code></li>
 * </ul>
 *
 * <p>
 * The most commonly used hedges are included as static data members. For
 * example, for using the standard "very" hedge, the following code is
 * recommended:
 *
 * <p>
 * <code>
 * Hedge h = Hedge.VERY;<br>
 * Double out = h.apply(a); // or Hedge.VERY.apply(a)
 * </code>
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
@FunctionalInterface 
public interface Hedge extends UnaryOperator<Double>{ 
    /**
     * The linguistic hedge "very". It is defined as <code> h(a) = a^2</code>.
     */
    static Hedge VERY = (a) -> Math.pow(a,2.0);
    /**
     * The linguistic hedge "veryvery". It is defined as <code> h(a) = a^4</code>.
     */
    static Hedge VERYVERY = (a) -> Math.pow(a,4.0);
    /**
     * The linguistic hedge "plus". It is defined as <code> h(a) = a^1.25</code>.
     */
    static Hedge PLUS = (a) -> Math.pow(a,1.25);
    /**
     * The linguistic hedge "slightly". It is defined as <code> h(a) = a^0.5</code>.
     */
    static Hedge SLIGHTLY = (a) -> Math.pow(a,0.5);
     /**
     * The linguistic hedge "minus". It is defined as <code> h(a) = a^0.75</code>.
     */
    static Hedge MINUS = (a) -> Math.pow(a,0.75);
    
    /**
     * Applies this hedge to the given arguments.
     *
     * @param t the function argument.
     * @return the hedge result.
     */
    @Override
    public Double apply(Double t);
    
    /**
     * Returns a "concentration" linguistic hedge.
     * 
     * @param degree the degree
     * @param exponent exponent fot the concentration (must be greater than 1.0)
     * @return the linguistic hedges 
     */
    public static double concentration(double degree, double exponent){
        if(exponent<1.0)
            throw new InvalidParameterException("The exponent must be greater than 1.0");
        return Math.pow(degree,exponent);
    }
    
    /**
     * Returns a "dilation" linguistic hedge.
     * 
     * @param degree the degree
     * @param exponent exponent fot the dilation (must be lower than 1.0)
     * @return the linguistic hedges 
     */
    public static double dilation(double degree, double exponent){
        if(exponent>1.0)
            throw new InvalidParameterException("The exponent must be lower than 1.0");
        return Math.pow(degree,exponent);
    }
}
