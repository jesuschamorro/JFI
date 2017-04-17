package jfi.fuzzy.membershipfunction;

import java.security.InvalidParameterException;
import jfi.utils.Interval;

/**
 * Class representing a triangular membership function. It depends on four 
 * scalar parameters <tt>a</tt>, <tt>b</tt>, <tt>c</tt> and <tt>d</tt> as given 
 * by:
 * 
 * <br><br>
 * <pre>
 *                | 0, x≤a
 *                | (x−a)/( b−a), a≤x≤b
 * f(x;a,b,c,d) = | 1, b≤x≤c
 *                | (d−x)/(d−c),  c≤x≤d
 *                | 0, d≤x
 * </pre>
 * or, more compactly, by 
 * <br><br>
 * <pre>
 * f(x;a,b,c,d) = max(min( (x−a)/(b−a), 1 ,(d−x)/(d−c) ) , 0)
 * </pre>
 * 
 * The parameters <tt>a</tt> and <tt>d</tt> locate the "feet" of the trapezoid 
 * and the parameters <tt>b</tt> and <tt>c</tt> locate the "shoulders".
 * 
 * @param <D> unidimensional domain of the function
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class TrapezoidalFunction<D extends Number> implements MembershipFunction<D> {

    /**
     * Parameter 'a' of the trapezoidal function.
     */
    private double a;
    /**
     * Parameter 'b' of the trapezoidal function.
     */
    private double b;
    /**
     * Parameter 'c' of the trapezoidal function.
     */
    private double c;
    /**
     * Parameter 'd' of the trapezoidal function.
     */
    private double d;

    /**
     * Constructs a trapezoidal function.
     * 
     * @param a the parameter 'a' of the trapezoidal function.
     * @param b the parameter 'b' of the trapezoidal function.
     * @param c the parameter 'c' of the trapezoidal function.
     * @param d the parameter 'd' of the trapezoidal function.
     */
    public TrapezoidalFunction(double a, double b, double c, double d) {
        this.setParameters(a, b, c, d);
    }

    /**
     * Applies this membership function to the given argument.
     * 
     * @param x the function argument.
     * @return the function result.
     */
    @Override
    public Double apply(D x) {
        double xd = x.doubleValue();
        //If a!=b straight line with its slope; else, step function
        double f1 = b != a ? (xd - a) / (b - a) : (xd >= a ? 1.0 : 0.0);
        double f2 = d != c ? (d - xd) / (d - c) : (xd <= c ? 1.0 : 0.0);
        return (Math.max(Math.min(Math.min(f1, 1), f2), 0));

    }

    /**
     * Set the parameters of the trapezoidal function.
     * 
     * @param a the parameter 'a' of the trapezoidal function.
     * @param b the parameter 'b' of the trapezoidal function.
     * @param c the parameter 'c' of the trapezoidal function.
     * @param d the parameter 'd' of the trapezoidal function.
     */
    public final void setParameters(double a, double b, double c, double d) {
        if (a > b || b > c || c > d) {
            throw new InvalidParameterException("The parameters must satisfy the following condition: a<=b<=c<=d");
        }
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * Returns the four parameters of this trapezoidal function.
     * 
     * @return a vector with the four parameters of this trapezoidal function.
     */
    public double[] getParameters() {
        double p[] = {a, b, c, d};
        return p;
    }
    
    /**
     * Returns an alpha-cut associated to this trapezoidal membership function.
     *
     * @param alpha the alpha value.
     * @return the alpha-cut.
     */
    @Override
    public Interval<Double> alphaCut(double alpha) {
        Double interval_a = (b - a)*alpha + a;
        Double interval_b = d - (d - c)*alpha;
        return new Interval<>(interval_a, interval_b);
    }
    
    /**
     * Returns a string representation of this function.
     *
     * @return a string representation of this function.
     */
    @Override
    public String toString(){
        return this.getClass().getSimpleName()+"("+a+","+b+","+c+","+d+")";
    }
}
