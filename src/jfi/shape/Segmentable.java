package jfi.shape;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public interface Segmentable {
    
    public ArrayList<Point2D> getSegment(Point2D start, Point2D end, boolean clockwise);
    
    public ArrayList<Point2D> getSegment(Point2D start, int segment_size, boolean clockwise);
    
    default public ArrayList<Point2D> getSegment(Point2D start, Point2D end){
        return getSegment(start, end, true);
    }
    
    default public ArrayList<Point2D> getSegment(Point2D start, int segment_size){
        return getSegment(start, segment_size, true);
    }
}
