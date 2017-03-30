package jfi.fuzzy.membershipfunction;

import java.security.InvalidParameterException;
import jfi.utils.Interval;

/**
 * Class representing a triangular membership function. It depends on three 
 * scalar parameters <tt>a</tt>, <tt>b</tt>, and <tt>c</tt>, as given by:
 * 
 * <br><br>
 * <pre>
 *               | 0, x≤a
 * f(x;a,b,c) =  | (x−a)/( b−a), a≤x≤b
 *               | (c−x)/(c−b),  b≤x≤c
 *               | 0, c≤x
 * </pre>
 * or, more compactly, by 
 * <br><br>
 * <pre>
 * f(x;a,b,c) = max(min( (x−a)/(b−a), (c−x)/(c−b) ) , 0)
 * </pre>
 * 
 * The parameters <tt>a</tt> and <tt>c</tt> locate the "feet" of the triangle 
 * and the parameter <tt>b</tt> locates the peak.
 * 
 * @param <D> unidimensional domain of the function.
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class TriangularFunction<D extends Number> implements MembershipFunction<D> {

    /**
     * Parameter 'a' of the triangular function.
     */
    private double a;
    /**
     * Parameter 'b' of the triangular function.
     */
    private double b;
    /**
     * Parameter 'c' of the triangular function.
     */
    private double c;

    /**
     * Constructs a triangular function.
     * 
     * @param a the parameter 'a' of the triangular function.
     * @param b the parameter 'b' of the triangular function.
     * @param c the parameter 'c' of the triangular function.
     */
    public TriangularFunction(double a, double b, double c) {
        this.setParameters(a, b, c);
    }

    /**
     * Applies this membership function to the given argument.
     * 
     * @param x the function argument
     * @return the function result
     */
    @Override
    public Double apply(D x) {
        double xd = x.doubleValue();
        //Si a==b recta con pendiente; si no, función escalón
        double f1 = a != b ? (xd - a) / (b - a) : (xd >= a ? 1.0 : 0.0);
        double f2 = b != c ? (c - xd) / (c - b) : (xd <= c ? 1.0 : 0.0);
        return Math.max(Math.min(f1, f2), 0.0);
    }

    /**
     * Set the parameters of the triangular function.
     * 
     * @param a the parameter 'a' of the triangular function.
     * @param b the parameter 'b' of the triangular function.
     * @param c the parameter 'c' of the triangular function.
     */
    public final void setParameters(double a, double b, double c) {
        if (a > b || b > c) {
            throw new InvalidParameterException("The parameters must satisfy the following condition: a<=b<=c");
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Returns the three parameters of this triangular function.
     * 
     * @return a vector with the three parameters of this triangular function.
     */
    public double[] getParameters() {
        double[] p = {a, b, c};
        return p;
    }

    /**
     * Returns an alpha-cut associated to this triangular membership function
     *
     * @param alpha the alpha value.
     * @return the alpha-cut.
     */
    @Override
    public Interval<Double> getAlphaCut(double alpha) {
        Double interval_a = (b - a)*alpha + a;
        Double interval_b = c - (c - b)*alpha;
        return new Interval<>(interval_a, interval_b);
    }
    
    /**
     * Returns a string representation of this function.
     *
     * @return a string representation of this function.
     */
    @Override
    public String toString(){
        return this.getClass().getSimpleName()+"("+a+","+b+","+c+")";
    }
}
