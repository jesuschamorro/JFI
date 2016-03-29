package jfi.fuzzy.membershipfunction;

import java.security.InvalidParameterException;
import jfi.utils.Interval;

/**
 *
 * @author Jesús Chamorro
 * @param <Domain> unidimensional domain of the function
 */
public class TriangularFunction<Domain extends Number> implements MembershipFunction<Domain> {

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
     * @param a
     * @param b
     * @param c
     */
    public TriangularFunction(double a, double b, double c) {
        this.setParameters(a, b, c);
    }

    /**
     *
     * @param x
     * @return
     */
    @Override
    public Double apply(Domain x) {
        double xd = x.doubleValue();
        //Si a==b recta con pendiente; si no, función escalón
        double f1 = a != b ? (xd - a) / (b - a) : (xd >= a ? 1.0 : 0.0);
        double f2 = b != c ? (c - xd) / (c - b) : (xd <= c ? 1.0 : 0.0);
        return Math.max(Math.min(f1, f2), 0.0);
    }

    /**
     *
     * @param a
     * @param b
     * @param c
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
     *
     * @return
     */
    public double[] getParameters() {
        double[] p = {a, b, c};
        return p;
    }

    /**
     * Return an alpha-cut associated to a triangular membership function
     *
     * @param alpha the alpha
     * @return the alpha-cut
     */
    @Override
    public Interval<Number> getAlphaCut(double alpha) {
        Double interval_a = (b - a)*alpha + a;
        Double interval_b = c - (c - b)*alpha;
        return new Interval<>((Domain)interval_a, (Domain)interval_b);
    }
}
