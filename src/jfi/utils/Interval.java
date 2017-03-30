package jfi.utils;

import java.security.InvalidParameterException;

/**
 * Interval of numbers (open or closed).
 * 
 * @param <N> the interval domain.
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class Interval<N extends Number> {
    /**
     * Left endpoint.
     */
    private N left;
    /**
     * Right endpoint.
     */
    private N right;
    /**
     * Flag for open-left interval.
     */
    private boolean left_open;
    /**
     * Flag for open-right interval.
     */
    private boolean right_open;

    /**
     * Constructs a new closed interval.
     * 
     * @param left left endpoint.
     * @param right right endpoint.
     */
    public Interval(N left, N right) {
        this(left,right,false,false);
    }
    
    /**
     * Constructs a new interval.
     * 
     * @param left left endpoint.
     * @param right right endpoint.
     * @param left_open <tt>true</tt> for an open-left interval.
     * @param right_open <tt>true</tt> for an open-right interval.
     */
    public Interval(N left, N right, boolean left_open, boolean right_open) {
        this.setEndPoints(left, right);
        this.setLeftOpen(left_open);
        this.setRightOpen(right_open);
    }    

    /**
     * Set the endpoints of this interval.
     * 
     * @param left left endpoint.
     * @param right right endpoint.
     */
    public void setEndPoints(N left, N right){
        if (left.doubleValue()>right.doubleValue()) {
            throw new InvalidParameterException("The left endpoint must be less or equal than the right one");
        }
        this.left = left;
        this.right = right;
    }
    
    /**
     * Set the left endpoint of this interval.
     * 
     * @param left the left endpoint
     */
    public void setLeftEndpoint(N left) {
        this.setEndPoints(left,this.right);
    }

    /**
     * Set the right endpoint of this interval.
     * 
     * @param right the right endpoint
     */
    public void setRightEndpoint(N right) {
        this.setEndPoints(this.left, right);
    }
    
    /**
     * Returns the left endpoint of this interval.
     * 
     * @return the left endpoint of this interval
     */
    public N getLeftEndpoint() {
        return left;
    }

    /**
     * Returns the right endpoint of this interval.
     * 
     * @return the right endpoint of this interval
     */
    public N getRightEndpoint() {
        return right;
    }

    /**
     * Check if the interval is left-open.
     * 
     * @return <tt>true</tt> if the interval is left-open.
     */
    public boolean isLeftOpen(){
        return this.left_open;
    }
    
    /**
     * Check if the interval is right-open.
     * 
     * @return <tt>true</tt> if the interval is right-open.
     */
    public boolean isRightOpen(){
        return this.right_open;
    }
    
    /**
     * Set the left-open status (open or closed).
     * 
     * @param status the new left-open status.
     */
    public void setLeftOpen(boolean status){
        this.left_open = status;
    }
    
    /**
     * Set the right-open status (open or closed).
     * 
     * @param status the new right-open status.
     */
    public void setRightOpen(boolean status){
        this.right_open = status;
    }
    
    /**
     * Checks if this intervals contains the given number.
     * 
     * @param n the number to be analyzed.
     * @return <tt>true</tt> if this intervals contains the given number, 
     * <tt>false</tt> if not
     */
    public boolean contains(N n) {
        boolean inside_left = left_open ? (n.doubleValue() > left.doubleValue()) : (n.doubleValue() >= left.doubleValue());
        boolean inside_right = right_open ? (n.doubleValue() < right.doubleValue()) : (n.doubleValue() <= right.doubleValue());
        return inside_left && inside_right;
    }
    
    /**
     * Return the centre (midpoint) of interval.
     * 
     * @return the centre of interval
     */
    public N center(){
        Double center = (left.doubleValue()+right.doubleValue())/2;
        return (N)center;
    }
    
    /**
     * Return the length of the interval.
     * 
     * @return the length of the interval 
     */
    public double length() {
        return Math.abs(right.doubleValue() - left.doubleValue());
    }

    /**
     * Check if the interval is empty (i.e, if the endpoints are equal).
     * 
     * @return <tt>true</tt> if the interval is empty
     */
    public boolean isEmpty() {
        return left.doubleValue() > right.doubleValue();
    }
}
