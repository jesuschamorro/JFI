/*
    Factory for creating FuzzyContourOld

    @author Luis Suárez Lloréns
 */
package jfi.shape.fuzzy;

import java.util.ArrayList;
import jfi.shape.Contour;
import jfi.utils.JFIMath;

public class FuzzyContourFactory {
    
    public static final int TYPE_LINEARITY = 1;
    public static final int TYPE_VERTICITY = 2;
    
    /**
     * Create a new FuzzyContourOld
     * 
     * @param contour Contour used to create the new FuzzyContourOld
     * @param type Type of FuzzyContourOld
     * 
     * @return A new instance of FuzzyContourOld
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
        FuzzyContour fuzzyContour = new FuzzyContour();
        
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
        FuzzyContour fuzzyContour = new FuzzyContour();
        
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
        int windowSize = contour.size()/15;
        int k = 3;
        
        if (windowSize > numPoints)
            windowSize = numPoints;

        for (int i = 0; i < numPoints; i++) {
            linearity.add((float) JFIMath.getSegmentLinearity(contour.getSegment((i-windowSize+1 + numPoints) % numPoints,2*windowSize)));
        }
        
        return linearity;
    }
    
    private static ArrayList<Float> getLinearity(Contour contour, int offset, int windowSize) {

        ArrayList<Float> linearity =  new ArrayList<>();
        
        int numPoints = contour.size();
        int k = 3;
        
        if (windowSize > numPoints)
            windowSize = numPoints;

        for (int i = 0; i < numPoints; i++) {
            linearity.add((float) JFIMath.getSegmentLinearity(contour.getSegment((i+offset+numPoints) % numPoints, windowSize)));
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
        int windowSize = contour.size()/15;
        double vv_min = 0.1;
        double vv_max = 0.6;
        
        ArrayList<Float> left_linearity = getLinearity(contour,1-windowSize,windowSize);
        ArrayList<Float> right_linearity = getLinearity(contour,0,windowSize);
        ArrayList<Float> centered_linearity = getLinearity(contour);
        
        double actualVerticity; 
        
        for (int i = 0; i < left_linearity.size(); i++){
            actualVerticity = Math.min(escalon(left_linearity.get(i),0,1),escalon(right_linearity.get(i),0,1));
            actualVerticity = Math.min(actualVerticity,escalon(1-centered_linearity.get(i),vv_min,vv_max));
            verticity.add((float) actualVerticity);
        }
        
        return verticity;
    }
    
    private static double escalon(double x, double alpha, double beta){
        if (x < alpha)
            return 0;
        else if (x < beta)
            return (x-alpha)/(beta-alpha);
        
        return 1;
    }
}
