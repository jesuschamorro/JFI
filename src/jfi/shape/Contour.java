/*
  Contour represented as a collection of 2D points

  @author Jesús Chamorro Martínez - UGR
*/
package jfi.shape;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Collection;

public class Contour extends ArrayList<Point2D> { 
    /**
     * Constructs an empty contour.
     */
    public Contour(){
        super();
    }
    /**
     * Constructs a contour containing the points of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param points the collection whose elements are to be placed into this contour
     * @throws NullPointerException if the specified collection is null
     */
    public Contour(Collection<Point> points){
        super(points);
    }
    /**
     * Constructs a contour from an mask image. 
     * 
     * It is assumed that the mask contains a single connected component. If not,
     * only the contour of the first shape (starting from the top-left) is created
     * 
     * @param mask the mask image
     */
    public Contour(ImageMask mask){ 
        super(); 
        if(mask!=null){
                        
            int r, c, S = 6, iter;  //Pos es 1 pq el vector d puntos empieza en 1 ya que la componente 0 es para el tama�o
            Point currentPoint;
            boolean pointFound, firstIteration = true;
            Point initialPoint = null;

            
            for (r = 0, pointFound = false; r < mask.getHeight() && !pointFound; r++)
              for (c = 0; c < mask.getWidth() && !pointFound; c++)
                if (mask.getRaster().getSample(c, r, 0) != 0) {
                  initialPoint = new Point(c,r);
                  pointFound = true;
                }
            /*if (!pointFound)return null; salir*/
            
            // Comienza el recorrido del contorno
            currentPoint = new Point(initialPoint);
            while ( ( (currentPoint.x != initialPoint.x) || (currentPoint.y != initialPoint.y)) || (firstIteration)) {
                this.add(new Point(currentPoint)); //Guardamos el actual fuera
                pointFound = false;
                iter = 0; // iteramos
                while ( (!pointFound) && (iter < 3)) {
                    if (pointFound = isFrontierPoint((8 + (S - 1)) % 8, currentPoint,mask)) {
                        S = (8 + (S - 2)) % 8;
                    }
                    else {
                        if (pointFound = isFrontierPoint(S, currentPoint, mask))
                            S = S;
                        else {
                            if (pointFound = isFrontierPoint((S + 1) % 8, currentPoint, mask))
                                S = S;
                            else S = (S + 2) % 8;
                        }
                    }
                    iter = iter++;
                }

                firstIteration = false;
            }         
        }
    }
    /**
     * Calculates the curvature function of the contour, normalizing and scaling 
     * the output 
     * 
     * @param normalize If true, the output is normalized by the maximum 
     * @param new_scale If it is positive, the output is scaled in X to new_scale.
     * If it is zero or negative, the function is not scaled
     * @param windowSize
     * @param offset
     * @return The curvature function of the contour
     */
    public CurvatureFunction getCurvature(boolean normalize, int new_scale, int windowSize, int offset){
        CurvatureFunction curvature = this.getCurvature(windowSize,offset);
        if(normalize)
            curvature.normalize();
        if(new_scale>0)
           curvature.scale(new_scale);
        return curvature;
    }
    
    /**
     * Calculates the curvature function of the contour with automatic 
     * parameters estimation
     * 
     * @return The curvature function of the contour
     */
    public CurvatureFunction getCurvature(){
        int windowSize = 20, offset = 2;
        
        //TODO
        //Calcular automaticamente los parámetros
        
        return this.getCurvature(windowSize, offset);
    }
    
    
    /**
     * Calculates the curvature function of the contour
     * 
     * @param windowSize
     * @param offset
     * @return the curvature function
     */
    public CurvatureFunction getCurvature(int windowSize, int offset){
        CurvatureFunction curvature =  new CurvatureFunction();
        
        double currentCurvature;
        double arc_cos, arc_cos2; //Hacen falta para guardar los arcos coseno
        Point2D.Double firstDirectionVector, secondDirectionVector;
        int numPoints = this.size();
        
        if (windowSize > numPoints)
            windowSize = numPoints;

        for (int i = 0; i < numPoints; i++) {

            firstDirectionVector = getDirectionVector(windowSize,numPoints + i - (windowSize + offset)%numPoints, i);
            secondDirectionVector = getDirectionVector(windowSize,i+offset % numPoints,i);

            arc_cos = ( (firstDirectionVector.y < 0) ? -Math.acos( (double) firstDirectionVector.x) :
                               Math.acos( (double) firstDirectionVector.x));
            arc_cos2 =( (secondDirectionVector.y < 0) ? -Math.acos( (double) secondDirectionVector.x) :
                                Math.acos( (double) secondDirectionVector.x));
            currentCurvature = arc_cos - arc_cos2;

            if (arc_cos2 > arc_cos)
                currentCurvature+= (float) (Math.PI);
            else
                currentCurvature-= (float) Math.PI;
            
            curvature.add(currentCurvature);
        }
        
        return curvature;
    }
    
