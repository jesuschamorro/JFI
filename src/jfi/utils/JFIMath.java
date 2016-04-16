package jfi.utils;

import java.awt.geom.Point2D;
import java.util.Collection;

/**
 *
 * @author Luis Suárez Lloréns
 */
public class JFIMath {
    
    /**
     * Epsilon constant
     */
    public static final double EPSILON = Double.MIN_VALUE; 
    
    /**
     * Returns the coefficient of determination, or R-squared, in a ordinary 
     * least squares (OLS) regression that is often used as a goodness-of-fit 
     * measure
     * 
     * @param pointSet the collection of points 
     * @return the coefficient of determination
     */
    public static double getCoefficientDetermination(Collection<Point2D> pointSet){

        JFILine line;
        line = JFIMath.linearRegression(pointSet);
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
        
        return 1-(residuo/total);
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
     * @return the refression line
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
        return Math.max(Math.min(a,b),c);
    }
}
