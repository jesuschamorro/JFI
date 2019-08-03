package jfi.shape.fuzzy;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Map;
import jfi.shape.Contour;

/**
 * Class implementing the segmentation of a contour.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class SaliencePointsSelectionOp {
    
    private int window_size_curvacity;
    private double alpha_curvacity;    
    private int window_size_maxima;
    private double alpha_quantifier_almostall;
    private double alpha_quantifier_enough; 
    private double beta_quantifier_enough;
    
    private boolean auto = false;
    
    private static double DEFAULT_ALPHA_ANGLE = 180;
    private static double DEFAULT_ALPHA_QUANTIFIER_ALMOST = 4.0;
    private static double DEFAULT_ALPHA_QUANTIFIER_ENOUGH = 0.2;
    private static double DEFAULT_BETA_QUANTIFIER_ENOUGH = 0.5;
    
    private static double DEFAULT_ALPHACUT = 0.4;
    
    
    public SaliencePointsSelectionOp(int window_size_curvacity, double alpha_curvacity, int window_size_maxima, double alpha_quantifier_almostall, double alpha_quantifier_enough, double beta_quantifier_enough) {       
        this.window_size_curvacity = window_size_curvacity;
        this.alpha_curvacity = alpha_curvacity;
        this.window_size_maxima = window_size_maxima;
        this.alpha_quantifier_almostall = alpha_quantifier_almostall;
        this.alpha_quantifier_enough = alpha_quantifier_enough;
        this.beta_quantifier_enough = beta_quantifier_enough;
    }
    
    public SaliencePointsSelectionOp(){
        this(0, getAlpha(DEFAULT_ALPHA_ANGLE),0, DEFAULT_ALPHA_QUANTIFIER_ALMOST, DEFAULT_ALPHA_QUANTIFIER_ENOUGH, DEFAULT_BETA_QUANTIFIER_ENOUGH);
        auto = true;
        //The 'window_size_curvacity' and 'window_size_maxima' parameters will
        //be calculated automatically. 
    }
    
    public FuzzyContour apply(Contour contour, double alpha) {
        FuzzyContour salience_points = new FuzzyContour("Contour.Salience Point");
        if (auto) {
            this.window_size_curvacity = (int) (Contour.DEFAULT_WINDOW_RATIO_SIZE * contour.size());
            this.window_size_maxima = window_size_curvacity / 2;
        }
        FuzzyContour saliency = FuzzyContourFactory.getSaliencyInstance(contour, window_size_curvacity, alpha_curvacity, window_size_maxima, alpha_quantifier_almostall, alpha_quantifier_enough, beta_quantifier_enough);        

        double i_degree;
        int first,last,index_salience_point;
        ArrayList<Map.Entry> points = new ArrayList(saliency.entrySet());
        for (int i = 0; i < points.size(); i++) {
            i_degree = (Double) points.get(i).getValue();
            if (i_degree > alpha) { 
                
                if(i==0){
                    System.err.println("First point could be repeated");
                }
                
                //The endpoits of the alphacut segment are calculated
                first = last = i;
                while(last+1<points.size() && (Double)points.get(last+1).getValue()>alpha){
                    last++;
                }
                //At this point, 'last' is the index of the last point of the
                //alphacut segment. The position of the salience point is 
                //calculated as the middle point of the segment.               
                index_salience_point = first + (last-first)/2;
                salience_points.add((Point2D) points.get(index_salience_point).getKey(), i_degree);
                //The index 'i' is moved to the end of the alphacut segment 
                i=last; 
            }
        }    
        
        return salience_points;

    }
    
    public FuzzyContour apply(Contour contour) {
        return this.apply(contour,DEFAULT_ALPHACUT);
    }
    
    private static double getAlpha(double angle){
        return  1.0-(0.37*Math.toRadians(angle)/Math.PI);        
    }
    
}
