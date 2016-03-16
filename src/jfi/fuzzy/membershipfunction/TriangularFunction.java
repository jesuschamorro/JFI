package jfi.fuzzy.membershipfunction;

import java.security.InvalidParameterException;

/**
 * 
 * @author Jesús Chamorro
 */
public class TriangularFunction implements MembershipFunction<Double> {

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
    public Double apply(Double x) {
        //Si a==b recta con pendiente; si no, función escalón
        double f1 = a != b ? (x - a)/(b - a) : (x>=a?1.0:0.0);
        double f2 = b != c ? (c - x)/(c - b) : (x<=c?1.0:0.0);
        return Math.max(Math.min(f1, f2), 0.0);
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     */
    public final void setParameters(double a, double b, double c) {
        if (a>b || b>c) {
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

}
