package jfi.fuzzy.membershipfunction;

import java.security.InvalidParameterException;

/**
 * 
 * @author Jesús Chamorro Martínez
 */
public class TrapezoidalFunction implements MembershipFunction<Double> {

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
     *
     * @param x
     * @return
     */
    @Override
    public Double apply(Double x) {
        //Si a==b recta con pendiente; si no, función escalón
        double f1 = b != a ? (x - a) / (b - a) : (x >= a ? 1.0 : 0.0);
        double f2 = d != c ? (d - x) / (d - c) : (x <= c ? 1.0 : 0.0);
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
}
