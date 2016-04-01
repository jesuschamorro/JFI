/*
  Contour represented as a collection of 2D points

  @author Jesús Chamorro Martínez - UGR
*/
package jfi.shape;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Collection;
import jfi.utils.JFIMath;

public class Contour extends ArrayList<Point2D> { 
    
    /**
     *  Default ratio for window creation.
     */
    public static double windowRatio = 1.0/15;
    
    /**
     *  Default offset for computing curvature.
     */
    public static int offset = 0;
    
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
     * Draws the contour points into an image
     * 
     * @return an image with the contour drawn
     */
    public BufferedImage toImage(){
        Rectangle bounds = this.getBounds();
        BufferedImage img = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_BYTE_GRAY);
        int x,y;

        WritableRaster imgRaster = img.getRaster();
        for (Point2D point : this) {
            x = (int) point.getX()-bounds.x;
            y = (int) point.getY()-bounds.y;
            imgRaster.setSample(x, y, 0, 255);
        } 
        return img;
    }
    
    /**
     * Returns an integer Rectangle that completely encloses the contour
     *
     * @return an integer Rectangle that completely encloses the contour.
     */
    public Rectangle getBounds() {
        int maxX = 0, maxY = 0;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (Point2D point : this) {
            if (maxX < point.getX()) {
                maxX = (int) point.getX();
            }
            if (minX > point.getX()) {
                minX = (int) point.getX();
            }
            if (maxY < point.getY()) {
                maxY = (int) point.getY();
            }
            if (minY > point.getY()) {
                minY = (int) point.getY();
            }
        }
        return new Rectangle(minX, minY, maxX-minX+1, maxY-minY+1);
    }
    
    /**
     * Applies a mask to the contour
     * 
     * @param mask
     * 
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
    
    /**
     * Calculates the curvature of the contour
     * 
     * @return Curvature of the contour
     */
    public CurvatureFunction getCurvature(){
        int windowSize = (int) windowRatio * this.size();
        
        return getCurvature(windowSize, offset);
    }
    
    /**
     * Calculates the curvature of the contour
     * 
     * @param windowSize Size of the segments used to calculate the curvature
     * @param offset distance between the point where curvature is calculated and the start of the segment
     * 
     * @return Curvature of the contour
     */
    public CurvatureFunction getCurvature(int windowSize, int offset){
        CurvatureFunction curvature = new CurvatureFunction();
        
        double currentCurvature;
        double arc_cos, arc_cos2;
        Point2D.Double firstDirectionVector, secondDirectionVector;
        
        int numPoints = this.size();
        
        if (windowSize > numPoints)
            windowSize = numPoints;

        for (int i = 0; i < numPoints; i++) {

            secondDirectionVector = JFIMath.getDirectionVector(this.getSegment(i+offset, windowSize));
            firstDirectionVector = JFIMath.getDirectionVector(this.getSegment((i-offset+numPoints)%numPoints,-windowSize));
            
            arc_cos = ( (firstDirectionVector.y < 0) ? -Math.acos( (double) firstDirectionVector.x) :
                               Math.acos( (double) firstDirectionVector.x));
            arc_cos2 =( (secondDirectionVector.y < 0) ? -Math.acos( (double) secondDirectionVector.x) :
                                Math.acos( (double) secondDirectionVector.x));
            currentCurvature = arc_cos - arc_cos2;

            if (arc_cos2 > arc_cos)
                currentCurvature += (float) (Math.PI);
            else
                currentCurvature -= (float) Math.PI;
            
            curvature.add(currentCurvature);
        }
        
        return curvature;
    }
    
    /**
     * Return a segment of the contour
     * 
     * If windowSize is negative, the segment will be the previous "-windowSize" points
     * 
     * @param start Starting point
     * @param windowSize Size of the generated segment.   
     * 
     * @return ArrayList with the points of the segment
     */
    public ArrayList<Point2D> getSegment(int start, int windowSize){
        ArrayList<Point2D> segment = new ArrayList<>();
        
        if (windowSize > 0){
            for(int i = 0; i < windowSize; i++){
                segment.add(this.get((start + i) % this.size()));
            }
        }
        else{
            for(int i = 0; i > windowSize; i--){
                segment.add(this.get((start + i +this.size())%this.size()));
            }
        }
        
        return segment;
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

}