    public CurvatureFunction[] getLinearity(int windowSize, int offset, double k){
        CurvatureFunction linearity_der =  new CurvatureFunction();
        CurvatureFunction linearity_izq =  new CurvatureFunction();
        CurvatureFunction linearity_total =  new CurvatureFunction();
        int numPoints = this.size();
        
        if (windowSize > numPoints)
            windowSize = numPoints;

        for (int i = 0; i < numPoints; i++) {
            linearity_der.add(computeLinearity(windowSize,i+offset,k));
            linearity_izq.add(computeLinearity(windowSize,(i-offset-windowSize+1 + numPoints) % numPoints,k));
            linearity_total.add(computeLinearity(2*windowSize,(i-offset-windowSize+1 + numPoints) % numPoints,k));
        }
        CurvatureFunction[] linearity = new CurvatureFunction[3];
        linearity[0] = linearity_der;
        linearity[1] = linearity_izq;
        linearity[2] = linearity_total;
        
        return linearity;
    }
    
    public CurvatureFunction getVerticity(int windowSize, double exponent,double vv_min,double vv_max){
        CurvatureFunction verticity = new CurvatureFunction();
        CurvatureFunction[] linearity = this.getLinearity(windowSize, 0,exponent);
        double actualVerticity; 
        
        for (int i = 0; i < linearity[0].size(); i++){
            actualVerticity = Math.min(escalon(linearity[0].apply(i),0,1),escalon(linearity[1].apply(i),0,1));
            actualVerticity = Math.min(actualVerticity,escalon(1-linearity[2].apply(i),vv_min,vv_max));
            verticity.add(actualVerticity);
        }
        
        return verticity;
    }
    /**
     * Draws the contour points into an image
     * 
     * @return an image with the contour drawn
     */
    public BufferedImage toImage(){
        Dimension size;
        size = this.imageSize();
        
        BufferedImage img = new BufferedImage(size.width,size.height,BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster imgRaster = img.getRaster();
        for (Point2D point : this) {
            imgRaster.setSample((int)point.getX(), (int)point.getY(), 0, 1);
        } 
        return img;
    }
    /**
     * Applies a mask to the contour
     * 
     * @param mask
     * @return filtered contour.
     */
    public Contour filter(ArrayList<Double> mask){
        Point2D.Double point;
        Contour filteredContour = new Contour();
        
        for(int i = 0; i < this.size(); i++){
            point = new Point2D.Double(0.0,0.0);
            for(int j = 0; j < mask.size(); j++){
                point.x += this.get((i+j-mask.size()/2+this.size())%this.size()).getX()* mask.get(j);
                point.y += this.get((i+j-mask.size()/2+this.size())%this.size()).getY()* mask.get(j);
            }
            
            filteredContour.add(point);
        }
        return filteredContour;
    }
    
    private boolean isFrontierPoint(int direction, Point actual, BufferedImage image){
        Point nextStep = freemanStep(direction,actual);

        if(0 <= nextStep.x && nextStep.x < image.getWidth() && 
           0 <= nextStep.y && nextStep.y < image.getHeight()){

            if (image.getRaster().getSample(nextStep.x, nextStep.y, 0) !=0 ) {
                actual.x = nextStep.x;
                actual.y = nextStep.y;
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }
      
    private Point freemanStep(int direction, Point actualPoint){
        Point nextStep = new Point(actualPoint);
        
        switch (direction){ /* Dependiendo del valor de S...*/
            case 0:
                nextStep.x++;
                break;
            case 1: 
                nextStep.x++;
                nextStep.y--;
                break;
            case 2:
                nextStep.y--;
                break;
            case 3: 
                nextStep.x--;
                nextStep.y--;
                break;
            case 4:
                nextStep.x--;
                break;
            case 5: 
                nextStep.x--;
                nextStep.y++;
                break;
            case 6:
                nextStep.y++;
                break;
            case 7: 
                nextStep.x++;
                nextStep.y++;
                break;
        }
        
        return nextStep;
    }

    private Point2D.Double getDirectionVector( int win, int start, int i){
        int numPoints = this.size();
        float mod;
        double[] line;
        Point2D.Double projectedPoint;
        Point2D.Double projectedInitialPoint;
        Point2D.Double meanPoint;
        Point2D.Double directionVector;
        
        line = linearRegresion(start, win);
            
        projectedInitialPoint = Projection(this.get(i),line);

        meanPoint = new Point2D.Double(0.0,0.0);
        for (int k = start; k < start + win; k++) {
            projectedPoint = Projection(this.get(k % numPoints),line);
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
       
    private Point2D.Double Projection(Point2D point, double[] line){
        double x = ((line[1] * line[1] * point.getX() - line[0] * line[1] * point.getY() - line[0] * line[2]) / (line[1] * line[1] + line[0] * line[0]));
        double y = (- (line[1] * line[2] + line[0] * line[1] * point.getX() - line[0] * line[0] * point.getY()) / (line[1] * line[1] + line[0] * line[0]));

        return new Point2D.Double(x,y);
    }

    private double[] linearRegresion(int start, int windowSize){
        double meanX, meanY, Sxy, Sxx, Syy;
        double aux, aux1;
        int j;
        double trS2, det;
        double lambda, delta;
        int tamElp = this.size();
        double a,b,c;

        meanX = meanY = (float) 0.0;
        for (j = start; j < start + windowSize; j++) {
            meanX += this.get(j % tamElp).getX(); //Calculamos la media
            meanY += this.get(j % tamElp).getY();
        }
        meanX /= windowSize;
        meanY /= windowSize;

        Sxx = Syy = Sxy = (float) 0.0;
        
        //2nd central moments
        for (j = start; j < start + windowSize; j++) {
          aux = this.get(j % tamElp).getX() - meanX;
          aux1 = this.get(j % tamElp).getY() - meanY;
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
   
    private Dimension imageSize(){
        Dimension dimension = new Dimension();
        
        double maxX = 0;
        double maxY = 0;
        
        for (Point2D point: this){
            if (maxX < point.getX())
                maxX = point.getX();
            if (maxY < point.getY())
                maxY = point.getY();
        }
        
        dimension.width = (int)Math.ceil(maxX)+1;
        dimension.height = (int) Math.ceil(maxY)+1;
        
        return dimension;
    }
    
    private double distance(Point2D a, Point2D b){
        return Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY()));
    }
    
    private double computeLinearity(int windowSize, int start, double k){
        int numPoints = this.size();
        double[] line;
        line = linearRegresion(start, windowSize);
        Point2D.Double projectedPoint;
        
        Point2D.Float mean = new Point2D.Float(0.0f,0.0f);
        int tamElp = this.size();

        for (int i = start; i < start + windowSize; i++) {
            mean.x += this.get(i % tamElp).getX(); //Calculamos la media
            mean.y += this.get(i % tamElp).getY();
        }
        mean.x /= windowSize;
        mean.y /= windowSize;
        
        double residuo = 0.0;
        double total = 0.0;
        for (int i = start; i < start + windowSize; i++) {
            projectedPoint = Projection(this.get(i % numPoints),line);
            residuo += distance(this.get(i % numPoints),projectedPoint);
            total += distance(this.get(i % numPoints),mean);
        }
        
        return Math.pow(1-(residuo/total),k);
    }
    
    public double escalon(double x, double alpha, double beta){
        if (x < alpha)
            return 0;
        else if (x < beta)
            return (x-alpha)/(beta-alpha);
        
        return 1;
    }

}
