package jfi.color.fuzzy;

import java.awt.Color;
import jfi.fuzzy.FuzzySet;
import jfi.fuzzy.GranularFuzzySet;
import jfi.fuzzy.operators.TConorm;
import jfi.geometry.Point3D;

/**
 * Fuzzy color represented as the union of "atomic" fuzzy sets.
 * 
 * There are not restrictions about the fuzzy sets, except that they have to be
 * defined over the {@link jfi.geometry.Point3D} domain. The union is performed
 * by using a t-conorm, defined as a data member of the object (by default, the
 * bounded sum is used).
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class GranularFuzzyColor extends GranularFuzzySet<Point3D> implements FuzzyColor<Point3D>{
    /**
     * The default t-norm used for the union.  
     */
    private static TConorm DEFAULT_TCONORM = TConorm.BOUNDED_SUM;
    
    /**
     * Constructs an empty granular fuzzy color with the
     * {@link jfi.fuzzy.operators.TConorm#BOUNDED_SUM} as t-conorm.
     *
     * @param label label of the fuzzy set.
     */
    public GranularFuzzyColor(String label) {
        super(label,DEFAULT_TCONORM);      
    }

    /**
     * Constructs an empty granular fuzzy color with an empty label and the
     * {@link jfi.fuzzy.operators.TConorm#BOUNDED_SUM} as t-conorm.
     */
    public GranularFuzzyColor() {
        super("", DEFAULT_TCONORM);
    }
    
    /**
     * Constructs a new granular fuzzy color containing the atomic fuzzy sets of
     * the specified collection.
     *
     * @param label label of the fuzzy set.
     * @param collection list of atomic fuzzy set to be placed into this
     * granular fuzzy color.
     */
    public GranularFuzzyColor(String label, FuzzySet<Point3D>... collection) {
        super(label,DEFAULT_TCONORM,collection);
    }
    
    /**
     * Constructs a new granular fuzzy color containing the atomic fuzzy sets of
     * the specified collection. By default, the empty label and the
     * {@link jfi.fuzzy.operators.TConorm#BOUNDED_SUM} t-conorm are used.
     *
     * @param collection list of atomic fuzzy set to be placed into this
     * granular fuzzy set
     */
    public GranularFuzzyColor(FuzzySet<Point3D>... collection) {
        super("", DEFAULT_TCONORM,collection);
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
     * Class for generating granular fuzyy colors.
     */
    static public class Factory {

        /**
         *
         * @param prototypes_p
         * @param prototypes_n
         * @param kernel_factor
         * @return
         */
        static public GranularFuzzyColor getSphericalInstance(Point3D[] prototypes_p, Point3D[] prototypes_n, double kernel_factor) {
            GranularFuzzyColor gfc = new GranularFuzzyColor("Color granulado");            
            double min, dist, a, b;

            // A kernel factor equals to 1.0 means that the kernel radius should 
            // be equal to half of the distance to the nearest point
            kernel_factor /= 2.0;
            // For each positive prototype, the kernel radius of its associated 
            // "atomic color" is calculated as the distance to the nearest point 
            // from the negative prototypes. The kernel radius is calculated by 
            // weighted that distance by the kernel factor.           
            for (Point3D ep : prototypes_p) {
                min = Double.MAX_VALUE;
                for (Point3D eq : prototypes_n) {
                    if (ep != eq) {
                        dist = ep.distance(eq);
                        if (dist < min) {
                            min = dist;
                        }
                    }
                }                
                a = min*kernel_factor;
                b = min-a;                
                // We create the spherical fuzzy color using the parameters
                SphericalFuzzyColor fc = new SphericalFuzzyColor("", ep, a, b);
                gfc.add(fc);               
            }                        
            return gfc;
        }
    }
}
