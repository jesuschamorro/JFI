package jfi.fuzzy.membershipfunction;

import java.security.InvalidParameterException;
import java.util.List;
import jfi.utils.JFIMath;
import jfi.utils.MultivariableFunction;

/**
 * Polynomial-based membership function.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class PolynomialFunction implements MembershipFunction<List<Double>> {

    /**
     * The degree of the polynomial
     */
    protected int polynomial_degree;
    /**
     * The dimension of the polynomial
     */
    protected int dimension;
    /**
     * The set of polynomial coefficients.
     */
    protected double coefficients[];
    /**
     * Polynomial function associated to this membership function.
     */
    protected MultivariableFunction<Double, Double> polynomial;
    /**
     * Default dimension.
     */
    public static int DEFAULT_DIMENSION = 1;

    /**
     * Constructs a polynominal-based membership function on the basis of a set
     * of coefficients. From the set of coefficients, the polynomial degree is
     * infered.
     *
     * @param coefficients the coeficients of the polynominal function. They
     * should be provided in the order a0, a1, ..., an.
     * @param dimension the dimension of the polynominal function. It must be
     * greater than or equal to 1.
     */
    public PolynomialFunction(double coefficients[], int dimension) {
        setDimension(dimension);
        setCoefficients(coefficients); //It also set the degree
        //A polynomial must be created for this dimension
        MultivariableFunction polynomial = createPolynomial(this.dimension);
        if (polynomial != null) {
            setPolynomial(polynomial);
        } else {
            throw new UnsupportedOperationException("Dimension not supported.");
        }
    }

    /**
     * Constructs a polynominal-based membership function on the basis of a set
     * of coefficients using the default dimension {@link #DEFAULT_DIMENSION}.
     * From the set of coefficients, the polynomial degree is infered.
     *
     * @param coefficients the coeficients of the polynominal function. They
     * should be provided in the order a0, a1, ..., an.
     */
    public PolynomialFunction(double coefficients[]) {
        this(coefficients, DEFAULT_DIMENSION);
    }
    
    /**
     * Constructs a polynominal-based membership function on the basis of a
     * sequence of coefficients using the default dimension
     * {@link #DEFAULT_DIMENSION}. From the sequence of coefficients, the
     * polynomial degree is infered.
     *
     * @param c1 the first element of the sequence.
     * @param c2toN the rest of element in the sequence.
     */
    public PolynomialFunction(double c1, double... c2toN) {
        this(mergeArrays(c1,c2toN));
        //To call the previous constructor, a static method for merging
        //arrays is needed; that method is defined below
    }
    private static double[] mergeArrays(double c1, double... c2toN){
        double coefficients[] = new double[c2toN.length+1];
        coefficients[0]=c1;
        System.arraycopy(c2toN,0,coefficients,1,c2toN.length);
        return coefficients;
    }
    
    /**
     * Constructs a polynominal-based membership function on the basis of a
     * multivariate function representing a polynomial. It is assumed that the
     * degree and dimension are consistent with the given multivariate function
     * (if not, it is not relevant for membership calculation, which only
     * depends on the polynomial, but will affect to the answer given by the
     * methods {@link #getDimension() and {@link #getPolynomialDegree()}}.
     * 
     * @param degree the degree of the polynomial.
     * @param dimension the dimension (number of variables) of the polynominal.
     * @param polynomial the polynomial. 
     */
    public PolynomialFunction(int degree, int dimension, MultivariableFunction<Double, Double> polynomial) {
        setDimension(dimension);
        this.polynomial_degree = degree;
        this.setPolynomial(polynomial);
        //If the polynomial is given, the coefficients are not necessary 
        this.coefficients = null;
    }

    /**
     * Returns the degree of the polynomial.
     *
     * @return the degree of the polynomial.
     */
    public int getPolynomialDegree() {
        return this.polynomial_degree;
    }

    /**
     * Returns the dimension of the polynomial.
     *
     * @return the dimension of the polynomial.
     */
    public int getDimension() {
        return this.dimension;
    }

    /**
     * Returns the coeficients of the polynomial-based function.
     *
     * @return the coeficients of the polynomial-based function.
     */
    public double[] getCoefficients() {
        return this.coefficients;
    }

    /**
     * Set the dimension of this function.
     *
     * @param dimension the new dimension of this function.
     */
    private void setDimension(int dimension) {
        if (dimension < 1) {
            throw new InvalidParameterException("Dimension must be greater than 0.");
        }
        this.dimension = dimension;
    }

    /**
     * Set new coefficients for this polynominal-based function. On the basis of
     * the given coeficients, this method set the degree of the polynomial-based
     * function.
     *
     * @param coefficients the new coeficients.
     */
    public final void setCoefficients(double coefficients[]) {
        if (coefficients == null || coefficients.length == 0) {
            throw new InvalidParameterException("Empty coeficient set.");
        }
        if (!setPolynomialDegree(coefficients, this.dimension)) {
            //Coefficients must be consistent with the degree and the dimension 
            throw new InvalidParameterException("Invalid number of coeficients.");
        }
        this.coefficients = coefficients;
    }

    /**
     * Set the degree of the polynominal function based on the number of
     * coeficients. The number of coeficients should be ( d+n n), with (a b)
     * being the binomial coefficient "n choose k", 'n' being the polynomial
     * degree, and 'd' the dimension (if not, the setting is not done)
     *
     * @param coeficients the set of coeficients.
     * @param dimension the polynominal dimension.
     * @return <tt>true<tt> the number of coeficients is correct and the setting
     * is done
     */
    private boolean setPolynomialDegree(double coeficients[], int dimension) {
        int n = 0;
        long numCoeff = 1;  // Binomial coefficient of (x 0)

        while (numCoeff < coeficients.length) {
            n++;
            numCoeff = JFIMath.binomialCoefficient(n + dimension, n);
        }
        boolean found = numCoeff == coeficients.length;
        if (found) {
            this.polynomial_degree = n;
        }
        return found;
    }

    /**
     * Creates a polynomial for the given dimension. Only the unidimensional and
     * bidimensional cases are supported.
     *
     * @param dimension the number or variables of the polynomial.
     * @return a polynomial for the given dimension (or <t>null<t> if the
     * dimension is not supported.
     */
    private MultivariableFunction<Double, Double> createPolynomial(int dimension) {
        MultivariableFunction<Double, Double> polynomial;
        switch (dimension) {
            case 1:
                polynomial = new Polynomial1D();
                break;
            case 2:
                polynomial = new Polynomial2D();
                break;
            default:
                polynomial = null;
        }
        return polynomial;
    }

    /**
     * Set the multivariate function associated to the polynomial.
     *
     * @param polynomial the polynomial function.
     */
    private void setPolynomial(MultivariableFunction<Double, Double> polynomial) {
        if (polynomial == null) {
            throw new InvalidParameterException("Null polynomial.");
        }
        this.polynomial = polynomial;
    }

    /**
     * Applies this membership function to the given vector. If the vector size
     * is greater than the dimension of this function, the extra elements are
     * ignored; if the vector size is smaller than the dimension of this
     * function, an exception will be thrown.
     *
     * @param x the input vector as a list.
     * @return the function result.
     * 
     * @throws IndexOutOfBoundsException if the sequence size is smaller than
     * this function dimension.
     */
    @Override
    public Double apply(List<Double> x) {
        if (x.isEmpty()) {
            throw new InvalidParameterException("Empty list of arguments.");
        }
        Double x1 = x.get(0);
        List<Double> x2toN = x.subList(1, x.size());
        return apply(x1, (Double[]) x2toN.toArray());
    }

    /**
     * Applies this membership function to the given vector expressed as a
     * sequence of numbers. If the sequence size is greater than the dimension
     * of this function, the extra elements are ignored; if the sequence size is
     * smaller than the dimension of this function, an exception will be thrown.
     *
     * @param x1 the first element of the sequence.
     * @param x2toN the rest of element in the sequence.
     * @return the function result.
     *
     * @throws IndexOutOfBoundsException if the sequence size is smaller than
     * this function dimension.
     */
    public Double apply(Double x1, Double... x2toN) {
        Double output = polynomial.apply(x1, x2toN);
        if (output < 0) return 0.0;      
        if (output > 1) return 1.0;
        return output;
    }

    /**
     * Inner class representing an unidimensional polynomial-based function. It
     * depends on the set of coefficients of the polynomial function, as given
     * by:
     * <br><br>
     * <pre>
     * polyn(x;an,...,a0) = an·x^n +...+a1·x^1 + a0
     * </pre>
     *
     * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
     */
    class Polynomial1D implements MultivariableFunction<Double, Double> {
        /**
         * {@inheritDoc}
         */
        @Override
        public Double apply(Double x1, Double... x2toN) {
            double result = 0.0;
            for (int n = 0; n <= polynomial_degree; n++) {
                result += coefficients[n] * Math.pow(x1, n);
            }
            return result;
        }
    }

    /**
     * Inner class representing an bidimensional polynomial-based function. It
     * depends on the set of coefficients of the polynomial function, as given
     * by:
     * <br><br>
     * <pre>
     * poly2n(x,y;a(n!+n),...,a0) = SUMi(SUMj( a(i!+j)·x^j·y^(i-j) ))
     * </pre>
     *
     * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
     */
    class Polynomial2D implements MultivariableFunction<Double, Double> {
        /**
         * {@inheritDoc}
         */
        @Override
        public Double apply(Double x1, Double... x2toN) {
            double result = 0.0;
            double x2 = x2toN[0];
            int coeff_index = 0;
            for (int i = 0; i <= polynomial_degree; i++) {
                for (int j = 0; j <= i; j++) {
                    result += coefficients[coeff_index] * Math.pow(x1, j) * Math.pow(x2, i - j);
                    coeff_index++;
                }
            }
            return result;
        }
    }

}
