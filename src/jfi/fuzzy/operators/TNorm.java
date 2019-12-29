package jfi.fuzzy.operators;

import java.security.InvalidParameterException;
import java.util.function.BinaryOperator;
import jfi.fuzzy.DiscreteFuzzySet;

/**
 * Class representing a t-norm. 
 * 
 * A t-notm is implemented as a functional object, so we can assign a function 
 * to a <code>TConorm</code> object as, for example, in the following code:
 * 
 * <p><ul>
 * <li><code>TNorm tnorm_min = (a,b) -&gt; Math.min(a, b);</code></li>
 * <li><code>TNorm tnorm_product = (a,b) -&gt; a*b;</code></li>
 * <li><code>TNorm tnorm_diference = (a,b) -&gt; Math.max(0.0,a+b-1.0);</code></li>
 * </ul>
 * 
 * <p>
 * The most commonly used t-norms are included as static data members
 * (see {@link #MIN}, {@link #PRODUCT} or {@link #DIFFERENCE}). For example, for
 * using the standard minimum t-norm, the following code is recommended:
 *
 * <p>
 * <code>
 * TNorm tnorm = TNorm.MIN;<br>
   Double out = tnorm.apply(a,b); // or TNorm.MIN.apply(a,b)
 * </code>
 * 
 * <p>
 * A list of arguments is allowed in the previous calls. For example:
 * <p><code>
   Double out = TNorm.MIN.apply(a,b,c,d);
 * </code>
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
@FunctionalInterface 
public interface TNorm extends BinaryOperator<Double>, Aggregation<Double,Double> {
    /**
     * The t-norm "minimum". It is defined as <code> tnorm(a,b) = min(a, b)</code>.
     */
    static TNorm MIN = (a,b) -> Math.min(a, b);
    /**
     * The t-norm "algebraic product". It is defined as <code> tnorm(a,b) = a * b</code>.
     */
    static TNorm PRODUCT = (a,b) -> a*b;
    /**
     * The t-norm "bounded difference". It is defined as <code> tnorm(a,b) = max(0,a*b-1)</code>.
     */
    static TNorm DIFFERENCE = (a,b) -> Math.max(0.0,a+b-1.0);
    
    /**
     * Applies this t-norm to the given arguments.
     *
     * @param t the first argument.
     * @param u the second argument.
     * @return the t-norm result.
     */
    @Override
    public Double apply(Double t, Double u);

    /**
     * Applies this t-norm to the given list of arguments.
     *
     * @param t the first argument.
     * @param u the list of arguments from the second one.
     * @return the t-norm result.
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
     * Applies this t-norm to the given discrete fuzzy sets.
     * 
     * @param t the first fuzzy set.
     * @param u the second fuzzy set.
     * @return a new fuzzy set result of applying the t-norm.
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
    
    /**
     * Inner class implementing the Weber t-norm.
     */
    public class Weber implements TNorm {

        /**
         * Lambda paremeter of the Weber t-norm.
         */
        private double lambda = 0.0;

        /**
         * Constructs a Weber t-norm.
         *
         * @param lambda the lambda parameter.
         */
        public Weber(double lambda) {
            this.setLambda(lambda);
        }

        /**
         * Applies this t-norm to the given arguments.
         *
         * @param t the first argument.
         * @param u the second argument.
         * @return the t-norm result.
         */
        @Override
        public Double apply(Double t, Double u) {
            return Math.max(0, (t + u + (t * u * lambda) - 1.0) / 1.0 + lambda);
        }

        /**
         * Returns the lambda parameter of this Weber t-norm.
         *
         * @return the lambda parameter.
         */
        public double getLambda() {
            return lambda;
        }

        /**
         * Set the lambda parameter of this Weber t-norm.
         *
         * @param lambda the new lambda parameter.
         */
        public final void setLambda(double lambda) {
            if (lambda <= -1.0) {
                throw new InvalidParameterException("The lambda parameter must be greater than -1.0");
            }
            this.lambda = lambda;
        }
    } //End Weber class
    
    
    /**
     * Inner class implementing the Dubois-Prade t-norm.
     */
    public class DuboisPrade implements TNorm {
        /**
         * Alpha paremeter of the Dubois-Prade t-norm.
         */
        private double alpha = 0.0;

        /**
         * Constructs a Dubois-Prade t-norm.
         *
         * @param alpha the alpha parameter.
         */
        public DuboisPrade(double alpha) {
            this.setAlpha(alpha);
        }

        /**
         * Applies this t-norm to the given arguments.
         *
         * @param t the first argument.
         * @param u the second argument.
         * @return the t-norm result.
         */
        @Override
        public Double apply(Double t, Double u) {
            return t*u / Math.max(Math.max(t,u),alpha);
        }

        /**
         * Returns the alpha parameter of this Dubois-Prade t-norm.
         *
         * @return the alpha parameter.
         */
        public double getAlpha() {
            return alpha;
        }

        /**
         * Set the alpha parameter of this Dubois-Prade t-norm.
         *
         * @param alpha the new alpha parameter.
         */
        public final void setAlpha(double alpha) {
            if (alpha < 0.0 || alpha > 1.0) {
                throw new InvalidParameterException("The alpha parameter must be between 0 and 1");
            }
            this.alpha = alpha;
        }
    } //End Weber class
    
}
