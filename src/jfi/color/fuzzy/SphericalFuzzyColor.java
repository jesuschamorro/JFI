package jfi.color.fuzzy;

import java.awt.Color;
import jfi.fuzzy.FunctionBasedFuzzySet;
import jfi.fuzzy.membershipfunction.SphericalFunction;
import jfi.geometry.Point3D;

/**
 * Fuzzy color based on a spherical membership function.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class SphericalFuzzyColor extends FunctionBasedFuzzySet<Point3D> implements FuzzyColor<Point3D>{
    
    /**
     * Constructs a new fuzzy color based on a spherical membership function.
     * 
     * @param label the label associated to the fuzzy color.
     * @param center the center of the spherical function.
     * @param a the parameter 'a' of the spherical function.
     * @param b the parameter 'b' of the spherical function.
     * 
     * @see jfi.fuzzy.membershipfunction.SphericalFunction
     */
    public SphericalFuzzyColor(String label, Point3D center, double a, double b) {
        super(label, new SphericalFunction(center, a, b));    
    }
    
    /**
     * Constructs a new fuzzy color based on a spherical membership function. By
     * default, an empty label is used.
     * 
     * @param center the center of the spherical function.
     * @param a the parameter 'a' of the spherical function.
     * @param b the parameter 'b' of the spherical function.
     * 
     * @see jfi.fuzzy.membershipfunction.SphericalFunction
     */
    public SphericalFuzzyColor(Point3D center, double a, double b) {
        super(new SphericalFunction(center, a, b));        
    }

    /**
     * Returns the membership degree of the given crisp color to this fuzzy
     * color.
     *
     * @param c a crisp color.
     * @return the membership degree.
     */
    @Override
    public double membershipDegree(Color c) {
        Point3D p = new Point3D(c.getRed(),c.getGreen(),c.getBlue());
        return membershipDegree(p);
    }
    
    /**
     * Returns the center of this spherical function.
     * 
     * @return the center of this spherical function.
     */
    public Point3D getCenter(){
        return ((SphericalFunction)this.getMembershipFunction()).getCenter();
    }
    
    /**
     * Returns the parameter 'a' of this spherical function.
     * 
     * @return the parameter 'a' of this spherical function.
     */
    public double getKernelRadius() {
        return ((SphericalFunction)this.getMembershipFunction()).getA();
    }
    
    /**
     * Returns the parameter 'b' of this spherical function.
     * 
     * @return the parameter 'b' of this spherical function.
     */
    public double getSupportRadius() {
        return ((SphericalFunction)this.getMembershipFunction()).getB();
    }
}
