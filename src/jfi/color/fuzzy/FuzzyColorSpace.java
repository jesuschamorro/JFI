package jfi.color.fuzzy;

import java.security.InvalidParameterException;
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
         * Default kernel factor used for calculating the kernel radius in an
         * sphere-based fuzzy color space.
         */
        public final static double DEFAULT_KERNEL_FACTOR = 0.0;
                
        /**
         * Creates a new fuzzy color space based on spherical membership
         * functions. Given a set of color prototypes (represented by a
         * three-dimensional point), the kernel and support of each color are
         * calculated on the basis of the minimum distance between the color
         * prototype and the rest of points in the set.
         *
         * Depending on the kernel factor (a value between 0 and 1), the kernel
         * will be smaller (factor 0) or larger (factor 1). The support will be
         * calculated taking into account that it should not intersect with any
         * other kernel. For example, if the kernel factor is 0.0 (meaning a
         * kernel with a single point located in the center of the sphere), the
         * suppot radius will be equal to the minimum distance between its
         * prototype and the rest of points in the set.
         *
         * @param prototypes a map of color prototypes with its names.
         * @param kernel_factor a value in the interval [0..1] related to the
         * size of the kernel. A kernel factor equals to 1.0 means that the
         * kernel radius should be equal to half of the distance to its nearest
         * point; a kernel factor equals to 0.0 will produce a kernel with a
         * single point located in the center of the sphere.
         *
         * @return a new fuzzy color space based on spherical membership
         * functions.
         */
        static public FuzzyColorSpace<Point3D> createSphereBasedFCS(Map<String,Point3D> prototypes, double kernel_factor){
            FuzzyColorSpace<Point3D> fcs = new FuzzyColorSpace();
            Set<Map.Entry<String,Point3D>> set = prototypes.entrySet();           
            double kernel_radius[] = new double[set.size()];
            double min, dist, a, b;
            int index_ep, index_eq;
            Point3D p;
            
            // The kernel factor must be between 0 and 1. A factor equals to 1.0 
            // means that the kernel radius should be equal to half of the 
            // distance to the nearest point; so, this radius is obtained by 
            // multiplying 'min' by the factor divided by two
            if (kernel_factor < 0.0 || kernel_factor > 1.0) {
                throw new InvalidParameterException("The kernel factor must be between 0 and 1");
            }
            kernel_factor /= 2.0;            
            // Firstly, for each prototype, the kernel radius is calculated on
            // the basis of the distance to the nearest point. At most, it will
            // be equal to half of the distance from the center to its nearest 
            // point. This value is weighted by the kernel factor           
            index_ep = 0;
            for (Map.Entry<String,Point3D> ep : set){
                min=Double.MAX_VALUE;
                p = ep.getValue();              
                for(Entry<String,Point3D> eq: set){
                    if(ep!=eq){
                        dist = p.distance(eq.getValue());
                        if(dist<min) min = dist;                        
                    }                   
                }               
                kernel_radius[index_ep++] = min*kernel_factor;
            }   
            // Secondly, for each prototype, the support radius is calculated on
            // the basis of the distance to the other prototype kernels. 
            // Specifically, we set the support radius as the distance to the 
            // nearest kernel (let us note that the nearest kernel does not have 
            // to correspond with the kernel of the closest prototype). Finally,
            // spherical fuzzy colors are calculated and added to the output 
            // fuzzy color space 
            index_ep = 0;
            for (Map.Entry<String,Point3D> ep : set){
                p = ep.getValue();           // p: The center point                              
                a = kernel_radius[index_ep]; // a: The kernel radius
                b = Double.MAX_VALUE;        // b: The support radius
                index_eq = 0;
                for(Entry<String,Point3D> eq: set){
                    if(ep!=eq){
                        dist = p.distance(eq.getValue())-kernel_radius[index_eq];
                        if(dist<b) b=dist;                        
                    }
                    index_eq++;
                }
                // We create the spherical fuzzy color using the parameters
                SphericalFuzzyColor fc = new SphericalFuzzyColor(ep.getKey(),p,a,b);
                fcs.add(fc);               
                index_ep++;
            }
            return fcs;           
        }     
        
        /**
         * Creates a new fuzzy color space based on spherical membership
         * functions. Given a set of color prototypes (represented by a
         * three-dimensional point), the kernel and support of each color are
         * calculated on the basis of the minimum distance between the color
         * prototype and the rest of points in the set. For calculating the
         * kernel size, the default {@link #DEFAULT_KERNEL_RADIUS} factor is 
         * used.
         *
         * @param prototypes a map of color prototypes with its names.
         * @return a new fuzzy color space based on spherical membership
         * functions.
         */
        static public FuzzyColorSpace<Point3D> createSphereBasedFCS(Map<String,Point3D> prototypes){
            return createSphereBasedFCS(prototypes,DEFAULT_KERNEL_FACTOR);           
        }
               
        /**
         * Creates a new fuzzy color space based on spherical membership
         * functions. Given a set of color prototypes (represented by a
         * three-dimensional point), the kernel and support of each color are
         * calculated on the basis of the minimum distance between the color
         * prototype and the rest of points in the set. For calculating the
         * kernel size, the default {@link #DEFAULT_KERNEL_RADIUS} factor is 
         * used.
         *
         * @param prototypes the set of color prototypes.
         * @return a new fuzzy color space based on spherical membership
         * functions.
         */
        static public FuzzyColorSpace<Point3D> createSphereBasedFCS(Point3D... prototypes){            
            LinkedHashMap<String,Point3D> map = new LinkedHashMap(prototypes.length);
            for(int i=0; i<prototypes.length; i++){                
                map.put("Color "+i,prototypes[i]);
            }
            return createSphereBasedFCS(map);
        }
        
        /**
         * Creates a new fuzzy color space based on the fuzzy c-means membership
         * functions. Given a set of color prototypes (represented by a
         * three-dimensional point), a fuzzy partition on the sense of FCM will
         * be built over a 3D color space. Each cluster will define a fuzzy
         * color.
         *
         * @param prototypes a map of color prototypes with its names.
         * @param m the parameter <tt>m</tt> of the Fuzzy C-Means membership
         * function.
         * @return a new fuzzy color space based on the fuzzy c-means membership
         * function.
         */
        static public FuzzyColorSpace<Point3D> createFuzzyCMeansFCS(Map<String,Point3D> prototypes, double m){
            FuzzyColorSpace<Point3D> fcs = new FuzzyColorSpace();
            Set<Map.Entry<String, Point3D>> set = prototypes.entrySet();
            Point3D[] prototypes_vector = prototypes.values().toArray(new Point3D[0]);
            FuzzyCMeamsColor fcmc;
            for (Map.Entry<String, Point3D> me : set) {
                fcmc = new FuzzyCMeamsColor(me.getKey(), me.getValue(), prototypes_vector, m);                
                fcs.add(fcmc);
            } 
            return fcs;
        }
        
        /**
         * Creates a new fuzzy color space based on the fuzzy c-means membership
         * functions. Given a set of color prototypes (represented by a
         * three-dimensional point), a fuzzy partition on the sense of FCM will
         * be built over a 3D color space. Each cluster will define a fuzzy
         * color. The default fuzzy c-means m-value
         * {@link jfi.color.fuzzy.FuzzyCMeamsColor#DEFAULT_M} is used.
         *
         * @param prototypes a map of color prototypes with its names.
         * @return a new fuzzy color space based on the fuzzy c-means membership
         * function.
         */
        static public FuzzyColorSpace<Point3D> createFuzzyCMeansFCS(Map<String,Point3D> prototypes){
            return createFuzzyCMeansFCS(prototypes,FuzzyCMeamsColor.DEFAULT_M);
        }
        
        /**
         * Creates a new fuzzy color space based on the fuzzy c-means membership
         * functions. Given a set of color prototypes (represented by a
         * three-dimensional point), a fuzzy partition on the sense of FCM will
         * be built over a 3D color space. Each cluster will define a fuzzy
         * color. The default fuzzy c-means m-value
         * {@link jfi.color.fuzzy.FuzzyCMeamsColor#DEFAULT_M} is used.
         *
         * @param prototypes the set of color prototypes with its names.
         * @return a new fuzzy color space based on the fuzzy c-means membership
         * function.
         */
        static public FuzzyColorSpace<Point3D> createFuzzyCMeansFCS(Point3D... prototypes){
            LinkedHashMap<String,Point3D> map = new LinkedHashMap(prototypes.length);
            for(int i=0; i<prototypes.length; i++){                
                map.put("Color "+i,prototypes[i]);
            }
            return createFuzzyCMeansFCS(map);
        }
    }
    
}
