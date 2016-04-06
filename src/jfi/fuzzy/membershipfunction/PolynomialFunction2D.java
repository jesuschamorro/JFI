package jfi.fuzzy.membershipfunction;

import java.awt.geom.Point2D;
import java.security.InvalidParameterException;
import jfi.utils.Interval;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class PolynomialFunction2D implements PolynomialFunction<Point2D.Double>{
    private int polynomial_degree;
    private double coeficients[];
    
    /**
     * Constructs a polynominal membership function of two variables
     * 
     * @param coeficients the coeficients of the polynominal function
     */
    public PolynomialFunction2D(double coeficients[]){
        this.setCoeficients(coeficients);
    }
    
    /**
     * Set new coeficients for this polynominal function
     * 
     * @param coeficients the new coeficients
     */
    @Override
    public final void setCoeficients(double coeficients[]){
        if(coeficients==null || coeficients.length==0){
            throw new InvalidParameterException("Empty coeficient set.");
        }
        if(!setPolynomialDegree()){
            throw new InvalidParameterException("Invalid number of coeficients. The number of coeficients should be (n!+n), with 'n' being the polynomial degree.");
        }
        this.coeficients = coeficients;
    }
    
    /**
     * Return the coeficients of the polynomial
     *
     * @return the coeficients of the polynomial
     */
    @Override
    public double[] getCoeficients() {
        return this.coeficients;
    }
    
    /**
     * Set the degree of the polinominal function based on the number of coeficients.
     * The number of coeficients should be (n!+n), with 'n' being the polynomial degree 
     * (if not, the setting is not done)
     * 
     * @return <tt>true<tt> the number of coeficients is correct and the setting is done
     */
    private boolean setPolynomialDegree(){
        int n = 1;
        long factorial = 1;    
        //Pendiente: chequear desbordamiento
        while (factorial+n < coeficients.length-1) {
            n++;
            factorial *= n;
        }
        boolean found = factorial+n == coeficients.length-1;
        if(found)
            this.polynomial_degree = n;
        return found;
    }
    
    /**
     * Return the degree of the polynomial
     *
     * @return the degree of the polynomial
     */
    @Override
    public int getPolynomialDegree() {
        return this.polynomial_degree;
    }
    
    @Override
    public Double apply(Point2D.Double point) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    @Override
    public Interval<Number> getAlphaCut(double alpha) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
