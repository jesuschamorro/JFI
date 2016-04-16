package jfi.shape.fuzzy;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import jfi.fuzzy.membershipfunction.TrapezoidalFunction;
import jfi.shape.Contour;
import jfi.utils.FuzzyHedges;
import jfi.utils.JFIMath;

/**
 * Class for fuzzy contour templates generation.
 * 
 * @author Luis Suárez Lloréns
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzyContourFactory {
    
    /**
     *  Exponent used to calculate linearity
     */
    private static final int DEFAULT_K = 3;    
    
    /**
     * Parameters used to check if the segment is curved enough
     * in verticity calculations
     */
    private static final double VERTICITY_MIN = 0.1;
    private static final double VERTICITY_MAX = 0.6;
    
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
     * @return 
     */
    public static FuzzyContour getLinearityInstance(Contour contour){
        return getLinearityInstance(contour, DEFAULT_K, (int)(Contour.DEFAULT_WINDOW_RATIO_SIZE * contour.size()));
    }
    
    /**
     * Creates a new <code>FuzzyContour</code> modeling the linearity property
     * for a given contour
     * 
     * @param contour contour used to create the linearity fuzzy set
     * @param exponent exponent in the linearity formula
     * @param segment_size the segment size around each contour point for linearity 
     * @return 
     */
    public static FuzzyContour getLinearityInstance(Contour contour, int exponent, int segment_size){
        FuzzyContour fuzzyContour = new FuzzyContour("Contour.Linearity", contour);
        setLinearityDegrees(fuzzyContour, exponent, segment_size);
        return fuzzyContour;
    }
    
    /**
     * Calculates and set the membership degrees corresponding to the linearity
     * property. This calculation is performed on the reference set (which is a
     * contour)
     * 
     * @param fcontour fuzzy contour on which to calculate and set the linearity
     * @param exponent exponent in the linearity formula
     * @param segment_size the segment size around each contour point for linearity 
     * calculation 
     */
    private static void setLinearityDegrees(FuzzyContour fcontour, int exponent, int segment_size) { 
        ArrayList segment;
        double degree;
        Contour ccontour = fcontour.getContourReferenceSet(); 
 
        for(Point2D point:ccontour){
            segment = ccontour.getSegment(ccontour.getPointBeside(point,-(segment_size/2)+1), segment_size);
            degree = linearityDegree(segment,exponent);
            fcontour.setMembershipDegree(point,degree);
        }
    }
    
    /**
     * Returns the linearity degree of a given segment
     * @param segment the segment of points
     * @param exponent the exponent formula parameter
     * @return the linearity degree
     */
    public static double linearityDegree(ArrayList segment, int exponent){
        return Math.pow(JFIMath.getCoefficientDetermination(segment),exponent);
    }
    
    /**
     * Create a new FuzzyContour using verticity as truth value
     * 
     * @param contour Contour used to create the new FuzzyContourOld
     * 
     * @return A new instance of FuzzyContour
     */    
    public static FuzzyContour getVerticityInstance(Contour contour){
        return getVerticityInstance(contour, DEFAULT_K, (int)(Contour.DEFAULT_WINDOW_RATIO_SIZE * contour.size()),Contour.DEFAULT_OFFSET);
    }
    
    /**
     * Create a new FuzzyContour using verticity as truth value
     * 
     * @param contour Contour used to create the new FuzzyContourOld
     * @param exponent exponent in the linearity formula
     * @param segment_size the segment size for verticity calculation
     * @param offset distance from the point to verticity calculation
     * 
     * @return A new instance of FuzzyContour
     */ 
    public static FuzzyContour getVerticityInstance(Contour contour, int exponent, int segment_size, int offset){
        FuzzyContour fuzzyContour = new FuzzyContour("Contour.Verticity", contour);
        setVerticityDegrees(fuzzyContour, exponent, segment_size, offset);
        return fuzzyContour;
    }

    /**
     * Calculates and set the membership degrees corresponding to the verticity
     * property. This calculation is performed on the reference set (which is a
     * contour)
     * 
     * @param fcontour fuzzy contour on which to calculate and set the verticity
     * @param exponent exponent in the linearity formula
     * @param segment_size the segment size for verticity calculation
     * @param offset distance from the point to verticity calculation
     * 
     */
    private static void setVerticityDegrees(FuzzyContour fcontour, int exponent, int segment_size, int offset){
        ArrayList left_segment, right_segment, centered_segment;
        double degree, ldegree_left, ldegree_right, ldegree_center, very_ldegree_center;
        Contour ccontour = fcontour.getContourReferenceSet();
        TrapezoidalFunction range_adjust = new TrapezoidalFunction(0.05,1.0,1.0,1.0);  
        
        for(Point2D point:ccontour){
            left_segment = ccontour.getSegment(ccontour.getPointBeside(point, -segment_size-offset+1), segment_size);
            right_segment = ccontour.getSegment(ccontour.getPointBeside(point, offset), segment_size);
            centered_segment = ccontour.getSegment(ccontour.getPointBeside(point, -segment_size/2+1), segment_size);
                     
            ldegree_left = linearityDegree(left_segment,exponent);
            ldegree_right = linearityDegree(right_segment,exponent);
            ldegree_center = linearityDegree(centered_segment,exponent);
            
            //Segun articulo
            very_ldegree_center =  FuzzyHedges.veryvery(ldegree_center); //Math.pow(ldegree_center,4.0);            
            very_ldegree_center = range_adjust.apply(very_ldegree_center);  // Ajuste superior para alcanzar 1.0             
            degree = JFIMath.min(ldegree_left,ldegree_right, 1.0-very_ldegree_center);
            
            //Implementación actual:
            //TrapezoidalFunction trapezoidal_function = new TrapezoidalFunction(VERTICITY_MIN,VERTICITY_MAX,1,1.1);    
            //double cosa_de_luis = trapezoidal_function.apply(1.0-ldegree_center);            
            //degree = Math.min(Math.min(ldegree_left,ldegree_right),cosa_de_luis);
                                    
            fcontour.setMembershipDegree(point,degree);
        }
    }
            
    
}
