package jfi.shape.fuzzy;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Map;
import jfi.fuzzy.membershipfunction.TrapezoidalFunction;
import jfi.fuzzy.membershipfunction.TriangularFunction;
import jfi.shape.Contour;
import jfi.shape.ContourIterator;
import jfi.shape.ContourSegment;
import jfi.fuzzy.operator.Hedge;
import jfi.fuzzy.operator.TNorm;
import jfi.utils.FuzzyUtils;
import jfi.utils.JFIMath;

/**
 * Class for fuzzy contour templates generation.
 * 
 * @author Luis Suárez Lloréns
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzyContourFactory {
    
    /**
     *  Alpha used to calculate linearity
     */
    private static final double ALPHA_45 = 0.91;
    private static final double ALPHA_90 = 0.814;
    private static final double ALPHA_180 = 0.63;
    private static final double ALPHA_360 = 0.26;
    public static final double DEFAULT_ALPHA = ALPHA_90;  
    public static final double DEFAULT_ALPHA_VERTICITY_LEFT = DEFAULT_ALPHA;
    public static final double DEFAULT_ALPHA_VERTICITY_RIGHT = DEFAULT_ALPHA;
    public static final double DEFAULT_ALPHA_VERTICITY_CENTER = ALPHA_90;
    
    public static final double DEFAULT_BETA = 1.0;   
    public static final double DEFAULT_BETA_VERTICITY_LEFT = DEFAULT_BETA;
    public static final double DEFAULT_BETA_VERTICITY_RIGHT = DEFAULT_BETA;
    public static final double DEFAULT_BETA_VERTICITY_CENTER = 0.97;
    
    public static final double DEFAULT_CONCENTRATION_VERTICITY_LEFT = 1.0;  //1.0 -> No change
    public static final double DEFAULT_CONCENTRATION_VERTICITY_RIGHT = 1.0; //1.0 -> No change
    public static final double DEFAULT_CONCENTRATION_VERTICITY_CENTER = 2.0;
    
    /**
     * Default t-norm used to calculate the salience.
     */
    public static TNorm DEFAULT_SALIENCY_TNORM = TNorm.PRODUCT;
    
    /**
     * Type representing the linearity fuzzy property
     */
    public static final int TYPE_LINEARITY = 1;
    
    /**
     * Type representing the verticity fuzzy property
     */
    public static final int TYPE_VERTICITY = 2;
    
    /**
     * Creates a new <code>FuzzyContour</code> modeling a given fuzzy property
     * for a given contour
     * 
     * @param contour contour used to create the new <code>FuzzyContour</code>
     * @param type type of property to be modeled
     * @return a new instance of <code>FuzzyContour</code>
     */
    public static FuzzyContour getInstance(Contour contour, int type){
        switch (type) {
        case TYPE_LINEARITY:
            return getLinearityInstance(contour);
        case TYPE_VERTICITY:
            return getVerticityInstance(contour);
        default:
            return null;
        }
    }

    /**
     * Creates a new <code>FuzzyContour</code> modeling the linearity property
     * for a given contour
     * 
     * @param contour contour used to create the linearity fuzzy set
     * 
     * @return a fuzzy contour modeling the linearity property
     */
    public static FuzzyContour getLinearityInstance(Contour contour){
        return getLinearityInstance(contour, DEFAULT_ALPHA, (int)(Contour.DEFAULT_WINDOW_RATIO_SIZE * contour.size()));
    }
    
    /**
     * Creates a new <code>FuzzyContour</code> modeling the linearity property
     * for a given contour
     * 
     * @param contour contour used to create the linearity fuzzy set
     * @param alpha coefficient of determination of the curve considered 0 
     * @param segment_size the segment size around each contour point for linearity 
     * 
     * @return a fuzzy contour modeling the linearity property
     */
    public static FuzzyContour getLinearityInstance(Contour contour, double alpha, int segment_size){
        FuzzyContour fuzzyContour = new FuzzyContour("Contour.Linearity", contour);
        setLinearityDegrees(fuzzyContour, alpha, segment_size);
        return fuzzyContour;
    }
    
    /**
     * Calculates and set the membership degrees corresponding to the linearity
     * property. This calculation is performed on the reference set (which is a
     * contour)
     * 
     * @param fcontour fuzzy contour on which to calculate and set the linearity
     * @param alpha coefficient of determination of the curve considered 0 
     * @param segment_size the segment size around each contour point for linearity 
     * calculation 
     */
    private static void setLinearityDegrees(FuzzyContour fcontour, double alpha, int segment_size) { 
        ArrayList segment;
        double degree;
        Contour ccontour = fcontour.getContourReferenceSet(); 
 
        for(Point2D point:ccontour){
            segment = ccontour.getSegment(ccontour.getPointBeside(point,-(segment_size/2)+1), segment_size);
            degree = linearityDegree(segment,alpha);
            fcontour.setMembershipDegree(point,degree);
        }
    }
    
    /**
     * Creates a new <code>FuzzyContour</code> modeling the linearity property
     * for a given contour segmentation
     * 
     * @param contour contour used to create the linearity fuzzy set 
     * @param segmentation contour segments 
     * @return a fuzzy contour 
     */
    public static FuzzyContour getLinearityInstance(Contour contour, ArrayList<ContourSegment> segmentation){
        return getLinearityInstance(contour, DEFAULT_ALPHA, segmentation);
    }
    
    /**
     * Creates a new <code>FuzzyContour</code> modeling the linearity property
     * for a given contour segmentation
     * 
     * @param contour contour used to create the linearity fuzzy set
     * @param alpha coefficient of determination of the curve considered 0 
     * @param segmentation contour segments 
     * @return a fuzzy contour 
     */
    public static FuzzyContour getLinearityInstance(Contour contour, double alpha, ArrayList<ContourSegment> segmentation){
        FuzzyContour fuzzyContour = new FuzzyContour("Contour.Linearity", contour);
        setLinearityDegrees(fuzzyContour, alpha, segmentation);
        return fuzzyContour;
    }
    
    /**
     * Calculates and set the membership degrees corresponding to the linearity
     * property for a given segmentation. It gives de same degree to all the 
     * points in the same segment (endpoints includeded).
     * 
     * @param fcontour fuzzy contour on which to calculate and set the linearity
     * @param alpha coefficient of determination of the curve considered
     * @param segmentation contour segments
     */
    private static void setLinearityDegrees(FuzzyContour fcontour, double alpha, ArrayList<ContourSegment> segmentation) { 
        ArrayList segment;
        double degree;
        Point2D point;
        
        Contour ccontour = fcontour.getContourReferenceSet();         
        for (ContourSegment s : segmentation) {            
            segment = s.toArray();
            degree = linearityDegree(segment,alpha);
            
            System.out.println("\nSegmento "+s.getStartPoint()+"->"+s.getEndPoint()+" : "+degree);
            
            // We set the same degree to all the points in the segment (endpoints included)
            ContourIterator it = new ContourIterator(ccontour,s.getStartPoint(),s.isClockwise());
            while(!it.isPrevious(s.getEndPoint())){
                point = it.next();
                fcontour.setMembershipDegree(point,degree);
            }          
        }       
    }
        
    /**
     * Returns the linearity degree of a given segment using a trapezoidal function as adjustment
     * @param segment the segment of points
     * @param alpha coefficient of determination of the curve considered 0 
     * @return the linearity degree
     */
    public static double linearityDegree(ArrayList segment, double alpha){  
        return linearityDegree(segment, alpha, DEFAULT_BETA);
    }
    
    /**
     * Returns the linearity degree of a given segment using a trapezoidal function as adjustment
     * @param segment the segment of points
     * @param alpha coefficient of determination of the curve considered 0 
     * @param beta coefficient of determination of curve considered 1
     * @return the linearity degree
     */
    public static double linearityDegree(ArrayList segment, double alpha, double beta){  
        TrapezoidalFunction linearity_adjust = new TrapezoidalFunction(alpha,beta,1.0,1.0); 
        return linearity_adjust.apply(JFIMath.CoefficientDetermination(segment));
    }
    
    
    
    /**
     * Creates a new <code>FuzzyContour</code> modeling the linearity property
     * for a given contour
     * 
     * @param contour contour used to create the linearity fuzzy set
     * 
     * @return a fuzzy contour modeling the linearity property
     */
    public static FuzzyContour getCurvacityInstance(Contour contour){
        return getCurvacityInstance(contour, DEFAULT_ALPHA, (int)(Contour.DEFAULT_WINDOW_RATIO_SIZE * contour.size()));
    }
    
    /**
     * Creates a new <code>FuzzyContour</code> modeling the linearity property
     * for a given contour
     * 
     * @param contour contour used to create the linearity fuzzy set
     * @param alpha coefficient of determination of the curve considered 0 
     * @param segment_size the segment size around each contour point for linearity 
     * 
     * @return a fuzzy contour modeling the linearity property
     */
    public static FuzzyContour getCurvacityInstance(Contour contour, double alpha, int segment_size){
        FuzzyContour fuzzyContour = getLinearityInstance(contour,alpha,segment_size);
        fuzzyContour = (FuzzyContour) FuzzyUtils.negation(fuzzyContour);
        fuzzyContour.setLabel("Contour.Curvacity");
        return fuzzyContour;
    }
    
    
    
    
    
    
    /**
     * Create a new FuzzyContour using verticity as truth value
     * 
     * @param contour Contour used to create the new FuzzyContourOld
     * 
     * @return A new instance of FuzzyContour
     */    
    public static FuzzyContour getVerticityInstance(Contour contour){
        return getVerticityInstance(contour, DEFAULT_ALPHA, (int)(Contour.DEFAULT_WINDOW_RATIO_SIZE * contour.size()),Contour.DEFAULT_OFFSET);
    }
    
    /**
     * Creates a new FuzzyContour using verticity as truth value
     * 
     * @param contour Contour used to create the new FuzzyContourOld
     * @param alpha coefficient of determination of the curve considered 0 
     * @param segment_size the segment size for verticity calculation
     * @param offset distance from the point to verticity calculation
     * 
     * @return A new instance of FuzzyContour
     */ 
    public static FuzzyContour getVerticityInstance(Contour contour, double alpha, int segment_size, int offset){
        FuzzyContour fuzzyContour = new FuzzyContour("Contour.Verticity", contour);
        setVerticityDegrees(fuzzyContour, alpha, segment_size, offset);
        return fuzzyContour;
    }

    /**
     * Calculates and set the membership degrees corresponding to the verticity
     * property. This calculation is performed on the reference set (which is a
     * contour)
     * 
     * @param fcontour fuzzy contour on which to calculate and set the verticity
     * @param alpha coefficient of determination of the curve considered 0 
     * @param segment_size the segment size for verticity calculation
     * @param offset distance from the point to verticity calculation
     * 
     */
    private static void setVerticityDegrees(FuzzyContour fcontour, double alpha, int segment_size, int offset){
        ArrayList left_segment, right_segment, centered_segment;
        double degree, ldegree_left, ldegree_right, ldegree_center, noldegree_center;
        Contour ccontour = fcontour.getContourReferenceSet();

        for(Point2D point:ccontour){
            left_segment = ccontour.getSegment(ccontour.getPointBeside(point, -segment_size-offset+1), segment_size);
            right_segment = ccontour.getSegment(ccontour.getPointBeside(point, offset), segment_size);
            centered_segment = ccontour.getSegment(ccontour.getPointBeside(point, -segment_size/2+1), segment_size);
                     
            ldegree_left = linearityDegree(left_segment,alpha);
            ldegree_right = linearityDegree(right_segment,alpha);
            ldegree_center = linearityDegree(centered_segment,DEFAULT_ALPHA_VERTICITY_CENTER,DEFAULT_BETA_VERTICITY_CENTER);
            
            ldegree_left = Hedge.concentration(ldegree_left,DEFAULT_CONCENTRATION_VERTICITY_LEFT);
            ldegree_right = Hedge.concentration(ldegree_right,DEFAULT_CONCENTRATION_VERTICITY_RIGHT);
            noldegree_center = Hedge.concentration(1.0-ldegree_center,DEFAULT_CONCENTRATION_VERTICITY_CENTER);

            degree = JFIMath.min(ldegree_left,ldegree_right, noldegree_center);                                    
            fcontour.setMembershipDegree(point,degree);
        }
    }
         
    /**
     * Creates a new <code>FuzzyContour</code> modeling the maximality of a
     * given fuzzy contour.
     * 
     * Maximality measures the degree in which a contour point value is higher
     * than almoost all the values around it.
     *
     * @param fcontour fuzzy contour to be analyzed 
     * @param alpha parameter of the 'almost all' quantifier. It represents the 
     * number of points (greater than the one studied) above which the point is 
     * not considered a maximum. The quantifier is modelled with a triangular
     * membership function with parameter (0,0,alpha).
     * @param window_size size of the window around the countour point used to
     * check the local maximality.
     * 
     * @return a fuzzy contour modeling the maximality
     */
    public static FuzzyContour getMaximalityInstance(FuzzyContour fcontour, double alpha, int window_size){        
        FuzzyContour maxima = new FuzzyContour("Contour.Maximality");
        TriangularFunction quantifier_almostAll = new TriangularFunction(0.0,0.0,alpha);           
        
        ArrayList<Map.Entry> entry_list = new ArrayList(fcontour.entrySet());
        double i_degree, w_degree;
        int wsize_half, w_index, w, i, num_higher_points;
        
        wsize_half = (int)(window_size/2);  
        for (i = 0; i < entry_list.size(); i++) {
            i_degree = (Double) entry_list.get(i).getValue();
            for (w = -wsize_half, num_higher_points=0; w <= wsize_half; w++) {
                w_index = (i + w + entry_list.size()) % entry_list.size();
                w_degree = (Double) entry_list.get(w_index).getValue();
                if(w != 0 && i_degree < w_degree) num_higher_points++;                
            }
            i_degree = quantifier_almostAll.apply(num_higher_points);
            maxima.add((Point2D) entry_list.get(i).getKey(), i_degree ); 
        }
        return maxima;
    }
     
    
    /**
     * Creates a new <code>FuzzyContour</code> modeling the saliency of a given
     * fuzzy contour. Saliency is measured for each contour point on the basis
     * of its curvacity and its maximalty: A point is a salience point if (1) it
     * has curvacity enough and (2) its curvacity value is higher than almost
     * all the values around it.
     *
     * @param contour contour to be analyzed
     * @param window_size_curvacity size of the window around the countour point
     * used to check the local curvacity.
     * @param alpha_curvacity coefficient of determination of the arch
     * associated to zero membeship degree to linearity. It is used to calculate
     * the curvacity.
     * @param window_size_maxima size of the window around the countour point
     * used to check the local maximality.
     * @param alpha_quantifier_almostall parameter of the 'almost all'
     * quantifier used to calculate the maximality. It represents the number of
     * points (greater than the one studied) above which the point is not
     * considered a maximum. The quantifier is modelled with a triangular
     * membership function with parameter (0,0,alpha).
     * @param alpha_quantifier_enough parameter alpha of the 'enough' quantifier
     * used to analyze if a countour point has curvacity enough. Any value lower
     * than alpha is considered non-enough (i.e., the membebmeship degree to
     * 'enough' is zero).
     * @param beta_quantifier_enough parameter alpha of the 'enough' quantifier
     * used to analyze if a countour point has curvacity enough. Any value
     * higher than beta is considered enough (i.e., the membebmeship degree to
     * 'enough' is one). Any value between alfa and beta will have membebmeship
     * degree to 'enough' between 0 and 1 (the quantifier is modelled with a
     * trapezoidal function with parameter (alpha,beta,1,1)).
     * @param tnorm the tnorm used to aggregate the 'curvacity enough' and the
     * 'curvacity higher than almost all' membership degrees
     *
     * @return a fuzzy contour modeling the saliency.
     */
    public static FuzzyContour getSaliencyInstance(Contour contour, int window_size_curvacity, double alpha_curvacity, 
            int window_size_maxima, double alpha_quantifier_almostall,double alpha_quantifier_enough, double beta_quantifier_enough, TNorm tnorm) {
         
        FuzzyContour saliency = new FuzzyContour("Contour.Saliency");
        TriangularFunction quantifier_almostAll = new TriangularFunction(0.0, 0.0, alpha_quantifier_almostall);
        TrapezoidalFunction quantifier_enough = new TrapezoidalFunction(alpha_quantifier_enough,beta_quantifier_enough,1.0,1.0); 
        double i_degree, w_degree;
        int wsize_half, w_index, w, i, num_higher_points;
                
        FuzzyContour fcontourLinealidad = FuzzyContourFactory.getLinearityInstance(contour, alpha_curvacity, window_size_curvacity);
        FuzzyContour fcontornoCurvacidad = (FuzzyContour) FuzzyUtils.negation(fcontourLinealidad);
        ArrayList<Map.Entry> entry_list = new ArrayList(fcontornoCurvacidad.entrySet());
        
        wsize_half = (int)(window_size_maxima/2);  
        for (i = 0; i < entry_list.size(); i++) {
            i_degree = (Double) entry_list.get(i).getValue();
            for (w = -wsize_half, num_higher_points=0; w <= wsize_half; w++) {
                w_index = (i + w + entry_list.size()) % entry_list.size();
                w_degree = (Double) entry_list.get(w_index).getValue();
                if(w != 0 && i_degree < w_degree) num_higher_points++;                
            }
            i_degree = tnorm.apply( quantifier_enough.apply(i_degree) , quantifier_almostAll.apply(num_higher_points) );
            saliency.add((Point2D) entry_list.get(i).getKey(), i_degree );  
        }
        
        return saliency;
    }
    
    /**
     * Creates a new <code>FuzzyContour</code> modeling the saliency of a given
     * fuzzy contour. Saliency is measured for each contour point on the basis
     * of its curvacity and its maximalty: A point is a salience point if (1) it
     * has curvacity enough and (2) its curvacity value is higher than almost
     * all the values around it. By default, the product is used to aggregate 
     * both concepts.
     *
     * @param contour contour to be analyzed
     * @param window_size_curvacity size of the window around the countour point
     * used to check the local curvacity.
     * @param alpha_curvacity coefficient of determination of the arch
     * associated to zero membeship degree to linearity. It is used to calculate
     * the curvacity.
     * @param window_size_maxima size of the window around the countour point
     * used to check the local maximality.
     * @param alpha_quantifier_almostall parameter of the 'almost all'
     * quantifier used to calculate the maximality. It represents the number of
     * points (greater than the one studied) above which the point is not
     * considered a maximum. The quantifier is modelled with a triangular
     * membership function with parameter (0,0,alpha).
     * @param alpha_quantifier_enough parameter alpha of the 'enough' quantifier
     * used to analyze if a countour point has curvacity enough. Any value lower
     * than alpha is considered non-enough (i.e., the membebmeship degree to
     * 'enough' is zero).
     * @param beta_quantifier_enough parameter alpha of the 'enough' quantifier
     * used to analyze if a countour point has curvacity enough. Any value
     * higher than beta is considered enough (i.e., the membebmeship degree to
     * 'enough' is one). Any value between alfa and beta will have membebmeship
     * degree to 'enough' between 0 and 1 (the quantifier is modelled with a
     * trapezoidal function with parameter (alpha,beta,1,1)).
     *
     * @return a fuzzy contour modeling the saliency.
     */
    public static FuzzyContour getSaliencyInstance(Contour contour, int window_size_curvacity, double alpha_curvacity, 
            int window_size_maxima, double alpha_quantifier_almostall,double alpha_quantifier_enough, double beta_quantifier_enough) {
        return getSaliencyInstance(contour,window_size_curvacity,alpha_curvacity, 
                window_size_maxima, alpha_quantifier_almostall, alpha_quantifier_enough, beta_quantifier_enough, DEFAULT_SALIENCY_TNORM);
    }
    
}
