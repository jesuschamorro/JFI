package jfi.utils;

/**
 * Class representing a line
 * 
 * @author Luis Suárez Lloréns
 */
public class JFILine {
    /**
     * Coefficient 'a' in the general form of a line equation.
     */
    private double a;
    /**
     * Coefficient 'b' in the general form of a line equation.
     */
    private double b;
    /**
     * Coefficient 'c' in the general form of a line equation.
     */
    private double c;

    /**
     * Constructs the line "a*x + b*y + c =0"
     * 
     * @param a coefficient 'a'
     * @param b coefficient 'b'
     * @param c coefficient 'c'
     */
    public JFILine(double a, double b, double c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Returns the coefficient 'a' of this line.
     * 
     * @return the coefficient 'a' of this line
     */
    public double getA(){
        return a;
    }

    /**
     * Returns the coefficient 'b' of this line.
     * 
     * @return the coefficient 'b' of this line
     */
    public double getB(){
        return b;
    }

    /**
     * Returns the coefficient 'c' of this line.
     * 
     * @return the coefficient 'c' of this line
     */
    public double getC(){
        return c;
    }

    /**
     * Set the coefficient 'a' of this line.
     * 
     * @param a the new coefficient 'a'
     */
    public void setA(double a){
        this.a = a;
    }

    /**
     * Set the coefficient 'b' of this line.
     * 
     * @param b the new coefficient 'b'
     */
    public void setB(double b){
        this.b = b;
    }

    /**
     * Set the coefficient 'c' of this line.
     * 
     * @param c the new coefficient 'c'
     */
    public void setC(double c){
        this.c = c;
    }
}
