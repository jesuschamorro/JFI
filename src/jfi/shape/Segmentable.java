package jfi.shape;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public interface Segmentable {
    /**
     * 
     * @param start
     * @param end
     * @param clockwise
     * @return 
     */
    public ArrayList<Point2D> getSegment(Point2D start, Point2D end, boolean clockwise);
    /**
     * 
     * @param start
     * @param segment_size
     * @param clockwise
     * @return 
     */
    public ArrayList<Point2D> getSegment(Point2D start, int segment_size, boolean clockwise);
    /**
     * 
     * @param start
     * @param end
     * @return 
     */
    default public ArrayList<Point2D> getSegment(Point2D start, Point2D end){
        return getSegment(start, end, true);
    }
    /**
     * 
     * @param start
     * @param segment_size
     * @return 
     */
    default public ArrayList<Point2D> getSegment(Point2D start, int segment_size){
        return getSegment(start, segment_size, true);
    }
}
