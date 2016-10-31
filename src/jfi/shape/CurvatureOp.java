package jfi.shape;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import jfi.utils.JFIMath;

/**
 * Class implementing the curvature estimation of a contour
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class CurvatureOp {   
    /**
     * Inner enumeration related to the type of curvature estimation approaches 
     * used in this class.
     * 
     *  - ESTIMATION_LINE_BASED : Estimation based on line fitting
     */
    public enum Approach{LINE_BASED_APPROACH, 
                         LINE_BASED_APPROACH_IMPROVED, 
                         LINE_BASED_APPROACH_SOTO};
    
    /**
     *  Default ratio for segment size estimation.
     */
    public static final double DEFAULT_SEGMENT_RATIO_SIZE = Contour.DEFAULT_WINDOW_RATIO_SIZE;
    
    /**
     *  Default offset.
     */
    public final static int DEFAULT_OFFSET = 5;
    
    /**
     * Default approach used for curvature estimation
     */
    public final static Approach DEFAULT_ESTIMATION =Approach.LINE_BASED_APPROACH; 
    
    /**
     * Segment size for curvarure estimation.
     */
    private Integer segmentSize;
    
    /**
     * Segment size ratio (in relation to the contour size) for curvarure estimation.
     */
    private Double segmentSizeRatio;

    /**
     * Distance (in points) between the point where curvature is calculated and 
     * the start of the segment.
     */
    private int offset;
     
    /**
     * The method used for curvature estimation.
     */
    private Approach estimationMethod;
        
    /**
     * Constructs a new curvature operator using the default parameters.
     */
    public CurvatureOp(){        
        this(DEFAULT_SEGMENT_RATIO_SIZE,DEFAULT_OFFSET);
    }
    
    /**
     * Constructs a new curvature operator given a fixed segment size for
     * curvature estimation (this size will be used regardless the size of the 
     * contour). The default offset is used.
     * 
     * @param segmentSize the segment size (in points) for curvature estimation.
     * It must be greater than 2.
     */
    public CurvatureOp(int segmentSize){        
        this(segmentSize,DEFAULT_OFFSET);
    }   
    
    /**
     * Constructs a new curvature operator given a segment size ratio. This ratio 
     * is in relation to the contour size and it is used to calculate the segment 
     * size adapted to each contour. The default offset is used.
     * 
     * @param segmentSizeRatio the segment size ratio for curvature estimation. 
     * It must be a value between 0 an 1.
     */
    public CurvatureOp(double segmentSizeRatio){        
        this(segmentSizeRatio,DEFAULT_OFFSET);
    }
    
    
    /**
     * Constructs a new curvature operator given a fixed segment size for
     * curvature estimation (this size will be used regardless the size of the 
     * contour).
     * 
     * @param segmentSize the segment size (in points) for curvature estimation.
     * It must be greater than 2.
     * @param offset the offset
     */
    public CurvatureOp(int segmentSize, int offset){
        this.setSegmentSize(segmentSize);
        this.setOffset(offset);
        this.setEstimationMethod(DEFAULT_ESTIMATION);
    }
    
    /**
     * Constructs a new curvature operator given a segment size ratio. This ratio 
     * is in relation to the contour size and it is used to calculate the segment 
     * size adapted to each contour.
     * 
     * @param windowSizeRatio the segment size ratio for curvature estimation. 
     * It must be a value between 0 an 1.
     * @param offset the offset
     */
    public CurvatureOp(double windowSizeRatio, int offset){
        this.setSegmentSizeRatio(windowSizeRatio);
        this.setOffset(offset);
        this.setEstimationMethod(DEFAULT_ESTIMATION);
    }
    
    /**
     * Set the segment size (in points) for curvature estimation. 
     * 
     * This size will be used in any contoour curvature estimation, regardless 
     * the size of the contour. The segment size ratio parameter is set 
     * to <tt>null</tt> 
     * 
     * @param segmentSize the segment size (in points) for curvature estimation.
     * It must be greater than 2.
     */
    public final void setSegmentSize(int segmentSize) {        
        if(segmentSize<2){
            throw new InvalidParameterException("The segment size must be greater than 2.");
        }
        this.segmentSize = segmentSize;
        this.segmentSizeRatio = null;
    }
    
    /**
     * Set the segment size ratio (a value between 0 an 1) for curvature 
     * estimation. This ratio is in relation to the contour size and it will be
     * used to calculate the segment size adapted to each contour.
     * 
     * The segment size parameter, used only when the segment size is fixed 
     * regardless the size of the contour, is set to <tt>null</tt> 
     * 
     * @param segmentSizeRatio the segment size ratio for curvature estimation. 
     * It must be a value between 0 an 1.
     */
    public final void setSegmentSizeRatio(double segmentSizeRatio) {
        if(segmentSizeRatio<0.0 || segmentSizeRatio>1.0){
            throw new InvalidParameterException("The segment size ratio must be between 0 and 1.");
        }
        this.segmentSizeRatio = segmentSizeRatio;
        this.segmentSize = null;
    }
    
    /**
     * Set the offset value used for curvature estimation.
     * 
     * @param offset the offset value
     */
    public final void setOffset(int offset) {        
        this.offset = offset;
    }
    
    /**
     * Set the method used for curvature estimation.
     * 
     * @param methodType the offset value
     */
    public final void setEstimationMethod(Approach methodType) {
        this.estimationMethod = methodType;
    }
    
    /**
     * Returns the segment size used for curvature estimation (<tt>null</tt>
     * if this paremeter is not used).
     * 
     * @return the segment size
     */
    public Integer getWindowSize() {
        return segmentSize;
    }
    
    /**
     * Returns the segment size ratio used for curvature estimation (<tt>null</tt>
     * if this paremeter is not used).
     * 
     * @return the segment size ratio
     */
    public Double getWindowSizeRatio() {
        return segmentSizeRatio;
    }
    
    /**
     * Returns the offset value used for curvature estimation
     * 
     * @return the offset value
     */
    public int getOffset() {
        return offset;
    }
       
    /**
     * Calculates the curvature of the given contour. 
     * 
     * @param contour the contour to be analyzed
     * 
     * @return Curvature of the contour
     */
    public CurvatureFunction apply(Contour contour){
        CurvatureFunction output=null;
        switch(estimationMethod){
            case LINE_BASED_APPROACH:
                output = curvatureLineBased(contour);
                break;
            case LINE_BASED_APPROACH_IMPROVED:
                output = curvatureLineBasedImproved(contour);
                break;
            case LINE_BASED_APPROACH_SOTO:
                output = curvatureLineBasedSoto(contour);
                break;    
        }        
        return output;
    }
    
    /**
     * Calculates the curvature of the given contour. Is applies the standar
     * procedure based on the estimation of two lines associated to the left and 
     * right segment around the the analyzed point, and the estimation of the
     * curvature as the angle between these lines.
     * 
     * @param contour the contour to be analyzed
     * 
     * @return Curvature of the contour
     */
    private CurvatureFunction curvatureLineBased(Contour contour){
        if(contour.isEmpty()){
            return null;
        }
        double currentCurvature;
        double arc_cos, arc_cos2;
        Point2D.Double firstDirectionVector, secondDirectionVector;        
        CurvatureFunction curvature = new CurvatureFunction();   
        
        int windowSize = segmentSize!=null ? segmentSize : (int)(segmentSizeRatio*contour.size());
        if (windowSize > contour.size())
            windowSize = contour.size();

        for (int i = 0; i < contour.size(); i++) {
            ArrayList leftSegment = contour.getSegment(contour.get((i+offset)%contour.size()), windowSize, true); 
            ArrayList rightSegment = contour.getSegment(contour.get((i-offset+contour.size())%contour.size()), windowSize, false);          
            secondDirectionVector = JFIMath.getDirectionVector(leftSegment);
            firstDirectionVector = JFIMath.getDirectionVector(rightSegment);             
            arc_cos = ( (firstDirectionVector.y < 0) ? -Math.acos((double)firstDirectionVector.x) : Math.acos((double)firstDirectionVector.x));
            arc_cos2 =( (secondDirectionVector.y < 0) ? -Math.acos((double)secondDirectionVector.x) : Math.acos((double)secondDirectionVector.x));
            currentCurvature = arc_cos - arc_cos2;
            if (arc_cos2 > arc_cos)
                currentCurvature += (float) (Math.PI);
            else
                currentCurvature -= (float) Math.PI;          
            curvature.add(-currentCurvature);  //Positive means convex curve
        }     
        return curvature;
    }
    
    
    /**
     * Pruebas con Antonio...
     * 
     * @param contour
     * @return 
     */
    private CurvatureFunction curvatureLineBasedImproved(Contour contour){
        if(contour.isEmpty()){
            return null;
        }
        double currentCurvature;
        double arc_cos, arc_cos2;
        Point2D.Double firstDirectionVector, secondDirectionVector;        
        CurvatureFunction curvature = new CurvatureFunction();   
        
        int windowSize = segmentSize!=null ? segmentSize : (int)(segmentSizeRatio*contour.size());
        if (windowSize > contour.size())
            windowSize = contour.size();
                
        ArrayList<Point2D.Double> vectoresDirectores = new ArrayList();
        for (int i = 0; i < contour.size(); i++) {
            ArrayList segment = contour.getSegment(contour.get(i), windowSize, true);          
            firstDirectionVector = JFIMath.getDirectionVector(segment);  
            vectoresDirectores.add(firstDirectionVector);
        }               
        for (int i = 1; i < contour.size(); i++){
            firstDirectionVector = vectoresDirectores.get(i);                    
            secondDirectionVector = vectoresDirectores.get(i-1);           
            double v = (firstDirectionVector.x * secondDirectionVector.x) + (firstDirectionVector.y * secondDirectionVector.y);         
            if(v<-0.5) {
                firstDirectionVector.x *= -1.0;
                firstDirectionVector.y *= -1.0;
            }           
        } 
        Point2D.Double paux;
        for (int i = 0; i < contour.size(); i++) {
            firstDirectionVector = vectoresDirectores.get(  (i-windowSize+contour.size())%contour.size()  );  
            paux = new Point2D.Double(firstDirectionVector.x,firstDirectionVector.y);
            secondDirectionVector = vectoresDirectores.get(i);            
            arc_cos = ( (paux.y < 0) ? -Math.acos( (double) paux.x) :
                               Math.acos( (double) paux.x));
            arc_cos2 =( (secondDirectionVector.y < 0) ? -Math.acos( (double) secondDirectionVector.x) :
                                Math.acos( (double) secondDirectionVector.x));            
            currentCurvature = arc_cos - arc_cos2;
            if(currentCurvature > Math.PI) currentCurvature -= 2.0f*(float)Math.PI;
            if(currentCurvature < -Math.PI) currentCurvature += 2.0f*(float)Math.PI;
            curvature.add(-currentCurvature);
        }     
        
        return curvature;
    }
    
    
    /**
     * Pruebas usando código de Soto, que a su vez viene de un código que yo
     * tenía en C (el que usé para los cromosomas), que a su vez creo que
     * viene de un código que en su día me pasó Antonio.
     * 
     * Está todo muy caótico, es solo para testeo
     * 
     * @param contour
     * @return 
     */
    private CurvatureFunction curvatureLineBasedSoto(Contour contour){
        if(contour.isEmpty()){
            return null; 
        }
        
        int windowSize = segmentSize!=null ? segmentSize : (int)(segmentSizeRatio*contour.size());
        if (windowSize > contour.size()){
            windowSize = contour.size();
        }               

        CurvatureFunction curvature = new CurvatureFunction();          
        Point[] puntos = new Point[contour.size()];
        int i=0;
        for(Point2D p : contour){
            puntos[i]= new Point((int)p.getY(),(int)p.getX());  //Cambio de orden!!
            i++;        
        }        
        float[] fcurvature;
        _toEraseTest test = new _toEraseTest();        
        fcurvature = test.curvatura(puntos,contour.size(),windowSize,offset);
        
        //Salida
        for(i=0;i<fcurvature.length;i++){
            curvature.add((double)fcurvature[i]);
        }
            
        return curvature;
    }
    
    
    
}
