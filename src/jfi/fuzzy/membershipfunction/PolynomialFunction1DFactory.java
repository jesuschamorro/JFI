package jfi.fuzzy.membershipfunction;

/**
 *
 * @author Jesús Chamorro
 */
public class PolynomialFunction1DFactory {
    
    public static final int TYPE_AMADASUN = 1;
    public static final int TYPE_CORRELATION = 2;
    
    /**
     * Create a new polynomial membership function
     * 
     * @param type type of polynomial membership function
     * 
     * @return A new instance of PolynomialFunction1D
     */
    public static PolynomialFunction1D getInstance(int type){

        switch (type) {
        case TYPE_AMADASUN:
            return getAmadasunInstance();
        case TYPE_CORRELATION:
            return getCorrelationInstance();
        default:
            return null;
        }
    }
    
    /**
     * Create a new polynomial membership function based on Amadasun measure. 
     * For this purpouse, the coeficients proposed in <cite> <\cite> are used 
     * by default
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
     * Create a new polynomial membership function based on Correlation measure. 
     * For this purpouse, the coeficients proposed in <cite> <\cite> are used 
     * by default
     * 
     * @return a default polynomial membership function for Correlation measure
     */
     private static PolynomialFunction1D getCorrelationInstance(){
        double alpha = 0.0301;
        double beta = 0.7711;
        double coeficients[] = {1.0486, -1.7013, 2.9961, -3.3110};
        return new PolynomialFunction1D(coeficients,alpha,beta);
    }
}
