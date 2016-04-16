package jfi.utils;

/**
 * Class representing a line
 * 
 * @author Luis Suárez Lloréns
 */
public class JFILine {
    private double a;
    private double b;
    private double c;

    /**
     * Constructs an empty line
     */
    public JFILine(){    
    }

    /**
     * Constructs the line "a*x + b*y + c =0"
     * 
     * @param a
     * @param b
     * @param c 
     */
    public JFILine(double a, double b, double c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getA(){
        return a;
    }

    public double getB(){
        return b;
    }

    public double getC(){
        return c;
    }

    public void setA(double a){
        this.a = a;
    }

    public void setB(double b){
        this.b = b;
    }

    public void setC(double c){
        this.c = c;
    }
}
