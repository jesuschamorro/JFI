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
