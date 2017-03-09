package jfi.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class representing a shape contour segmentation in the sense of a crisp set 
 * of segments from a given contour. 
 * 
 * There is not restrictions about the type of segmentation (covered or not, 
 * overlapped or not, etc.), except that all the segmments have to share the 
 * same source contour. Thus, the segmentation  not necessarily have to cover 
 * the whole contour (i.e., there may be contour  points that don´t belong to 
 * any segment). In addition, overlapped segments are also allowed.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class ContourSegmentation extends ArrayList<ContourSegment>{
    private final Contour contour;
    
    /**
     * Constructs an empty contour segmentation.
     * 
     * @param contour source contour containing the segment of this segmentation
     */
    public ContourSegmentation(Contour contour){
        this.contour = contour;
    }
    
    /**
     * Appends the specified segment to this segmentation.
     *
     * @param s segment to be appended to this segmentation
     * @throws InvalidParameterException if the segment contour is different from
     * this segmentation source contour
     * @return <tt>true</tt> as specified by {@link Collection#add}) 
     */
    @Override
    public boolean add(ContourSegment s){
        if(s.getSourceContour()!=this.contour){
            throw new InvalidParameterException("Segment from a different contour.");
        }
        return super.add(s);
    }
     
    /**
     * Inserts the specified segment at the specified position in this 
     * segmentation. Shifts the element currently at that position (if any) 
     * and any subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified segment is to be inserted
     * @param s segment to be inserted
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws InvalidParameterException if the segment contour is different from
     * this segmentation source contour
     */
    @Override
    public void add(int index, ContourSegment s){
       if(s.getSourceContour()!=this.contour){
            throw new InvalidParameterException("Segment from a different contour.");
        }
        super.add(index,s); 
    }
    
    /**
     * Appends all of the segments in the specified collection to the end of
     * this segmentation, in the order that they are returned by the
     * specified collection's Iterator. 
     *
     * @param c collection containing segments to be added to this segmentation
     * @return <tt>true</tt> if this segmentatin changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     * @throws InvalidParameterException if the segment contour is different from
     * this segmentation source contour
     */
    @Override
    public boolean addAll(Collection<? extends ContourSegment> c) {
        for(ContourSegment s : c) {
            if(s.getSourceContour() != this.contour) {
                throw new InvalidParameterException("Segment from a different contour.");
            }
        }
        return super.addAll(c);
    }
    
    /**
     * Inserts all of the segments in the specified collection into this
     * segmentation, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).
     *
     * @param index index at which to insert the first element from the
     *              specified collection
     * @param c collection containing elements to be added to this list
     * @return <tt>true</tt> if this segmentation changed as a result of the call
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException if the specified collection is null
     * @throws InvalidParameterException if the segment contour is different from
     * this segmentation source contour
     */
    @Override
     public boolean addAll(int index, Collection<? extends ContourSegment> c){
         for(ContourSegment s : c) {
            if(s.getSourceContour() != this.contour) {
                throw new InvalidParameterException("Segment from a different contour.");
            }
        }
         return super.addAll(index, c);
     }
    
    /**
     * Returns the first segment containig the given point.
     * 
     * If the point belongs to more than one segment, only the first occurrence
     * is returned.
     * 
     * @param point the analyzed point 
     * @return the first segment containig the given point
     */    
    public ContourSegment getSegment(Point2D point){
        for(ContourSegment s: this){
            if(s.contains(point)) return s;              
        }
        return null;
    }
    
    /**
     * Returns the source contour of this segmentation
     * 
     * @return the source contour
     */
    public Contour getContour(){
        return contour;
    }
    
    /**
     * Draws the endpoints of this segmentation into an image.
     * 
     * The endpoints are marked as a blue cross, and the alpha-component is used 
     * to set transparent the background. The image size will be set automatically
     * to the rectangle that bounds the source contour using the actual coordinates 
     * values and the (0.0) as the origin (top-left).
     * 
     * @return an image with the segment endpoints drawn
     */
    public BufferedImage toImage() {
        BufferedImage segmentationImage = null;
        if (contour != null) {
            Rectangle bounds = contour.getBounds();
            int width = bounds.width + bounds.x;
            int height = bounds.height + bounds.y;
            segmentationImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = segmentationImage.createGraphics();
            g2d.setColor(Color.blue);
            Point mark;
            for (ContourSegment s : this) {
                mark = new Point((int) s.getStartPoint().getX(), (int) s.getStartPoint().getY());
                g2d.drawLine(mark.x - 3, mark.y, mark.x + 3, mark.y);
                g2d.drawLine(mark.x, mark.y - 3, mark.x, mark.y + 3);

            }
        }
        return segmentationImage;
    }
}
