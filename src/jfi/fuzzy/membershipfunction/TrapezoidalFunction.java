package jfi.fuzzy.membershipfunction;

import java.security.InvalidParameterException;
import jfi.utils.Interval;

/**
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 * @param <Domain> unidimensional domain of the function
 */
public class TrapezoidalFunction<Domain extends Number> implements MembershipFunction<Domain> {

    /**
     *
     */
    private double a;
    /**
     *
     */
    private double b;
    /**
     *
     */
    private double c;
    /**
     *
     */
    private double d;

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param d
     */
    public TrapezoidalFunction(double a, double b, double c, double d) {
        this.setParameters(a, b, c, d);
    }

    /**
     * Applies this membership function to the given argument.
     * @param x the function argument
     * @return the function result
     */
    @Override
    public Double apply(Domain x) {
        double xd = x.doubleValue();
        //Si a==b recta con pendiente; si no, función escalón
        double f1 = b != a ? (xd - a) / (b - a) : (xd >= a ? 1.0 : 0.0);
        double f2 = d != c ? (d - xd) / (d - c) : (xd <= c ? 1.0 : 0.0);
        return (Math.max(Math.min(Math.min(f1, 1), f2), 0));

    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param d
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
     *
     * @return
     */
    public double[] getParameters() {
        double p[] = {a, b, c, d};
        return p;
    }
    
    /**
     * Return an alpha-cut associated to a trapezoidal membership function
     *
     * @param alpha the alpha
     * @return the alpha-cut
     */
    @Override
    public Interval<Number> getAlphaCut(double alpha) {
        Double interval_a = (b - a)*alpha + a;
        Double interval_b = d - (d - c)*alpha;
        return new Interval<>((Domain)interval_a, (Domain)interval_b);
    }
}
