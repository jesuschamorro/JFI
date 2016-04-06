package jfi.shape.fuzzy;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import jfi.fuzzy.membershipfunction.TrapezoidalFunction;
import jfi.shape.Contour;
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
    public static final int K = 3;
    
    //************************************** K parámetro en método y lo anterior valor por defecto?
    
    
    /**
     * Parameters used to check if the segment is curved enough
     * in verticity calculations
     */
    public static final double VERTICITY_MIN = 0.1;
    public static final double VERTICITY_MAX = 0.6;
    
    //************************************** parámetros en método y lo anterior valores por defecto?

    
    
    /**
     * Types of FuzzyContour available
     */
    public static final int TYPE_LINEARITY = 1;
    public static final int TYPE_VERTICITY = 2;
    
    /**
     * Create a new <code>FuzzyContour</code>
     * 
     * @param contour contour used to create the new <code>FuzzyContour</code>
     * @param type type of FuzzyContour
     * @return a new instance of FuzzyContour
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
     * Create a new FuzzyContour using linearity as truth value
     *
     * @param contour Contour used to create the new FuzzyContourOld
     * @return A new instance of FuzzyContourOld
     */
    private static FuzzyContour getLinearityInstance(Contour contour){
        FuzzyContour fuzzyContour = new FuzzyContour("Contour.Linearity");
        ArrayList<Float> linearity_degrees = getLinearityDegrees(contour);        
        for(int i = 0; i < contour.size(); i++){
            fuzzyContour.add(contour.get(i), linearity_degrees.get(i));
        }  
        return fuzzyContour;
    }
    
    
    
    private static FuzzyContour getLinearityInstance_new(Contour contour){
        FuzzyContour fuzzyContour = new FuzzyContour("Contour.Linearity", contour);
        setLinearityDegrees(fuzzyContour);
        return fuzzyContour;
    }
    
    private static void setLinearityDegrees(FuzzyContour fcontour) {  //Parámetros: windowRatio, k
        int numPoints = fcontour.size();
        int windowSize = (int)(Contour.DEFAULT_WINDOW_RATIO_SIZE * numPoints);       
        double degree;
        Contour c = fcontour.toCrispContour();  //Porque getSegment está en Contour
        int i = 0;                              //Porque usa índices para el tamaño de las ventanas
        for(Point2D point:fcontour){
            degree = Math.pow(JFIMath.getRegressionError(c.getSegment((i-windowSize+1 + numPoints) % numPoints,2*windowSize)),K);
            fcontour.setMembershipValue(point,degree);
            i++;
        }
    }
    
    
    /**
     * Create a new FuzzyContourOld using verticity as truth value
     * 
     * @param contour Contour used to create the new FuzzyContourOld
     * 
     * @return A new instance of FuzzyContourOld
     */    
    private static FuzzyContour getVerticityInstance(Contour contour){
        FuzzyContour fuzzyContour = new FuzzyContour("Contour.Verticity");
        ArrayList<Float> verticity_degrees = getVerticityDegrees(contour);
        for(int i = 0; i < contour.size(); i++){
            fuzzyContour.add(contour.get(i), verticity_degrees.get(i));
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
    private static ArrayList<Float> getLinearityDegrees(Contour contour) {
        ArrayList<Float> linearity =  new ArrayList<>();     
        int numPoints = contour.size();
        int windowSize = (int) (Contour.DEFAULT_WINDOW_RATIO_SIZE * contour.size());       
        if (windowSize > numPoints)
            windowSize = numPoints;       
        for (int i = 0; i < numPoints; i++) {          
            linearity.add((float) Math.pow(JFIMath.getRegressionError(contour.getSegment((i-windowSize+1 + numPoints) % numPoints,2*windowSize)),K));
        }       
        return linearity;
    }
    
    /**
     * Calculates the linearity of a contour
     * 
     * @param contour
     * @param offset
     * @param windowSize 
     * @return Contour's linearity
     */
    private static ArrayList<Float> getLinearityDegrees(Contour contour, int offset, int windowSize) {
        ArrayList<Float> linearity =  new ArrayList<>();       
        int numPoints = contour.size();       
        if (windowSize > numPoints)
            windowSize = numPoints;
        for (int i = 0; i < numPoints; i++) {
            linearity.add((float) Math.pow(JFIMath.getRegressionError(contour.getSegment((i+offset+numPoints) % numPoints, windowSize)),K));
        }       
        return linearity;
    }
    
    /**
     * Compute the verticity of contour
     * 
     * @param contour
     * @return Contour's verticity
     */
    private static ArrayList<Float> getVerticityDegrees(Contour contour) {     
        ArrayList<Float> verticity = new ArrayList<>();
        int windowSize = (int) (Contour.DEFAULT_WINDOW_RATIO_SIZE * contour.size());      
        TrapezoidalFunction trapezoidal_function = new TrapezoidalFunction(VERTICITY_MIN,VERTICITY_MAX,1,1.1);      
        ArrayList<Float> left_linearity = getLinearityDegrees(contour,1-windowSize-Contour.DEFAULT_OFFSET,windowSize);
        ArrayList<Float> right_linearity = getLinearityDegrees(contour,Contour.DEFAULT_OFFSET,windowSize);
        ArrayList<Float> centered_linearity = getLinearityDegrees(contour);     
        double actualVerticity;      
        for (int i = 0; i < left_linearity.size(); i++){
            actualVerticity = Math.min(left_linearity.get(i),right_linearity.get(i));
            actualVerticity = Math.min(actualVerticity,trapezoidal_function.apply(1-centered_linearity.get(i)));
            verticity.add((float) actualVerticity);
        }    
        return verticity;
    }
    
}
