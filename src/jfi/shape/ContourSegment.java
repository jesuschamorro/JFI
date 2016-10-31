package jfi.shape;

import java.awt.geom.Point2D;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class ContourSegment {
    /**
     * The source contour containing this segment
     */
    private Contour contour;
    /**
     * The endpoints of this segment
     */
    private Point2D start, end;    
    /**
     * Flag to know if the clockwise direcction is used in the contour round
     */
    private boolean clockwise = true;
    
    /**
     * Constructs a contour segment connecting the points <code>start</code> and 
     * <code>end</code> (both included). If <code>start==end</code>, a segment 
     * with a single point is created.
     * 
     * @param start the starting point of the segment
     * @param end the ending point of the segment
     * @param contour source contour containing the segment
     * @param clockwise if <tt>true</tt>, <code>end</code> is reached from 
     * <code>start</code> in the clockwise direcction
     */
    public ContourSegment(Point2D start, Point2D end, Contour contour, boolean clockwise){
        if(contour==null){
            throw new InvalidParameterException("Countour cannot be null.");
        }
        this.contour = contour;
        this.clockwise = clockwise;
        this.setStartPoint(start); // It checks if start is a valid point
        this.setEndPoint(end);     // It checks if end is a valid point
    }   
    
    /**
     * Constructs a contour segment connecting the points <code>start</code> and 
     * <code>end</code> (both included). If <code>start==end</code>, a segment 
     * with a single point is created. 
     * 
     * By default, the <code>contour</code> clockwise flag is used 
     * 
     * @param start the starting point of the segment
     * @param end the ending point of the segment
     * @param contour source contour containing the segment
     */
    public ContourSegment(Point2D start, Point2D end, Contour contour){
        this(start, end, contour, contour!=null?contour.isClockwise():null);
    }
    
    /**
     * Constructs a contour segment of size <code>segment_size</code> starting 
     * from the point <code>start</code> in <code>contour</code>
     * 
     * @param start the starting point of the segment
     * @param segment_size the segment size
     * @param contour source contour containing the segment
     * @param clockwise if <tt>true</tt>, <code>end</code> is reached from 
     * <code>start</code> in the clockwise direcction
     */
    public ContourSegment(Point2D start, int segment_size, Contour contour, boolean clockwise){
        if(contour==null){
            throw new InvalidParameterException("Countour cannot be null.");
        }
        this.contour = contour;
        this.clockwise = clockwise;
        this.setStartPoint(start); // It checks if start is a valid point
        if(segment_size > contour.size()){
            throw new InvalidParameterException("Segment size bigger than contour size.");
        } 
        int index_end = clockwise ? (contour.indexOf(start)+segment_size-1)%contour.size() : (contour.indexOf(start)-segment_size+1+contour.size())%contour.size(); 
        this.end = contour.get(index_end); // It is a valid point, check is not needed
    }
    
    /**
     * Constructs a contour segment of size <code>segment_size</code> starting 
     * from the point <code>start</code> in <code>contour</code>. 
     * 
     * By default, the <code>contour</code> clockwise flag is used.
     * 
     * @param start the starting point of the segment
     * @param segment_size the segment size
     * @param contour source contour containing the segment
     */
    public ContourSegment(Point2D start, int segment_size, Contour contour){
        this(start,segment_size,contour,contour!=null?contour.isClockwise():null);
    }
      
    /**
     * Returns the starting point of this segment
     * @return the starting point of this segment
     */
    public Point2D getStartPoint(){
        return start;
    }
    
    /**
     * Returns the ending point of this segment
     * @return the ending point of this segment
     */
    public Point2D getEndPoint(){
        return end;
    }
    
    /**
     * Returns the contour containing this segment
     * 
     * @return the source contour
     */
    public Contour getSourceContour(){
        return contour;
    }
    
    /**
     * Returns <tt>true</tt> if the clockwise direcction is used in the
     * contour round.
     * 
     * @return the clockwise direcction state
     */
    public boolean isClockwise(){
        return clockwise;
    }
    
    /**
     * Set the starting point of this segment
     * @param start the starting point of the segment 
     */
    public final void setStartPoint(Point2D start){
        if(!contour.contains(start)){
            throw new InvalidParameterException("Endpoint must be contained in the contour.");
        }
        this.start = start;
    }
    
    /**
     * Set the ending point of this segment
     * @param end the ending point of the segment 
     */
    public final void setEndPoint(Point2D end){
        if(!contour.contains(end)){
            throw new InvalidParameterException("Endpoint must be contained in the contour.");
        }
        this.end = end;
    }
       
    /**
     * Returns <tt>true</tt> if this segment contains the specified point.
     * 
     * @param point point to be tested
     * @return <tt>true</tt> if this segment contains the specified point
     */
    public boolean contains(Point2D point) {
        boolean found = false;
        ContourIterator it = new ContourIterator(contour,start,clockwise);  
        while(!it.isPrevious(end) && !found){
            found = point.equals(it.next());         
        }
        return found;
    }
    
    /**
     * Return the segment as a collection of points (endpoits included). 
     *  
     * @return the segment as a collection of points
     */
    public ArrayList<Point2D> toArray(){       
        ArrayList<Point2D> segment = new ArrayList<>();
        ContourIterator it = new ContourIterator(contour,start,clockwise);
        while(!it.isPrevious(end)){
            segment.add(it.next());         
        }
        return segment;
    }
    // Lo anterior sería equivalente a hacer la llamada: contour.getSegment(start, end);
    // Lo voy a poner de esta forma porque me estoy plantendo cambiar los métodos
    // 'getSegment' de Contour
}
