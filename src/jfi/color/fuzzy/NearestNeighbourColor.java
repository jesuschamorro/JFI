package jfi.color.fuzzy;

import java.awt.Color;
import jfi.geometry.Point3D;
import jfi.utils.Prototyped;

/**
 * Crisp approach for labeling a color based on the nearest neighbour algorithm. 
 * Although it implements the {@link jfi.color.fuzzy.FuzzyColor} interface for 
 * compatibility reasons, the membership degree of any color will be 1 or 0.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class NearestNeighbourColor implements FuzzyColor<Point3D>, Prototyped<Point3D>{
    private String label;
    private final Point3D color_prototype;
    private final Point3D[] all_prototypes;
     
    /**
     * Constructs a new fuzzy color based on the nearest neighbour algorithm.
     *
     * @param label the label associated to the fuzzy color.
     * @param color_prototype the prototype of the cluster associated to the
     * fuzzy color.
     * @param all_prototypes the set of all the prototypes of the fuzzy
     * partition to which this fuzzy color belongs
     */
    public NearestNeighbourColor(String label, Point3D color_prototype, Point3D[] all_prototypes) {
        this.label = label;
        this.color_prototype = color_prototype;
        this.all_prototypes = all_prototypes;        
    }

    
    /**
     * Returns the membership degree of the given crisp color to this fuzzy
     * color. It always will be 1.0 or 0.0.
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
     * color. It always will be 1.0 or 0.0.
     *
     * @param p a point representing a crisp color.
     * @return the membership degree.
     */
    @Override
    public double membershipDegree(Point3D p) {
        double dist_ij, dist_ik;
        
        dist_ij = p.distance(color_prototype);
        if (dist_ij > 0.0) {
            for (int k = 0; k < all_prototypes.length; k++) {
                if (all_prototypes[k] != color_prototype) {
                    dist_ik = p.distance(all_prototypes[k]);
                    if (dist_ik < dist_ij) {
                        return 0.0;
                    }
                }
            }
        }
        return 1.0;
    }
    
    /**
     * Returns the prototype associated to this color.
     * 
     * @return the prototype associated to this fuzzy color
     */
    @Override
    public Point3D getPrototype() {
        return this.color_prototype;
    }
}
