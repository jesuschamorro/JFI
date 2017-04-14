package jfi.fuzzy.membershipfunction;

import java.awt.geom.Point2D;
import java.security.InvalidParameterException;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class CircularFunction implements MembershipFunction<Point2D>{
    /**
     * Center of the circular function.
     */
    private Point2D center;
    /**
     * Parameter 'a' of the circular function.
     */
    private double a;
    /**
     * Parameter 'b' of the circular function.
     */
    private double b;

    /**
     * Constructs a circular function.
     * 
     * @param center the center of the circular function.
     * @param a the parameter 'a' of the circular function.
     * @param b the parameter 'b' of the circular function.
     */
    public CircularFunction(Point2D center, double a, double b){
        this.setParameters(center, a, b);
    }
    
    /**
     * Applies this membership function to the given point.
     * 
     * @param p the function argument (point)
     * @return the function result
     */
    @Override
    public Double apply(Point2D p) {
        return apply(p.getX(),p.getY());
    }
    
    /**
     * Applies this membership function to the given point.
     * 
     * @param x the x-coordinate of the point argument.
     * @param y the y-coordinate of the point argument.
     * @return the function result.
     */
    public Double apply(double x, double y) {
        double d = center.distance(x, y);
        //If a != b straight line with its slope; else, step function
        double f = a != b ? (b - d) / (b - a) : (d <= b ? 1.0 : 0.0);
        return Math.max(Math.min(1.0, f), 0.0);
    }

    /**
     * Set the parameters of the circular function.
     * 
     * @param center the center of the circular function.
     * @param a the parameter 'a' of the circular function.
     * @param b the parameter 'b' of the circular function.
     */
    public final void setParameters(Point2D center, double a, double b) {
        if (a > b) {
            throw new InvalidParameterException("The parameter 'a' must be smaller or equal than 'b'");
        }
        this.center = center;
        this.a = a;
        this.b = b;
    }

    /**
     * Returns the center of this circular function.
     * 
     * @return the center of this circular function.
     */
    public Point2D getCenter(){
        return this.center;
    }
    
    /**
     * Returns the parameter 'a' of this circular function.
     * 
     * @return the parameter 'a' of this circular function.
     */
    public double getA() {
        return this.a;
    }
    
    /**
     * Returns the parameter 'b' of this circular function.
     * 
     * @return the parameter 'b' of this circular function.
     */
    public double getB() {
        return this.b;
    }
        
}
