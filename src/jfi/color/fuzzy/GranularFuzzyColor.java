package jfi.color.fuzzy;

import java.awt.Color;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import jfi.fuzzy.FuzzySet;
import jfi.fuzzy.GranularFuzzySet;
import jfi.fuzzy.membershipfunction.SphericalFunction;
import jfi.fuzzy.operators.TConorm;
import jfi.geometry.Point3D;
import jfi.utils.Pair;
import jfi.utils.Prototyped;

/**
 * Fuzzy color represented as the union of other (single) fuzzy colors.
 * 
 * There are not restrictions about the single fuzzy colors, except that they
 * have to be defined over the {@link jfi.geometry.Point3D} domain. The union is
 * performed by using a t-conorm, defined as a data member of the object (by
 * default, the bounded sum is used).
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class GranularFuzzyColor extends GranularFuzzySet<Point3D> implements FuzzyColor<Point3D>, Prototyped<Point3D>{
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
     * Returns <code>null</code> as the prototype associated to this color.
     * 
     * @return the prototype associated to this fuzzy color
     */
    @Override
    public Point3D getPrototype() {
        return null;
    }
    
    /**
     * Returns all the prototypes associated to this granular color (if
     * available).
     *
     * @return a list with all the prototypes associated to this color. If no
     * prototypes are available, and empty list will be returned; if not all the
     * single colors are prototype-based, only the prototypes corresponding to
     * the prototype-based ones will be returned.
     */
    @Override
    public List<Point3D> getAllPrototypes(){        
        ArrayList<Point3D> output = new ArrayList();
        Point3D prototype;
        for (FuzzySet fuzzySet : this) {
            if(fuzzySet instanceof Prototyped){
                prototype = ((Prototyped<Point3D>)fuzzySet).getPrototype();
                output.add(prototype);
            }
        }       
        return output; 
    }
    
    /**
     * Class for generating granular fuzyy colors.
     */
    static public class Factory {    
        /**
         * Creates a new granular fuzzy color space based on spherical
         * membership functions. The idea underlying the procedure is to expand
         * the sphere associated to the support of each single color until it
         * reaches some negative prototype. In this way, we can see the negative
         * prototypes as the "limiters" of the growth of the support of the
         * granular color (given by the union of the individual supports).
         *
         * Therefore, the parameters (i.e, the radius of the kernel and the
         * support) of each single fuzzy color are estimated on the basis of the
         * distance of its positive prototype to the nearest point in the set of
         * negative prototypes as follow: If we note as 'D' that distance, then
         * the radius of the kernel (resp. the support) is calculated as
         * D*kernel_factor (resp. D*support_factor)
         *
         * @param label the label of the granular fuzzy color.
         * @param prototypes_p array of positive color prototypes.
         * @param prototypes_n array of negative color prototypes.
         * @param kernel_factor scale factor for the kernel radius. It must be a
         * values in the interval [0,1) smaller than support_factor
         * @param support_factor scale factor for the support radius. It must be
         * a value in the interval (0,1] greater than kernel_factor
         * @return a new granular fuzzy color based on spherical membership
         * functions.
         */
        static public GranularFuzzyColor getSphericalInstance(String label, 
                Point3D[] prototypes_p, Point3D[] prototypes_n, 
                double kernel_factor, double support_factor) {
            
            GranularFuzzyColor gfc = new GranularFuzzyColor(label);
            double min, dist, a, b; 
            
            // Both the kernel factor (alpha) and the support factor (beta) must 
            // be between 0 and 1 and satisfying alpha<beta 
            if (kernel_factor  < 0.0  || kernel_factor  >= 1.0 || 
                support_factor <= 0.0 || support_factor > 1.0 ||
                kernel_factor >= support_factor )  {
                throw new InvalidParameterException("The kernel factor (alpha) "
                        + "and the support factor (beta) must be between 0 and 1 "
                        + "and satisfying alpha<beta ");
            }
            // For each positive prototype, the nearest point in the set of 
            // negative prototypes is calculated. Then, both the kernel and 
            // support radius are calculated by weighted that distance by the 
            // corrresponding factor.           
            for (Point3D ep : prototypes_p) {
                min = Double.MAX_VALUE;
                for (Point3D eq : prototypes_n) {
                    dist = ep.distance(eq);
                    if (dist < min && dist!=0) {// dist==0 means that a negative 
                        min = dist;             // prototype is also a positive one; 
                    }                           // It should not happen, but we check
                }
                a = min * kernel_factor;
                b = min * support_factor;
                // We create the spherical fuzzy color using the parameters
                SphericalFuzzyColor fc = new SphericalFuzzyColor("", ep, a, b);
                gfc.add(fc);
            }
            return gfc;
        }
        
        /**
         * Creates a new granular fuzzy color space based on spherical
         * membership functions. In this approach, the sphere associated to the
         * support of each single color is expanded until it reaches some
         * negative prototype. After that, the kernel of each single color is
         * expanded to the nearest support in the granular color
         * intersecting with the support of the analyzed single color
         * 
         * @param label the label of the granular fuzzy color.
         * @param prototypes_p array of positive color prototypes.
         * @param prototypes_n array of negative color prototypes.
         * @return a new granular fuzzy color based on spherical membership
         * functions.
         */
        static public GranularFuzzyColor getSphericalInstance(String label, 
                Point3D[] prototypes_p, Point3D[] prototypes_n) {
            
            GranularFuzzyColor gfc = getSphericalInstance(label, prototypes_p, 
                    prototypes_n, 0.0, 1.0);
            kernelRadiusEstimation(gfc, true);
            return gfc;
        }
        
        /**
         * Estimates the radius of each single color by expanding it to the
         * nearest (or furthest, depending on the 'nearest' parameter) support
         * in the granular color intersecting with the support of the color 
         * being analyzed.
         *
         * @param gfc the granular fuzzy color which kernels are to be estimated
         */
        static private void kernelRadiusEstimation(GranularFuzzyColor gfc, boolean nearest) {
            SphericalFuzzyColor sfc1, sfc2;
            Point3D p1, p2;
            double a1, b1, b2, dp1support2, dp1p2, min;

            if (gfc.size() > 1) {
                // For each single color in the granular color, the other ones 
                // will be analized focusing the study on the cases where the 
                // support spheres intersect; from all of them, we will get the
                // nearest (or furthest, depending on the 'nearest' parameter) 
                // one (if exists) and we will set the kernel radius as the
                // distance to the nearest (or farthest) support.
                for (FuzzySet<Point3D> fc1 : gfc) {
                    sfc1 = (SphericalFuzzyColor) fc1;
                    p1 = sfc1.getCenter();
                    a1 = 0.0; //Default value
                    b1 = sfc1.getSupportRadius();
                    min = Double.MAX_VALUE;                                      
                    for (FuzzySet<Point3D> fc2 : gfc) {
                        if (fc1 != fc2) {
                            sfc2 = (SphericalFuzzyColor) fc2;
                            p2 = sfc2.getCenter();
                            b2 = sfc2.getSupportRadius();
                            dp1p2 = p1.distance(p2);
                            if (b1 + b2 > dp1p2) {        //Intersect?
                                dp1support2 = dp1p2 - b2; //It could be negative
                                if (nearest) {
                                    dp1support2 = Math.max(dp1support2, 0);
                                    if (dp1support2 < min) {   //The nearest    
                                        min = dp1support2;     //support
                                    }
                                } else if (dp1support2 > a1) { //The furthest  
                                    a1 = dp1support2;          //support   
                                }
                            }
                        }
                    }
                    if(nearest && min<Double.MAX_VALUE) a1 = min;
                    ((SphericalFunction) sfc1.getMembershipFunction()).setA(a1);
                }
            }
        }
        
        static public void enhanceConnectivity(GranularFuzzyColor gfc){
            SphericalFuzzyColor sfc_i, sfc_j, sfc_rho;
            Point3D pi, pj, u_ij, rho_ij;
            double ai, aj, bi, bj,arho,brho; 
            double dist_ij, dist_irho, t_ij;
            int max_iterations, iteration = 0;
            
            GranularFuzzyColor.Graph graph = gfc.new Graph();
            max_iterations = graph.num_components -1;
            
            //Salida para pruebas (provisional)
            System.out.println(gfc.label+" (#"+gfc.size()+"): #C="+graph.num_components);
            //Fin salida
            
            while(graph.num_components>1 && iteration<max_iterations){
                //First, the neartest single fuzzy colors belonging to different 
                //components are obtained.
                Pair<Integer, Integer> nearest = graph.nearestUnconnectedVertices();
                sfc_i = (SphericalFuzzyColor)gfc.get(nearest.getLeft());             
                sfc_j = (SphericalFuzzyColor)gfc.get(nearest.getRight());                                
                pi = sfc_i.getCenter();
                ai = sfc_i.getKernelRadius();
                bi = sfc_i.getSupportRadius();
                pj = sfc_j.getCenter();
                aj = sfc_j.getKernelRadius();
                bj = sfc_j.getSupportRadius();
                //Second, the new prototype rho_ij is calculated
                dist_ij = pi.distance(pj);               
                dist_irho = bi + ( dist_ij-(bi+bj) )/2.0;
                t_ij = dist_irho/dist_ij;
                u_ij = pj.subtract(pi);
                u_ij.scale(t_ij);
                rho_ij = pi.add(u_ij);
                //Third, the parameter arho y brho are calculated
                arho = Math.max(dist_irho-bi, 0.0);
                brho = Math.max(bi-ai, bj-aj)+(dist_irho-bi);
                //The granular color and its graph are updated
                sfc_rho = new SphericalFuzzyColor("", rho_ij, arho, brho);
                gfc.add(sfc_rho);                
                graph.updateGraph();
                iteration++;
                
                //Salida para pruebas (provisional)
                System.out.println("Iteración "+iteration+" ("+gfc.size()+",#C:"+graph.num_components+"): "+nearest+" rho:"+rho_ij);
                //Fin salida
                
            }
            
            //Salida para pruebas (provisional)
            System.out.println("#CFinal="+graph.num_components);
            //Fin salida
        }
        
    } // End of Factory class

    /**
     * Inner class representing the graph associated to this granular fuzzy
     * color.
     *
     * This implementation is developed only for granular fuzzy colors compound 
     * by spherical fuzzy colors and using the bounded sum as t-conorm.
     *
     */
    public class Graph{
        /**
         * Adjacency matrix of this graph.
         */
        private  Double adjacencyMatrix[][];
        /**
         * Vector for storing the connected component index associated to each 
         * vertex
         */
        private  Integer component[];  
        /**
         * Epsilon constant
         */
        private double EPSILON = 0.00001;
        /**
         * Number of vertices of this graph.
         */
        private int num_vertices = 0;
        /**
         * Number of edges of this graph.
         */
        private int num_edges = 0;
        /**
         * Number of connected components of this graph.
         */
        private int num_components = 0;
        
        /**
         * Construcs a graph associated to the granular color.
         */
        public Graph() {              
           this.updateGraph();
        }
        
        /**
         * Updates the adjacency matrix and the components vector.
         */
        public final void updateGraph() {
            num_vertices = size();
            if (num_vertices > 0) {
                if (!(get(0) instanceof SphericalFuzzyColor)) {
                    throw new InvalidParameterException("Spherical fuzzy color "
                            + "are needed for graph building");
                }
                if (getTConorm() != TConorm.BOUNDED_SUM) {
                    throw new InvalidParameterException("Bounded sum is needed "
                            + "as t-conorm for graph building");
                }
                this.setAdjacency();
                this.setComponents();
            }
        }
        
        /**
         * Set the adjacency matrix of this graph.
         */
        private void setAdjacency(){            
            SphericalFuzzyColor sfc_i, sfc_j;
            double dist_ij;
            
            adjacencyMatrix = new Double[num_vertices][num_vertices];
            for (int i = 0; i < num_vertices; i++) {
                sfc_i = (SphericalFuzzyColor) get(i);
                adjacencyMatrix[i][i] = 0.0;
                for (int j = i + 1; j < num_vertices; j++) {
                    sfc_j = (SphericalFuzzyColor) get(j);
                    dist_ij = sfc_i.getCenter().distance(sfc_j.getCenter());
                    // In the sphere-based case, and assuming that we use the 
                    // bounded sum of as t-conorm, it is easy to show that the 
                    // union of e Ci and e Cj has a connected kernel iff the 
                    // kernel of e Ci reaches the support of e Cj , and viceversa.
                    // This occurs if and only if the sphere associated to the 
                    // kernel of e Ci (resp. e Cj) intersects with the sphere 
                    // associated to the support of e Cj (resp. e Ci), that is,
                    // when the sum of their radii is greater than or equal to 
                    // the distance between their centers.
                    if (sfc_i.getKernelRadius() + sfc_j.getSupportRadius() + EPSILON >= dist_ij && 
                        sfc_j.getKernelRadius() + sfc_i.getSupportRadius() + EPSILON >= dist_ij) {
                        adjacencyMatrix[i][j] = adjacencyMatrix[j][i] = dist_ij;
                        num_edges++;
                    }
                }
            }
        }

        /**
         * Calculates the connected components of this graph.
         */
        private void setComponents(){
            num_components = 0;
            component = new Integer[num_vertices];
            for (int i = 0; i < num_vertices; i++) {
                if (component[i] == null) {
                    setComponentValue(i, num_components++);
                }
            }
        }
        
        /**
         * Set the given component value to the i-th vertex and to all the
         * vertices connected to it.
         * 
         * @param i the vertex index.
         * @param value the component index
         */
        private void setComponentValue(int i, int value) {
            component[i] = value;
            for (int j = 0; j < size(); j++) {
                if (i != j && component[j] == null
                        && adjacencyMatrix[i][j] != null) {
                    setComponentValue(j, value); // Recursive call    
                }
            }
        }
        
        /**
         * Returns the number of vertices of this graph.
         * 
         * @return the number of vertices of this graph.
         */
        public int numVertices(){
            return this.num_vertices;
        }
        
        /**
         * Returns the number of edges of this graph.
         * 
         * @return the number of edges of this graph.
         */
        public int numEdges(){
            return this.num_edges;
        }
        
        /**
         * Returns the number of connected components of this graph.
         * 
         * @return the number of connected components of this graph.
         */
        public int numConnectedComponents(){
            return this.num_components;
        }
        
        /**
         * Returns <code>true</code> if the vertices given by parameter are
         * connected.
         * 
         * @param i the first vertex index.
         * @param j the second vertex index.
         * @return <code>true</code> if the vertices are connected.
         */
        public boolean isConnected(int i, int j){
            return adjacencyMatrix[i][j] != null;
        }
        
        /**
         * Returns the weight of a given edge (i,j).
         *
         * @param i the left vertex index of the edge.
         * @param j the right vertex index of the edge.
         * @return the weight of the edge (<code>null</code> if they are not 
         * connected)
         */
        public Double edgeWeight(int i, int j) {
            return adjacencyMatrix[i][j];
        }
        
        /**
         * Returns the edge with the minimum weight (i.e., the pair of nearest
         * vertices in this graph)
         *
         * @return the edge with the minimum weight (null if there are not
         * edges)
         */
        public Pair<Integer, Integer> minimumEdge() {
            Pair<Integer, Integer> output = null;

            if (num_edges > 0) {
                Double min = Double.MAX_VALUE;
                for (int i = 0; i < num_vertices; i++) {
                    for (int j = i; j < num_vertices; j++) {
                        if (i != j && adjacencyMatrix[i][j] != null
                                   && adjacencyMatrix[i][j] < min) {
                            min = adjacencyMatrix[i][j];
                            output = new Pair(i, j);
                        }
                    }
                }
            }
            return output;
        }
        
        /**
         * Returns the neartest vertices belonging to different components.
         *
         * @return the neartest vertices belonging to different components
         * (<code>null</code> if this graph is a single component graph).
         */
        public Pair<Integer, Integer> nearestUnconnectedVertices() {
            Pair<Integer, Integer> output = null;
            Point3D pi,pj;

            Double min = Double.MAX_VALUE, dist_ij;
            if (num_components > 1) {
                for (int i = 0; i < num_vertices; i++) {
                    for (int j = i + 1; j < num_vertices; j++) {
                        if (!component[i].equals(component[j])) {
                            pi = ((SphericalFuzzyColor) get(i)).getCenter();
                            pj = ((SphericalFuzzyColor) get(j)).getCenter();
                            dist_ij = pi.distance(pj);
                            if (dist_ij < min) {
                                min = dist_ij;
                                output = new Pair(i, j);
                            }
                        }
                    }
                }
            }
            return output;
        }
        
        /**
         * Returns a string representation of this graph.
         *
         * @return a string representation of this graph.
         */
        @Override
        public String toString() {
            String out = "[";
            for (int i = 0; i < size(); i++) {
                out += "("+component[i]+") ";
                for (int j = 0; j < size(); j++) {
                    out += adjacencyMatrix[i][j] != null ? 
                           String.format("%.1f ", adjacencyMatrix[i][j]) : "NoC ";
                }
                out += "\n ";
            }
            return out.substring(0,out.length()-2) + "]";       
        }
    
    } //End of Graph class

}
