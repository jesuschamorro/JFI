/*
    Factory for creating FuzzyContourOld

    @author Luis Suárez Lloréns
 */
package jfi.shape.fuzzy;

import java.util.ArrayList;
import jfi.fuzzy.membershipfunction.TrapezoidalFunction;
import jfi.shape.Contour;
import jfi.utils.JFIMath;

public class FuzzyContourFactory {
    
    public static final int K = 3;
    
    public static double vv_min = 0.1;
    public static double vv_max = 0.6;
    
    public static final int TYPE_LINEARITY = 1;
    public static final int TYPE_VERTICITY = 2;
    
    /**
     * Create a new FuzzyContour
     * 
     * @param contour Contour used to create the new FuzzyContour
     * @param type Type of FuzzyContour
     * 
     * @return A new instance of FuzzyContour
     */
    public static FuzzyContour getInstance(Contour contour, int type){

        switch (type) {
        case TYPE_LINEARITY:
            return getInstanceLinearity(contour);
        case TYPE_VERTICITY:
            return getInstanceVerticity(contour);
        default:
            return null;
        }
    }

    /**
     * Create a new FuzzyContour using linearity as truth value
     * 
     * @param contour Contour used to create the new FuzzyContourOld
     * 
     * @return A new instance of FuzzyContourOld
     */
    private static FuzzyContour getInstanceLinearity(Contour contour){
        FuzzyContour fuzzyContour = new FuzzyContour("Linearity");
        
        ArrayList<Float> linearity = getLinearity(contour);       
        
        for(int i = 0; i < contour.size(); i++){
            fuzzyContour.add(contour.get(i), linearity.get(i));
        }
        
        return fuzzyContour;
    }
    
    /**
     * Create a new FuzzyContourOld using verticity as truth value
     * 
     * @param contour Contour used to create the new FuzzyContourOld
     * 
     * @return A new instance of FuzzyContourOld
     */    
    private static FuzzyContour getInstanceVerticity(Contour contour){
        FuzzyContour fuzzyContour = new FuzzyContour("Verticity");
        
        ArrayList<Float> verticity = getVerticity(contour);
        
        for(int i = 0; i < contour.size(); i++){
            fuzzyContour.add(contour.get(i), verticity.get(i));
        }
        
        return fuzzyContour;
    }
    
    /**
     * Compute the linearity of contour
     * 
     * @param contour Contour used
     * 
     * @return Contour's linearity
     */
    private static ArrayList<Float> getLinearity(Contour contour) {

        ArrayList<Float> linearity =  new ArrayList<>();
        
        int numPoints = contour.size();
        int windowSize = (int) (Contour.windowRatio * contour.size());
        
        if (windowSize > numPoints)
            windowSize = numPoints;
        
        for (int i = 0; i < numPoints; i++) {          
            linearity.add((float) Math.pow(JFIMath.getSegmentRegressionError(contour.getSegment((i-windowSize+1 + numPoints) % numPoints,2*windowSize)),K));
        }
        
        return linearity;
    }
    
    private static ArrayList<Float> getLinearity(Contour contour, int offset, int windowSize) {

        ArrayList<Float> linearity =  new ArrayList<>();
        
        int numPoints = contour.size();
        
        if (windowSize > numPoints)
            windowSize = numPoints;

        for (int i = 0; i < numPoints; i++) {
            linearity.add((float) Math.pow(JFIMath.getSegmentRegressionError(contour.getSegment((i+offset+numPoints) % numPoints, windowSize)),K));
        }
        
        return linearity;
    }
    
    /**
     * Compute the verticity of contour
     * 
     * @param contour Contour used
     * 
     * @return Contour's verticity
     */
    private static ArrayList<Float> getVerticity(Contour contour) {
        
        ArrayList<Float> verticity = new ArrayList<>();
        int windowSize = (int) (Contour.windowRatio * contour.size());
        
        TrapezoidalFunction trapezoidal_function = new TrapezoidalFunction(vv_min,vv_max,1,1.1);
        
        ArrayList<Float> left_linearity = getLinearity(contour,1-windowSize-Contour.offset,windowSize);
        ArrayList<Float> right_linearity = getLinearity(contour,Contour.offset,windowSize);
        ArrayList<Float> centered_linearity = getLinearity(contour);
        
        double actualVerticity; 
        
        for (int i = 0; i < left_linearity.size(); i++){
            actualVerticity = Math.min(left_linearity.get(i),right_linearity.get(i));
            actualVerticity = Math.min(actualVerticity,trapezoidal_function.apply(1-centered_linearity.get(i)));
            verticity.add((float) actualVerticity);
        }
        
        return verticity;
    }
    
}
