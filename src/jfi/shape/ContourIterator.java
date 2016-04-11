package jfi.shape;

import java.awt.geom.Point2D;
import java.util.Iterator;

/**
 * An iterator over a closed contour.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 * @see Contour
 */
public class ContourIterator implements Iterator<Point2D> {

    private final Contour contour;
    private boolean clockwise = true;
    private int index = 0;

    /**
     * Construct a new iterator over a given contour
     *
     * @param contour the contour to be iterated
     * @param start starting point in the iteration process
     * @param clockwise iteration in clockwise direcction
     */
    public ContourIterator(Contour contour, Point2D start, boolean clockwise) {
        this.contour = contour;
        this.setCurrent(start);
        this.clockwise = clockwise;
    }

    /**
     * Construct a new iterator over a given contour with clockwise direcction
     * by default.
     *
     * @param contour the contour to be iterated
     * @param start starting point in the iteration process
     */
    public ContourIterator(Contour contour, Point2D start) {
        this(contour, start, true);
    }

    /**
     * Construct a new iterator over a given contour.
     *
     * @param contour the contour to be iterated
     * @param clockwise iteration in clockwise direcction
     */
    public ContourIterator(Contour contour, boolean clockwise) {
        this(contour, null, clockwise);
    }

    /**
     * Construct a new iterator over a given contour with clockwise direcction
     * by default.
     *
     * @param contour the contour to be iterated
     */
    public ContourIterator(Contour contour) {
        this(contour, null, true);
    }

    /**
     * Set <code>point</code> as the current point in the iteration
     *
     * @param p
     */
    public final void setCurrent(Point2D p) {
        int p_index = (p != null) ? contour.indexOf(p) : 0;
        index = (p_index >= 0) ? p_index : 0;
    }

    /**
     * Return the current point in the iteration.
     *
     * @return the current point in the iteration
     */
    public Point2D getCurrent() {
        return contour.get(index);
    }
    
    /**
     * Always <tt>true</tt> in a closed contour
     *
     * @return  <tt>true</tt>
     */
    @Override
    public boolean hasNext() {
        return true;
    }

    /**
     * Returns <tt>true</tt> if the next point in the iteration is
     * <code>point</code>
     *
     * @param point the point to be checked
     * @return <tt>true</tt> if the next point in the iteration is
     * <code>point</code>
     */
    public boolean isNext(Point2D point) {
        return point.equals(contour.get(nextIndex()));
    }

    /**
     * Returns <tt>true</tt> if the current point in the iteration is
     * <code>point</code>
     *
     * @param point the point to be checked
     * @return <tt>true</tt> if the current point in the iteration is
     * <code>point</code>
     */
    public boolean isCurrent(Point2D point) {
        return point.equals(contour.get(index));
    }

    /**
     * Returns <tt>true</tt> if the previous point in the iteration is
     * <code>point</code>
     *
     * @param point the point to be checked
     * @return <tt>true</tt> if the previous point in the iteration is
     * <code>point</code>
     */
    public boolean isPrevious(Point2D point) {
        return point.equals(contour.get(prevIndex()));
    }

    /**
     * Go forward in the iteration.
     *
     * @return the current point before go forward
     */
    @Override
    public Point2D next() {
        Point2D current_point = contour.get(index);
        index = nextIndex();
        return current_point;
    }

    /**
     * Go backward in the iteration.
     *
     * @return the current point before go backward
     */
    public Point2D previous() {
        Point2D current_point = contour.get(index);
        index = prevIndex();
        return current_point;
    }
    /**
     * Go forward/backward in the iteration an <code>offset</code> distance.
     * 
     * If <code>offset</code> is positive (resp. negative), the new point will 
     * be reached following the forward (resp. backward) way.
     *
     * @param offset
     * @return the current point before go forward
     */
    public Point2D advance(int offset) {
        Point2D current_point = contour.get(index);
        index = nextIndex(offset);
        return current_point;
    }
    
    /**
     * Returns the next index in the iteration plus a distance. In other words, 
     * returns the index of the point located an <code>offset</code> distance 
     * from the current point in the iteration direction. The flag <code>clockwise</code> 
     * will be taked into acount.
     * 
     * If <code>offset</code> is negative, the new index will be reached following 
     * the backward way
     *
     * @param offset distance from the current point
     * @return the next index in the iteration plus a distance
    */
    private int nextIndex(int offset) {
        offset = offset%contour.size();
        if(!clockwise) offset = -offset;
        return (index + offset + contour.size()) % contour.size();
    }
    
    /**
     * Returns the next index in the iteration.
     *
     * @return the next index in the iteration
     */
    private int nextIndex() {
        return nextIndex(1);
        //return clockwise ? (index + 1) % contour.size() : (index - 1 + contour.size()) % contour.size();
    }
    
    /**
     * Returns the previous index in the iteration.
     *
     * @return the previous index in the iteration
     */
    private int prevIndex() {
        return nextIndex(-1);
        //return clockwise ? (index - 1 + contour.size()) % contour.size() : (index + 1) % contour.size();
    }
}
