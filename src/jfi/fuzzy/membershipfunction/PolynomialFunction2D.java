package jfi.fuzzy.membershipfunction;

import java.awt.geom.Point2D;
import java.security.InvalidParameterException;

/**
 * Class representing an bidimensional polynomial-based membership function. It 
 * depends on the 
 * set of coefficients of the polynomial function, as given by:
 * <br><br>
 * <pre>
 *                         | 0, poly2n(x,y)&lt;0
 * f(x,y;a(n!+n),...,a0) = | poly2n(x,y), 0&lt;poly2n(x,y)&lt;1 
 *                         | 1, poly2n(x,y)&gt;1
 * </pre>
 * 
 * with 'poly2n' being a polynomial function:
 * <br><br>
 * <pre>
 * poly2n(x,y;a(n!+n),...,a0) = SUMi(SUMj( a(i!+j)·x^j·y^(i-j) )) 
 * </pre>
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class PolynomialFunction2D implements PolynomialFunctionInterface<Point2D.Double>{
    /**
     * The degree of the polynomial
     */
    private int polynomial_degree;
    /**
     * The set of polynomial coefficients.
     */
    private double coeficients[];
    
    /**
     * Constructs a polynominal membership function of two variables
     * 
     * @param coeficients the coeficients of the polynominal function. They 
     * should be provided in the order a0, a1, ..., an.
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
        if(!setPolynomialDegree(coeficients)){
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
     * @param coeficients the set of coeficients
     * @return <tt>true<tt> the number of coeficients is correct and the setting is done
     */
    private boolean setPolynomialDegree(double coeficients[]){
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
    
    /**
     * Applies this membership function to the given argument.
     * 
     * @param point the function argument.
     * @return the function result.
     */
    @Override
    public Double apply(Point2D.Double point) {
        Double output = polynomial(point.x,point.y);
        if(output<0) return 0.0;
        if(output>1) return 1.0;
        return output;
    }
    
    /**
     * Applies the polynomial function to the given arguments.
     * 
     * @param x the  x-argument.
     * @param y the  y-argument.
     * @return the function result.
     */
    private double polynomial(Double x, Double y){
        double result = 0.0;
        int coeff_index = 0;
        for(int i=0; i<=polynomial_degree; i++){
            for(int j=0; j<=i; j++){
                //Pendiente: chequear desbordamiento
                result += coeficients[coeff_index]*Math.pow(x,j)*Math.pow(y,i-j);
                coeff_index++;
            }
        }
        return result;
    }
    
}
