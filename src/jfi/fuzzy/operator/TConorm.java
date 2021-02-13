package jfi.fuzzy.operator;

import java.util.function.BinaryOperator;
import jfi.fuzzy.DiscreteFuzzySet;

/**
 * Class representing a t-conorm. 
 * 
 * A t-conotm is implemented as a functional object, so we can assign a function 
 * to a <code>TNorm</code> object as, for example, in the following code:
 * 
 * <p><ul>
 * <li><code>TConorm tconorm_max = (a,b) -&gt; Math.max(a, b);</code></li>
 * <li><code>TConorm tconorm_sum = (a,b) -&gt; a+b-a*b;</code></li>
 * </ul>
 * 
 * <p>
 * The most commonly used t-conorms are included as static data members
 * (see {@link #MAX}, {@link #SUM} or {@link #BOUNDED_SUM}). For example, for
 * using the standard maximum t-conorm, the following code is recommended:
 *
 * <p>
 * <code>
 * TConorm tconorm = TCoorm.MAX;<br>
   Double out = tconorm.apply(a,b); // or TConorm.MAX.apply(a,b)
 * </code>
 * 
 * <p>
 * A list of arguments is allowed in the previous calls. For example:
 * <p><code>
   Double out = TConorm.MAX.apply(a,b,c,d);
 * </code>
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
@FunctionalInterface 
public interface TConorm extends BinaryOperator<Double>, Aggregation<Double,Double> {
    /**
     * The t-conorm "maximum". It is defined as <code> tconorm(a,b) = max(a, b)</code>.
     */
    static TConorm MAX = (a,b) -> Math.max(a, b);
    /**
     * The t-conorm "algebraic sum". It is defined as <code> tconorm(a,b) = a+b-a*b</code>.
     */
    static TConorm SUM = (a,b) -> a+b-a*b;
    /**
     * The t-conorm "bounded sum". It is defined as <code> tconorm(a,b) = min(1,a+b)</code>.
     */
    static TConorm BOUNDED_SUM = (a,b) -> Math.min(1.0,a+b);
    

    /**
     * Applies this t-conorm to the given arguments.
     *
     * @param t the first argument.
     * @param u the second argument.
     * @return the t-conorm result.
     */
    @Override
    public Double apply(Double t, Double u);

    /**
     * Applies this t-conorm to the given list of arguments.
     *
     * @param t the first argument.
     * @param u the list of arguments from the second one.
     * @return the t-conorm result.
     */
    @Override
    default public Double apply(Double t, Double... u) {
        double output = t; 
        for(double d: u){
            output = this.apply(output,d);
        }
        return output;
    }
    
    /**
     * Applies this t-conorm to the given discrete fuzzy sets.
     * 
     * @param t the first fuzzy set.
     * @param u the second fuzzy set.
     * @return a new fuzzy set result of applying the t-conorm.
     */
    default public  DiscreteFuzzySet apply(DiscreteFuzzySet t, DiscreteFuzzySet u) {        
        DiscreteFuzzySet output = new DiscreteFuzzySet();       
        for (Object e : t.getReferenceSet()) {            
            output.add(e, this.apply(t.membershipDegree(e), u.membershipDegree(e)));            
        }
        for (Object e : u.getReferenceSet()) {
            output.add(e, this.apply(t.membershipDegree(e), u.membershipDegree(e)));            
        }        
        return output;
    }
}
