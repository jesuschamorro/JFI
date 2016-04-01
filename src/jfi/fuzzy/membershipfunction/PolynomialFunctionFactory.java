package jfi.fuzzy.membershipfunction;

/**
 *
 * @author Jesús Chamorro Martínez <jesus@decsai.ugr.es>
 */
public class PolynomialFunctionFactory {
    
    public static final int TYPE_COARSENESS_AMADASUN = 1;
    public static final int TYPE_COARSENESS_CORRELATION = 2;
    public static final int TYPE_COARSENESS_AMADASUN_CORRELATION = 3;
    
    /**
     * Create a new polynomial membership function
     * 
     * @param type type of polynomial membership function
     * 
     * @return A new instance of PolynomialFunction1D
     */
    public static PolynomialFunction getInstance(int type){

        switch (type) {
        case TYPE_COARSENESS_AMADASUN:
            return getAmadasunInstance();
        case TYPE_COARSENESS_CORRELATION:
            return getCorrelationInstance();
        case TYPE_COARSENESS_AMADASUN_CORRELATION:
            return getAmadasunCorrelationInstance();    
        default:
            return null;
        }
    }
    
    /**
     * Create a new polynomial membership function for the Amadasun texture 
     * measure.For this purpouse, the coeficients proposed in <cite> <\cite> 
     * are used by default
     * 
     * @return a default polynomial membership function for Amadasun measure
     */
    private static PolynomialFunction1D getAmadasunInstance(){
        double alpha = 0.1727;
        double beta = 0.5858;
        double coeficients[] = {1.8707, -6.4835, 9.4901, -6.6128};
        return new PolynomialFunction1D(coeficients,alpha,beta);
    }
    
    /**
     * Create a new polynomial membership function for the Correlation texture 
     * measure. For this purpouse, the coeficients proposed in <cite> <\cite> 
     * are used by default
     * 
     * @return a default polynomial membership function for Correlation measure
     */
     private static PolynomialFunction1D getCorrelationInstance(){
        double alpha = 0.0301;
        double beta = 0.7711;
        double coeficients[] = {1.0486, -1.7013, 2.9961, -3.3110};
        return new PolynomialFunction1D(coeficients,alpha,beta);
    }
     
    /**
     * Create a new polynomial membership function of two variables for the 
     * Amadasun and Correlation texture measures. 
     * For this purpouse, the coeficients proposed in <cite> <\cite> are used 
     * by default
     * 
     * @return a default polynomial membership function of two variables for the 
     * Amadasun and Correlation texture measures
     */
     private static PolynomialFunction2D getAmadasunCorrelationInstance(){
        double coeficients[] = {-12.156, -27.351, 15.194, 1.5584, 51.149, -4.5174, -0.6475, 0.0, -38.064, 0.4175};
        return new PolynomialFunction2D(coeficients);
    } 
     
}
