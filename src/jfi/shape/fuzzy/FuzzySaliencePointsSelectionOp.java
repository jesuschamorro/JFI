package jfi.shape.fuzzy;

import java.awt.geom.Point2D;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Map;
import jfi.fuzzy.operator.TNorm;
import jfi.shape.Contour;

/**
 * Class implementing the contour salience points selection operator. This
 * operator is based on the saliency, a fuzzy property of the contour. Saliency
 * is measured for each contour point on the basis of its curvacity and its
 * maximalty (two fuzzy properties): A point is a salience point if (1) it has
 * curvacity enough and (2) its curvacity value is higher than almoost all the
 * values around it. Given the fuzzy saliency of a contour, a crisp set of
 * representative (salience) points are calculated as the centerpoints of the
 * segments generated in a given alpha-cut.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzySaliencePointsSelectionOp {
    /**
     * Size of the window around the countour point used to check the local
     * curvacity.
     */
    private int window_size_curvacity;
    /**
     * Coefficient of determination of the arch associated to zero membeship
     * degree to linearity. It is used to calculate the curvacity.
     */
    private double alpha_curvacity;
    /**
     * Size of the window around the countour point used to check the local
     * maximality
     */
    private int window_size_maxima;
    /**
     * Parameter of the 'almost all' quantifier used to calculate the
     * maximality. It represents the number of points (greater than the one
     * studied) above which the point is not considered a maximum. The
     * quantifier is modelled with a triangular membership function with
     * parameter (0,0,alpha).
     */
    private double alpha_quantifier_almostall;
    /**
     * Parameter alpha of the 'enough' quantifier used to analyze if a countour
     * point has curvacity enough. Any value lower than alpha is considered
     * non-enough (i.e., the membebmeship degree to 'enough' is zero).
     */
    private double alpha_quantifier_enough;
    /**
     * Parameter alpha of the 'enough' quantifier used to analyze if a countour
     * point has curvacity enough. Any value higher than beta is considered
     * enough (i.e., the membebmeship degree to 'enough' is one). Any value
     * between alfa and beta will have membebmeship degree to 'enough' between 0
     * and 1 (the quantifier is modelled with a trapezoidal function with
     * parameter (alpha,beta,1,1)).
     */
    private double beta_quantifier_enough;
    /**
     * T-norm used to calculate the saliency. It is used to aggregate the
     * 'curvacity enough' and the 'curvacity higher than almost all' membership
     * degrees
     */
    private TNorm tnorm;
    /**
     * If <tt>true</tt>, the parameters of 'window_size_curvacity' and
     * 'window_size_maxima' are calculated automatically on the basis of the
     * contour sie.
     */
    private boolean auto = false;
    /**
     * Default angle used to calculate de curvacity. It is the angle of the arch
     * associated to zero membeship degree to linearity
     */
    private static double DEFAULT_ALPHA_ANGLE = 180;
    /**
     * Default parameter alpha of the 'almost' quantifier.
     */
    public static double DEFAULT_ALPHA_QUANTIFIER_ALMOST = 4.0;
    /**
     * Default parameter alpha of the 'enough' quantifier.
     */
    public static double DEFAULT_ALPHA_QUANTIFIER_ENOUGH = 0.2;
    /**
     * Default parameter beta of the 'enough' quantifier.
     */
    public static double DEFAULT_BETA_QUANTIFIER_ENOUGH = 0.5;
    /**
     * Default alpha used to calculate the alpha-cut over the fuzzy saliency.
     */
    public static double DEFAULT_ALPHACUT = 0.4;
    /**
     * Default t-norm used to calculate the salience.
     */
    public static TNorm DEFAULT_TNORM = TNorm.PRODUCT;
    

    /**
     * Constructs a new salience points selection operator. This operator is
     * based on the saliency, a contour fuzzy property (see getSaliencyInstance
     * method in {@link jfi.shape.fuzzy.FuzzyContourFactory}). The parameter of
     * this operator are raleted to the fuzzy saliency parameters: the saliency
     * is measured for each contour point on the basis of its curvacity and its
     * maximalty (two fuzzy properties), with a point being a salience point if
     * (1) it has curvacity enough and (2) its curvacity value is higher than
     * almoost all the values around it.
     *
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
     * @param tnorm
     */
    public FuzzySaliencePointsSelectionOp(int window_size_curvacity, double alpha_curvacity, int window_size_maxima, double alpha_quantifier_almostall, double alpha_quantifier_enough, double beta_quantifier_enough, TNorm tnorm) {
        this.window_size_curvacity = window_size_curvacity;
        this.alpha_curvacity = alpha_curvacity;
        this.window_size_maxima = window_size_maxima;
        this.alpha_quantifier_almostall = alpha_quantifier_almostall;
        this.alpha_quantifier_enough = alpha_quantifier_enough;
        this.beta_quantifier_enough = beta_quantifier_enough;
        this.tnorm = tnorm;
    }

    
    
    /**
     * Constructs a new salience points selection operator operator using the
     * default parameters.
     */
    public FuzzySaliencePointsSelectionOp() {
        this(0, getAlpha(DEFAULT_ALPHA_ANGLE), 0, DEFAULT_ALPHA_QUANTIFIER_ALMOST, DEFAULT_ALPHA_QUANTIFIER_ENOUGH, DEFAULT_BETA_QUANTIFIER_ENOUGH, DEFAULT_TNORM);
        this.auto = true;
        //The 'window_size_curvacity' and 'window_size_maxima' parameters will
        //be calculated automatically in the apply method (they depend on the 
        //contour size). 
    }

    /**
     * Calculates the set of salience points associated to the given contour.
     *
     * @param contour contour to be analyzed.
     * @param alpha the alpha associated to the alpha-cut of the saliency (fuzzy
     * property of a contour) that will be used to select the salience points.
     * Each (crisp) segment of the alpha-cut will have associated a salience
     * point (specifically, the centerpoint of the segment).
     * @return the set of salience points associated to the given contour. The
     * salience poits are returned as a <code>FuzzyContour</code>, where each
     * point has a membership degree to the saliency property.
     */
    public FuzzyContour apply(Contour contour, double alpha) {
        if(contour==null){
            throw new InvalidParameterException("Null contour.");
        }
        FuzzyContour salience_points = new FuzzyContour("Contour.Salience Point");
        if(contour.isEmpty()){
            return salience_points;
        }
        if (auto) {
            this.window_size_curvacity = (int) (Contour.DEFAULT_WINDOW_RATIO_SIZE * contour.size());
            this.window_size_maxima = window_size_curvacity / 2;
        }
        //The fuzzy saliency is calculated (contour property)
        FuzzyContour saliency = FuzzyContourFactory.getSaliencyInstance(contour, window_size_curvacity, alpha_curvacity, window_size_maxima, alpha_quantifier_almostall, alpha_quantifier_enough, beta_quantifier_enough,tnorm);
        ArrayList<Map.Entry> points = new ArrayList(saliency.entrySet());

        //First, the start point is set. In general, it is the first of the point 
        //list, but it is necessary to consider the particular case when the fisrt  
        //point in the list (the one associated to the index 0) is inside the 
        //alpha-cut together with the last one 
        int i_start = 0;
        boolean cycle = false;
        while (!cycle && (Double) points.get(i_start).getValue() >= alpha) {
            i_start = (i_start - 1 + points.size()) % points.size();
            if(i_start==0) cycle=true; // => All points are in the alpha-cut
        }
        
        //The list of the contour points is analyzed looking for 'alpha-cut segments'
        //For each segment, the centerpoint is added to the salience point list.
        double i_degree;
        int first, index_salience_point,length, nexti, i = i_start;
        boolean done = false; //At least, there is a point
        while (!done) {
            i_degree = (Double) points.get(i).getValue();
            if (i_degree >= alpha) {
                //The right endpoint of the alphacut segment is calculated
                length = 1;
                first = i;
                nexti = (i + 1) % points.size();
                while (nexti != i_start && (Double) points.get(nexti).getValue() >= alpha) {
                    length++;
                    i = (i + 1) % points.size();
                    nexti = (i + 1) % points.size();
                }
                //The salience point is calculated as the centerpoint of the segment               
                index_salience_point = (first + length / 2) % points.size();
                salience_points.add((Point2D) points.get(index_salience_point).getKey(), i_degree);
            }
            i = (i + 1) % points.size();
            if (i == i_start) {
                done = true;
            }
        }
        return salience_points;
    }
    
    /**
     * Applies this operator using by default the alpha-cut
     * {@link #DEFAULT_ALPHACUT}
     *
     * @param contour contour to be analyzed
     * @return the set of salience points associated to the given contour. The
     * salience poits are returned as a <code>FuzzyContour</code>, where each
     * point has a membership degree to the saliency property.
     */
    public FuzzyContour apply(Contour contour) {
        return this.apply(contour, DEFAULT_ALPHACUT);
    }

    /**
     * Calculates, given the angle of the non-fullfilment arch, the alpha value
     * needed to estimate the curvacity.
     *
     * @param angle the angle (in degrees) of the non-fullfilment arch.
     * @return the alpha value associated to the given angle.
     */
    private static double getAlpha(double angle) {
        return 1.0 - (0.37 * Math.toRadians(angle) / Math.PI);
    }

    /*
     *  Alternative implementation of the 'apply' method. It is not used.
     */
    private FuzzyContour _apply(Contour contour, double alpha) {
         if(contour==null){
            throw new InvalidParameterException("Null contour.");
        }
        FuzzyContour salience_points = new FuzzyContour("Contour.Salience Point");
        if(contour.isEmpty()){
            return salience_points;
        }
        double i_degree;
        int first, last, index_salience_point;
        if (auto) {
            this.window_size_curvacity = (int) (Contour.DEFAULT_WINDOW_RATIO_SIZE * contour.size());
            this.window_size_maxima = window_size_curvacity / 2;
        }
        //The saliency is calculated (the salience poits are calculated on the basis of this property)
        FuzzyContour saliency = FuzzyContourFactory.getSaliencyInstance(contour, window_size_curvacity, alpha_curvacity, window_size_maxima, alpha_quantifier_almostall, alpha_quantifier_enough, beta_quantifier_enough,tnorm);
        ArrayList<Map.Entry> points = new ArrayList(saliency.entrySet());
        int start = 0, end = points.size();
        
        //Particular case: if the fisrt point in the list of contour points is
        //inside the alpha-cut and also the last one; in that case, the first 
        //segment of the alpha-cut will include points from the begining and the 
        //end of the list.        
        if( end>1 && (Double)points.get(start).getValue() > alpha && 
                     (Double)points.get(end-1).getValue() > alpha){            
            first = end - 1;
            while (first - 1 >= 0 && (Double) points.get(first - 1).getValue() >= alpha) {
                first--; //The left endpoint of the alphacut segment is calculated
            }
            last = 0;
            while (last + 1 < end && (Double) points.get(last + 1).getValue() >= alpha) {
                last++;  //The right endpoint of the alphacut segment is calculated
            }
            int length = ((end-first)+last+1)/2;
            index_salience_point = (first + length + contour.size()) % contour.size();
            salience_points.add((Point2D)points.get(index_salience_point).getKey(), 
                                (Double)points.get(index_salience_point).getValue());
            start = last+1;
            end = first;
        }
        
        //General case: the list of the contour points is crossed from the 
        //begining to the end looking for 'alpha-cut segments'; for each segment,
        //the centerpoint is added to the final salience point list.
        //In general, the start point is the first point of the contour point 
        //list (i.e., start=0 and end=points.size()) except if the previous 
        //particular case is applicable
        for (int i = start; i < end; i++) {
            i_degree = (Double) points.get(i).getValue();
            if (i_degree > alpha) {
                //The right endpoint of the alphacut segment is calculated
                first = last = i;
                while (last + 1 < points.size() && (Double) points.get(last + 1).getValue() >= alpha) {
                    last++;
                }
                //At this point, 'last' is the index of the last point of the
                //alphacut segment. The position of the salience point is 
                //calculated as the centerpoint of the segment.               
                index_salience_point = first + (last - first) / 2;
                salience_points.add((Point2D) points.get(index_salience_point).getKey(), i_degree);
                //The index 'i' is moved to the end of the alphacut segment 
                i = last;
            }
        }
        return salience_points;
    }

}
