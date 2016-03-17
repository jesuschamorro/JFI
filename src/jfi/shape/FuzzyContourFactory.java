/*
    Factory for creating FuzzyContour

    @author Luis Suárez Lloréns
 */
package jfi.shape;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class FuzzyContourFactory {
    
    public static final int TYPE_CURVATURE = 1;
    public static final int TYPE_LINEARITY = 2;

    /**
     *
     */
    public static final int TYPE_VERTICITY = 3;
    
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
        case TYPE_CURVATURE:
            return getInstanceCurvature(contour);
        case TYPE_LINEARITY:
            return getInstanceLinearity(contour);
        case TYPE_VERTICITY:
            return getInstanceVerticity(contour);
        default:
            return null;
        }
    }
    
    /**
     * Create a new FuzzyContour using curvature as truth value
     * 
     * @param contour Contour used to create the new FuzzyContour
     * 
     * @return A new instance of FuzzyContour
     */
    private static FuzzyContour getInstanceCurvature(Contour contour){         
        
        FuzzyContour fuzzyContour = new FuzzyContour();
        FuzzyPoint fp;
        
        ArrayList<Float> curvature = getCurvature(contour);        
        
        for(int i = 0; i < contour.size(); i++){
            fp = new FuzzyPoint(contour.get(i), curvature.get(i));
            fuzzyContour.add(fp);
        }
        
        return fuzzyContour;
    }
    
    /**
     * Create a new FuzzyContour using linearity as truth value
     * 
     * @param contour Contour used to create the new FuzzyContour
     * 
     * @return A new instance of FuzzyContour
     */
    private static FuzzyContour getInstanceLinearity(Contour contour){
        FuzzyContour fuzzyContour = new FuzzyContour();
        FuzzyPoint fp;
        
        ArrayList<Float> linearity = getLinearity(contour);       
        
        for(int i = 0; i < contour.size(); i++){
            fp = new FuzzyPoint(contour.get(i), linearity.get(i));
            fuzzyContour.add(fp);
        }
        
        return fuzzyContour;
    }
    
    /**
     * Create a new FuzzyContour using verticity as truth value
     * 
     * @param contour Contour used to create the new FuzzyContour
     * 
     * @return A new instance of FuzzyContour
     */
    private static FuzzyContour getInstanceVerticity(Contour contour){
        FuzzyContour fuzzyContour = new FuzzyContour();
        FuzzyPoint fp;
        
        ArrayList<Float> verticity = getVerticity(contour);
        
        for(int i = 0; i < contour.size(); i++){
            fp = new FuzzyPoint(contour.get(i), verticity.get(i));
            fuzzyContour.add(fp);
        }
        
        return fuzzyContour;
    }

    /**
     * Compute the curvature of contour
     * 
     * @param contour Contour used
     * 
     * @return Contour's curvature
     */
    private static ArrayList<Float> getCurvature(Contour contour) {
        ArrayList<Float> curvature = new ArrayList<>();
        
        double currentCurvature;
        double arc_cos, arc_cos2;
        Point2D.Double firstDirectionVector, secondDirectionVector;
        
        int numPoints = contour.size();
        int windowSize = numPoints/15;
        int offset = 0;
        
        if (windowSize > numPoints)
            windowSize = numPoints;

        for (int i = 0; i < numPoints; i++) {

            firstDirectionVector = getDirectionVector(contour, windowSize,numPoints + i - (windowSize + offset)%numPoints, i);
            secondDirectionVector = getDirectionVector(contour, windowSize,i+offset % numPoints,i);

            arc_cos = ( (firstDirectionVector.y < 0) ? -Math.acos( (double) firstDirectionVector.x) :
                               Math.acos( (double) firstDirectionVector.x));
            arc_cos2 =( (secondDirectionVector.y < 0) ? -Math.acos( (double) secondDirectionVector.x) :
                                Math.acos( (double) secondDirectionVector.x));
            currentCurvature = arc_cos - arc_cos2;

            if (arc_cos2 > arc_cos)
                currentCurvature+= (float) (Math.PI);
            else
                currentCurvature-= (float) Math.PI;
            
            curvature.add((float) currentCurvature);
        }
        
        return curvature;
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
            linearity.add((float) getSegmentLinearity(contour, 2*windowSize,(i-windowSize+1 + numPoints) % numPoints,k));
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
            linearity.add((float) getSegmentLinearity(contour, windowSize, (i+offset+numPoints) % numPoints, 3));
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

    private static Point2D.Double getDirectionVector(Contour contour, int win, int start, int i){
        int numPoints = contour.size();
        float mod;
        double[] line;
        Point2D.Double projectedPoint;
        Point2D.Double projectedInitialPoint;
        Point2D.Double meanPoint;
        Point2D.Double directionVector;
        
        line = linearRegresion(contour, start, win);
            
        projectedInitialPoint = Projection(contour.get(i),line);

        meanPoint = new Point2D.Double(0.0,0.0);
        for (int k = start; k < start + win; k++) {
            projectedPoint = Projection(contour.get(k % numPoints),line);
            meanPoint.x += projectedPoint.x;
            meanPoint.y += projectedPoint.y;
        }
        meanPoint.x = meanPoint.x / win;
        meanPoint.y = meanPoint.y / win;

        directionVector = new Point2D.Double(0.0,0.0);
        directionVector.x = meanPoint.x - projectedInitialPoint.x; 
        directionVector.y = meanPoint.y - projectedInitialPoint.y;
        mod = (float) Math.sqrt((double) (directionVector.x * directionVector.x 
                                          + directionVector.y * directionVector.y));
        directionVector.x /= mod;
        directionVector.y /= mod;

        return directionVector;
    }
    
    private static double[] linearRegresion(Contour contour, int start, int windowSize){
        double meanX, meanY, Sxy, Sxx, Syy;
        double aux, aux1;
        int j;
        double trS2, det;
        double lambda, delta;
        int tamElp = contour.size();
        double a,b,c;

        meanX = meanY = (float) 0.0;
        for (j = start; j < start + windowSize; j++) {
            meanX += contour.get(j % tamElp).getX(); //Calculamos la media
            meanY += contour.get(j % tamElp).getY();
        }
        meanX /= windowSize;
        meanY /= windowSize;

        Sxx = Syy = Sxy = (float) 0.0;
        
        //2nd central moments
        for (j = start; j < start + windowSize; j++) {
          aux = contour.get(j % tamElp).getX() - meanX;
          aux1 = contour.get(j % tamElp).getY() - meanY;
          Sxy += aux * aux1;
          Sxx += aux * aux;
          Syy += aux1 * aux1;
        }
        
        //Regresión de Deming??
        trS2 = (Sxx + Syy) / 2; 
        det = Sxx * Syy - Sxy * Sxy; 
        lambda = (trS2 * trS2) - det;
        lambda = trS2 - (float) Math.sqrt( (double) lambda);
        delta = (Sxy * Sxy + (lambda - Syy) * (lambda - Syy));
        delta = (float) Math.sqrt( (double) delta);
        
        if (delta > 0.00001) {
          /* --- if(delta!=0) ---- Si Sxy==0 && (Sxx==0 || Syy==0) y m�s ....*/
            a = (lambda - Syy) / delta;
            b = Sxy / delta;
        }
        else
            if (Syy < Sxx) { /*linea horizontal */
              a = (float) 0.0;
              b = (float) 1.0;
              //if (unflag) printf ("Nos metemos en horizontal");
            }
            else {
              a = (float) 1.0;
              b = (float) 0.0;
              //if (unflag) printf ("Nos metemos en vertical");
            }
        
        c = - ( (a) * meanX + (b) * meanY);
        if (c < 0) {
          a = -a;
          b = -b;
          c = -c;
        }
        
        double[] regresionLine = new double[3];
        regresionLine[0] = a;
        regresionLine[1] = b;
        regresionLine[2] = c;
        
        return regresionLine;
    }
    
    private static Point2D.Double Projection(Point2D point, double[] line){
        double x = ((line[1] * line[1] * point.getX() - line[0] * line[1] * point.getY() - line[0] * line[2]) / (line[1] * line[1] + line[0] * line[0]));
        double y = (- (line[1] * line[2] + line[0] * line[1] * point.getX() - line[0] * line[0] * point.getY()) / (line[1] * line[1] + line[0] * line[0]));

        return new Point2D.Double(x,y);
    }
    
    private static double distance(Point2D a, Point2D b){
        return Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY()));
    }
    
    private static double getSegmentLinearity(Contour contour, int windowSize, int start, double k){
        int numPoints = contour.size();
        double[] line;
        line = linearRegresion(contour,start, windowSize);
        Point2D.Double projectedPoint;
        
        Point2D.Float mean = new Point2D.Float(0.0f,0.0f);


        for (int i = start; i < start + windowSize; i++) {
            mean.x += contour.get(i % numPoints).getX(); //Calculamos la media
            mean.y += contour.get(i % numPoints).getY();
        }
        mean.x /= windowSize;
        mean.y /= windowSize;
        
        double residuo = 0.0;
        double total = 0.0;
        for (int i = start; i < start + windowSize; i++) {
            projectedPoint = Projection(contour.get(i % numPoints),line);
            residuo += distance(contour.get(i % numPoints),projectedPoint);
            total += distance(contour.get(i % numPoints),mean);
        }
        
        return Math.pow(1-(residuo/total),k);
    }
    
    private static double escalon(double x, double alpha, double beta){
        if (x < alpha)
            return 0;
        else if (x < beta)
            return (x-alpha)/(beta-alpha);
        
        return 1;
    }
}
