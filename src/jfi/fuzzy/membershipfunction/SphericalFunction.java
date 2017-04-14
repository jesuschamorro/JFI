package jfi.fuzzy.membershipfunction;

import java.security.InvalidParameterException;
import jfi.geometry.Point3D;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class SphericalFunction implements MembershipFunction<Point3D>{
    /**
     * Center of the spherical function.
     */
    private Point3D center;
    /**
     * Parameter 'a' of the spherical function.
     */
    private double a;
    /**
     * Parameter 'b' of the spherical function.
     */
    private double b;

    /**
     * Constructs a spherical function.
     * 
     * @param center the center of the spherical function.
     * @param a the parameter 'a' of the spherical function.
     * @param b the parameter 'b' of the spherical function.
     */
    public SphericalFunction(Point3D center, double a, double b){
        this.setParameters(center, a, b);
    }
    
    /**
     * Applies this membership function to the given point.
     * 
     * @param p the function argument (point)
     * @return the function result
     */
    @Override
    public Double apply(Point3D p) {
        return apply(p.x,p.y,p.z);
    }
    
    /**
     * Applies this membership function to the given point.
     * 
     * @param x the x-coordinate of the point argument.
     * @param y the y-coordinate of the point argument.
     * @param z the y-coordinate of the point argument.
     * @return the function result.
     */
    public Double apply(double x, double y, double z) {
        double d = center.distance(x, y, z);
        //If a != b straight line with its slope; else, step function
        double f = a != b ? (b - d) / (b - a) : (d <= b ? 1.0 : 0.0);
        return Math.max(Math.min(1.0, f), 0.0);
    }

    /**
     * Set the parameters of the circular function.
     * 
     * @param center the center of the spherical function.
     * @param a the parameter 'a' of the spherical function.
     * @param b the parameter 'b' of the spherical function.
     */
    public final void setParameters(Point3D center, double a, double b) {
        if (a > b) {
            throw new InvalidParameterException("The parameter 'a' must be smaller or equal than 'b'");
        }
        this.center = center;
        this.a = a;
        this.b = b;
    }

    /**
     * Returns the center of this spherical function.
     * 
     * @return the center of this spherical function.
     */
    public Point3D getCenter(){
        return this.center;
    }
    
    /**
     * Returns the parameter 'a' of this spherical function.
     * 
     * @return the parameter 'a' of this spherical function.
     */
    public double getA() {
        return this.a;
    }
    
    /**
     * Returns the parameter 'b' of this spherical function.
     * 
     * @return the parameter 'b' of this spherical function.
     */
    public double getB() {
        return this.b;
    }
        
}
