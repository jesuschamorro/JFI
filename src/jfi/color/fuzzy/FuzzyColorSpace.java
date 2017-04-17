package jfi.color.fuzzy;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import jfi.fuzzy.FuzzySetCollection;
import jfi.geometry.Point3D;

/**
 * A fuzzy color space represented as a finite collection of fuzzy colors.
 *
 * @param <T> the domain of the fuzzy colors, that is, the type in which the
 * crisp color is represented (as in {@link FuzzyColor})
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 
 */
public class FuzzyColorSpace<T> extends FuzzySetCollection<FuzzyColor<T>,T>{
     /**
     * Constructs an empty fuzzy color space.
     */
    public FuzzyColorSpace() {
        super();
    }

    /**
     * Constructs a new fuzzy color space containing the fuzzy colors of the 
     * specified collection.
     *
     * @param fuzzyColors the collection of fuzzy colors to be placed into this 
     * fuzzy color space.
     * @throws NullPointerException if the specified collection is null.
     */
    public FuzzyColorSpace(Collection<FuzzyColor<T>> fuzzyColors) {
        super(fuzzyColors);
    }

    /**
     * Constructs a new fuzzy color space containing the fuzzy colors of the 
     * specified list of arguments.
     *
     * @param fuzzyColors the list of fuzzy colors to be placed into this fuzzy 
     * color space.
     */
    public FuzzyColorSpace(FuzzyColor<T>...fuzzyColors) {
        super();
        for(FuzzyColor fc: fuzzyColors){
            this.add(fc);
        }
    }
    
    
    /**
     * Class for generating fuzyy color spaces.
     */
    static public class Factory{
        /**
         * Kernel size relative to the nearest point.
         */
        private final static double KERNEL_RELATIVE_DISTANCE = 0.0;
        /**
         * Support size relative to the nearest point.
         */
        private final static double SUPPORT_RELATIVE_DISTANCE = 1.0;
        
        /**
         * Creates a new fuzzy color space based on spherical membership
         * functions. Given a set of color prototipes (represented by a
         * three-dimensional point), the support of each color is calculated as
         * the minimum distance between its prototipe and the rest of points in
         * the set.
         *
         * @param prototypes the set of color prototypes.
         * @return a new fuzzy color space based on spherical membership
         * functions.
         */
        static public FuzzyColorSpace<Point3D> getSphericalFCS(Point3D... prototypes){            
            LinkedHashMap<String,Point3D> map = new LinkedHashMap(prototypes.length);
            for(int i=0; i<prototypes.length; i++){                
                map.put("Color "+i,prototypes[i]);
            }
            return getSphericalFCS(map);
        }
        
        /**
         * Creates a new fuzzy color space based on spherical membership
         * functions. Given a set of color prototipes (represented by a
         * three-dimensional point), the support of each color is calculated as
         * the minimum distance between its prototipe and the rest of points in
         * the set.
         *
         * @param prototypes a map of color prototipes with its names.
         * @return a new fuzzy color space based on spherical membership
         * functions.
         */
        static public FuzzyColorSpace<Point3D> getSphericalFCS(Map<String,Point3D> prototypes){
            FuzzyColorSpace<Point3D> fcs = new FuzzyColorSpace();
            Set<Map.Entry<String,Point3D>> set = prototypes.entrySet();
            double dist, minDist;
            
            for (Map.Entry<String,Point3D> ep : set){
                minDist=Double.MAX_VALUE;
                Point3D p = ep.getValue();
                for(Entry<String,Point3D> eq: set){
                    if(ep!=eq){
                        dist = p.distance(eq.getValue());
                        if(dist<minDist) minDist=dist;                            
                    }                    
                }                
                SphericalFuzzyColor fc = new SphericalFuzzyColor(ep.getKey(), p, 
                        minDist*KERNEL_RELATIVE_DISTANCE, 
                        minDist*SUPPORT_RELATIVE_DISTANCE);
                fcs.add(fc);
            }            
            return fcs;
        }
        
    }
    
}
