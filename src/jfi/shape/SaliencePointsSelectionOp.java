package jfi.shape;

import jfi.shape.fuzzy.*;
import java.awt.geom.Point2D;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class implementing the contour salience points selection operator. 
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class SaliencePointsSelectionOp {
    /**
     * Size of the window around the countour point used to check the local
     * curvature.
     */
    private int window_size_curvature;
    /**
     * Size of the window around the countour point used to check the local
     * maxima.
     */
    private int window_size_maxima;
    /**
     * If <tt>true</tt>, the parameters of 'window_size_curvacity' and
     * 'window_size_maxima' are calculated automatically on the basis of the
     * contour sie.
     */
    private boolean auto = false;
    

    /**
     * Constructs a new salience points selection operator. .
     *
     * @param window_size_maxima size of the window around the countour point
     * used to check the local maxima.
     */
    public SaliencePointsSelectionOp(int window_size_maxima) {
        this.window_size_maxima = window_size_maxima;
    }

    /**
     * Constructs a new salience points selection operator operator using the
     * default parameters.
     */
    public SaliencePointsSelectionOp() {
        this(3);
        this.auto = true;
        //The 'window_size_maxima' parameter will be calculated automatically in 
        //the apply method (it depends on the contour size). 
    }

    /**
     * Calculates the set of salience points associated to the given contour.
     *
     * @param contour contour to be analyzed.
     * @return the set of salience points associated to the given contour. The
     * salience poits are returned as a <code>FuzzyContour</code>, where each
     * point has a membership degree to the saliency property.
     */
    public FuzzyContour apply(Contour contour) {
        if(contour==null){
            throw new InvalidParameterException("Null contour.");
        }
        FuzzyContour salience_points = new FuzzyContour("Contour.Salience Point");
        if(contour.isEmpty()){
            return salience_points;
        }
        if (auto) {
            this.window_size_curvature = (int) (Contour.DEFAULT_WINDOW_RATIO_SIZE * contour.size());
            this.window_size_maxima = window_size_curvature / 2;
        }
                
        CurvatureFunction curvature = contour.curvature();
        ArrayList<Integer> maxima = curvature.localMaxima(window_size_maxima, true);
        
        for(Integer i: maxima){            
            salience_points.add(contour.get(i), 1.0);        
        }
        return salience_points;
    }
    

}
