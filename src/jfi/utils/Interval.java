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
     *
     */
    private N a;
    /**
     *
     */
    private N b;

    /**
     *
     * @param a
     * @param b
     */
    public Interval(N a, N b) {
        this.a = a;
        this.b = b;
    }

    /**
     *
     * @return
     */
    public N getA() {
        return a;
    }

    /**
     *
     * @return
     */
    public N getB() {
        return b;
    }

    /**
     *
     * @param a
     */
    public void setA(N a) {
        this.a = a;
    }

    /**
     *
     * @param b
     */
    public void setB(N b) {
        this.b = b;
    }

    /**
     *
     * @param number
     * @return
     */
    public boolean contains(N number) {
        return (number.doubleValue() >= a.doubleValue() && number.doubleValue() <= b.doubleValue());
    }
    
    /**
     * Return the centre (midpoint) of interval
     * @return the centre of interval
     */
    public N center(){
        Double center = (a.doubleValue()+b.doubleValue())/2;
        return (N)center;
    }
    
    /**
     * Return the length of the interval
     * @return the length of the interval 
     */
    public double length() {
        return Math.abs(b.doubleValue() - a.doubleValue());
    }

    /**
     * Check if the interval is empty
     * @return <tt>true</tt> if the interval is empty
     */
    public boolean isEmpty() {
        return a.doubleValue() > b.doubleValue();
    }
}
