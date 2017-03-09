/*
  Closed interval of numbers

  @author Jesús Chamorro Martínez - UGR
 */

/*
La implementación actual es para el caso 1D. Pendiente la generalización a ND
*/

package jfi.utils;

public class Interval<N extends Number> {

    /**
     * Left endpoint
     */
    private N a;
    /**
     * Right endpoint
     */
    private N b;

    /**
     * Constructs a new interval.
     * 
     * @param a left endpoint
     * @param b right endpoint
     */
    public Interval(N a, N b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Returns the left endpoint of this interval.
     * 
     * @return the left endpoint of this interval
     */
    public N getA() {
        return a;
    }

    /**
     * Returns the right endpoint of this interval.
     * 
     * @return the right endpoint of this interval
     */
    public N getB() {
        return b;
    }

    /**
     * Set the left endpoint of this interval.
     * 
     * @param a the left endpoint
     */
    public void setA(N a) {
        this.a = a;
    }

    /**
     * Set the right endpoint of this interval.
     * 
     * @param b the right endpoint
     */
    public void setB(N b) {
        this.b = b;
    }

    /**
     * Checks if this intervals contains the given number.
     * 
     * @param number the number to be analyzed.
     * @return <tt>true</tt> if this intervals contains the given number, 
     * <tt>false</tt> if not
     */
    public boolean contains(N number) {
        return (number.doubleValue() >= a.doubleValue() && number.doubleValue() <= b.doubleValue());
    }
    
    /**
     * Return the centre (midpoint) of interval.
     * 
     * @return the centre of interval
     */
    public N center(){
        Double center = (a.doubleValue()+b.doubleValue())/2;
        return (N)center;
    }
    
    /**
     * Return the length of the interval.
     * 
     * @return the length of the interval 
     */
    public double length() {
        return Math.abs(b.doubleValue() - a.doubleValue());
    }

    /**
     * Check if the interval is empty.
     * 
     * @return <tt>true</tt> if the interval is empty
     */
    public boolean isEmpty() {
        return a.doubleValue() > b.doubleValue();
    }
}
