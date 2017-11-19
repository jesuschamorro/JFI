package jfi.color.fuzzy;

import java.awt.Color;
import jfi.geometry.Point3D;
import jfi.utils.Prototyped;

/**
 * Fuzzy color based on the Fuzzy C-Means membership function.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzyCMeamsColor implements FuzzyColor<Point3D>, Prototyped<Point3D>{
    String label;
    Point3D color_prototype;
    Point3D[] all_prototypes;
    private double m_fcmParameter;
    public static final double DEFAULT_M = 2.0;
    
    /**
     * Constructs a new fuzzy color based on the Fuzzy C-Means membership
     * function.
     *
     * @param label the label associated to the fuzzy color.
     * @param color_prototype the prototype of the cluster associated to the
     * fuzzy color.
     * @param all_prototypes the set of all the prototypes of the fuzzy
     * partition to which this fuzzy color belongs
     * @param m the parameter <tt>m</tt> of the Fuzzy C-Means membership function.
     */
    public FuzzyCMeamsColor(String label, Point3D color_prototype, Point3D[] all_prototypes, double m) {
        this.label = label;
        this.color_prototype = color_prototype;
        this.all_prototypes = all_prototypes;
        this.m_fcmParameter = m;
    }

    /**
     * Constructs a new fuzzy color based on the Fuzzy C-Means membership function.
     * 
     * @param label the label associated to the fuzzy color.
     * @param color_prototype the prototype of the cluster associated to the
     * fuzzy color.
     * @param all_prototypes the set of all the prototypes of the fuzzy
     * partition to which this fuzzy color belongs
     */
    public FuzzyCMeamsColor(String label, Point3D color_prototype, Point3D[] all_prototypes) {
        this(label,color_prototype,all_prototypes,DEFAULT_M);
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
     * Returns the label associated to the fuzzy color.
     *
     * @return the label associated to the fuzzy color.
     */
    @Override
    public String getLabel() {
        return this.label;
    }

    /**
     * Set the label associated to the fuzzy color.
     *
     * @param label the new label.
     */
    @Override
    public void setLabel(String label) {
        this.label = label;    
    }

    /**
     * Returns the membership degree of the given crisp color to this fuzzy
     * color.
     *
     * @param p a point representing a crisp color.
     * @return the membership degree.
     */
    @Override
    public double membershipDegree(Point3D p) {
        double dist_ij, dist_ik, sum = 0.0, output = 1.0;
        
        dist_ij = p.distance(color_prototype);
        if (dist_ij > 0.0) {
            for (int k = 0; k < all_prototypes.length; k++) {
                dist_ik = p.distance(all_prototypes[k]);
                sum += Math.pow(dist_ij / dist_ik, 2.0 / (m_fcmParameter - 1.0));
            }
            output = 1.0 / sum;
        }
        return output;
    }
    
    /**
     * Returns the prototype associated to this color which corresponds to the 
     * center of this sphere-based fuzzy color.
     * 
     * @return the prototype associated to this fuzzy color
     */
    @Override
    public Point3D getPrototype() {
        return this.color_prototype;
    }
}
