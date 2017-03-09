package jfi.utils;

import java.awt.geom.Point2D;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Some additional math utilities.
 * 
 * @author Luis Suárez Lloréns
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class JFIMath {
    /**
     * Epsilon constant
     */
    public static final double EPSILON = Double.MIN_VALUE; 
    /**
     * Default precision (number of decimals) in rounding operations
     */
    public final static int DEFAULT_ROUNDING_DECIMALS = 2;
    /**
     * Default 10 power value associated to the default precision in rounding 
     * operations {@see #DEFAULT_ROUNDED_DECIMALS}
     */
    private final static double DEFAULT_ROUNDING_10POWER = Math.pow(10,DEFAULT_ROUNDING_DECIMALS);
    /**
     * Type of line estimation. The line is estimated as the line that links the
     * first and last point of the given set.
     */
    public static final int START_TO_END_LINE = 0;
    /**
     * Type of line estimation. The line is estimated bay means a regression
     * procedure over the given set of points.
     */
    public static final int REGRESSION_LINE = 1;
    /**
     * Default type of line estimation. 
     */
    public static int DEFAULT_COEFFICIENT_DETERMINATION_MODE = REGRESSION_LINE;
    
    /**
     * Returns the coefficient of determination, or R-squared, in a ordinary 
     * least squares (OLS) regression that is often used as a goodness-of-fit 
     * measure
     * 
     * @param pointSet the collection of points 
     * @return the coefficient of determination
     */
    public static double CoefficientDetermination(ArrayList<Point2D> pointSet){
        return CoefficientDetermination(pointSet,DEFAULT_COEFFICIENT_DETERMINATION_MODE);
    }
    
    /**
     * Returns the coefficient of determination, or R-squared, often used as 
     * a goodness-of-fit measure
     * 
     * @param pointSet the collection of points 
     * @param mode type of estimated line
     * @return the coefficient of determination
     */
    public static double CoefficientDetermination(ArrayList<Point2D> pointSet, int mode){
        JFILine line;
        switch (mode) {
            case REGRESSION_LINE:
                line = JFIMath.linearRegression(pointSet);
                break;
            case START_TO_END_LINE:
                line = JFIMath.startToEndLine(pointSet);
                break;
            default:
                throw new InvalidParameterException("Invalid value of mode.");
        }
        
        Point2D.Double projectedPoint;       
        Point2D.Float mean = new Point2D.Float(0.0f,0.0f);
        for (Point2D point:pointSet) {
            mean.x += point.getX(); 
            mean.y += point.getY();
        }
        mean.x /= pointSet.size();
        mean.y /= pointSet.size();
        
        double residuo = 0.0;
        double total = 0.0;
        for (Point2D point: pointSet) {
            projectedPoint = JFIMath.projection(point,line);
            residuo += JFIMath.distance(point,projectedPoint);
            total += JFIMath.distance(point,mean);
        }
        
        return Math.max(1.0-(residuo/total),0.0);
    }
    
    /**
     * Calculates the direction of a segment
     * 
     * @param pointSet the set of points
     * @return the direction vector of the segment
     */
    public static Point2D.Double getDirectionVector(Collection<Point2D> pointSet){

        double mod;
        JFILine line;
        Point2D.Double projectedPoint;
        Point2D.Double projectedInitialPoint;
        Point2D.Double meanPoint;
        Point2D.Double directionVector;
        
        line = JFIMath.linearRegression(pointSet);            
        projectedInitialPoint = JFIMath.projection(pointSet.iterator().next(),line);
        meanPoint = new Point2D.Double(0.0,0.0);
        for (Point2D point: pointSet) {
            projectedPoint = JFIMath.projection(point,line);
            meanPoint.x += projectedPoint.x;
            meanPoint.y += projectedPoint.y;
        }
        meanPoint.x = meanPoint.x / pointSet.size();
        meanPoint.y = meanPoint.y / pointSet.size();

        directionVector = new Point2D.Double(0.0,0.0);
        directionVector.x = meanPoint.x - projectedInitialPoint.x; 
        directionVector.y = meanPoint.y - projectedInitialPoint.y;
        mod = Math.sqrt((double) (directionVector.x * directionVector.x 
                                  + directionVector.y * directionVector.y));
        directionVector.x /= mod;
        directionVector.y /= mod;

        return directionVector;
    }
    
    /**
     * Returns the line obtained by means of a linear regression over a given
     * set of points
     * 
     * @param pointSet set of points
     * @return the regression line
     */
    public static JFILine linearRegression(Collection<Point2D> pointSet){
        double meanX, meanY, Sxy, Sxx, Syy;
        double aux, aux1;
        int j;
        double trS2, det;
        double lambda, delta;
        double a,b,c;

        meanX = meanY = (float) 0.0;
        for (Point2D point: pointSet) {
            meanX += point.getX();
            meanY += point.getY();
        }
        meanX /= pointSet.size();
        meanY /= pointSet.size();
        Sxx = Syy = Sxy = (float) 0.0;
        
        //2nd central moments
        for (Point2D point: pointSet) {
          aux = point.getX() - meanX;
          aux1 = point.getY() - meanY;
          Sxy += aux * aux1;
          Sxx += aux * aux;
          Syy += aux1 * aux1;
        }
        
        trS2 = (Sxx + Syy) / 2; 
        det = Sxx * Syy - Sxy * Sxy; 
        lambda = (trS2 * trS2) - det;
        lambda = trS2 - (float) Math.sqrt( (double) lambda);
        delta = (Sxy * Sxy + (lambda - Syy) * (lambda - Syy));
        delta = (float) Math.sqrt( (double) delta);
        
        if (delta > 0.00001) {
            a = (lambda - Syy) / delta;
            b = Sxy / delta;
        }
        else
            if (Syy < Sxx) { /*horizontal line */
              a = (float) 0.0;
              b = (float) 1.0;
            }
            else {           /*vertical line*/
              a = (float) 1.0;
              b = (float) 0.0;
            }       
        c = - ( (a) * meanX + (b) * meanY);
        if (c < 0) {
          a = -a;
          b = -b;
          c = -c;
        }      
        
        JFILine regressionLine = new JFILine(a,b,c);
        return regressionLine;
    }
    
    /**
     * Returns the line that passes through the start and the end points 
     * of the pointSet
     * 
     * @param pointSet set of points
     * @return the line
     */
    public static JFILine startToEndLine(ArrayList<Point2D> pointSet){
        float a,b,c;
        
        Point2D firstPoint = pointSet.get(0);
        Point2D lastPoint = pointSet.get(pointSet.size()-1);
        
        if (firstPoint.getX() == lastPoint.getX()){ //Vertical line
            a = (float) 1.0;
            b = (float) 0.0;
            c = (float) -firstPoint.getX();
        }
        else if (firstPoint.getY() == lastPoint.getY()){//Horizontal line
            
            a = (float) 0.0;
            b = (float) 1.0;
            c = (float) -firstPoint.getY();
        }
        else{
            a = (float) 1.0;
            b = (float)((float) (lastPoint.getX()-firstPoint.getX())/(firstPoint.getY()-lastPoint.getY()));
            c = (float)((float) -a*firstPoint.getX()-b*firstPoint.getY());
        }
        if (c < 0) {
          a = -a;
          b = -b;
          c = -c;
        }  
        JFILine regressionLine = new JFILine(a,b,c);
        return regressionLine;
    }
    
    /**
     * Returns the projection of a point on a line
     * 
     * @param point a point
     * @param line  a line
     * 
     * @return the projection of point on the line
     */
    public static Point2D.Double projection(Point2D point, JFILine line){
        double x = ((line.getB() * line.getB() * point.getX() - line.getA() * line.getB() * point.getY() - line.getA() * line.getC()) / (line.getB() * line.getB() + line.getA() * line.getA()));
        double y = (- (line.getB() * line.getC() + line.getA() * line.getB() * point.getX() - line.getA() * line.getA() * point.getY()) / (line.getB() * line.getB() + line.getA() * line.getA()));

        return new Point2D.Double(x,y);
    }
    
    /**
     * Returns the Euclidean distance between points a and b
     * 
     * @param a a point
     * @param b a point
     * 
     * @return the distance between a and b 
     */
    public static double distance(Point2D a, Point2D b){
        return Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY()));
    }    
    
    /**
     * Returns the minimun of the given values
     * @param a value
     * @param b vale
     * @param c vale
     * @return the minimum
     */
    public static double min(double a, double b, double c){
        return Math.min(Math.min(a,b),c);
    }
    
    /**
     * Returns the maximum of the given values
     * @param a value
     * @param b vale
     * @param c vale
     * @return the maximum
     */
    public static double max(double a, double b, double c){
        return Math.max(Math.max(a,b),c);
    }
    
    /**
     * Returns a rounded version of a given number (using the default precision)
     * 
     * @param number the number to be rounded
     * @return the rounded number
     */
    public static Double round(Double number){
        Double round = Math.round(number*DEFAULT_ROUNDING_10POWER)/DEFAULT_ROUNDING_10POWER;      
        return round;
    }
    
    /**
     * Returns a rounded version of a given number.
     * 
     * @param number the number to be rounded
     * @param number_decimals the number of decimals in the rounded number
     * @return the rounded number
     */
    public static Double round(Double number, int number_decimals){
        Double power = Math.pow(10,number_decimals);
        Double round = Math.round(number*power)/power;      
        return round;
    }
}
