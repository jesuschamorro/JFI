package jfi.texture.fuzzy;

import jfi.fuzzy.membershipfunction.PolynomialFunction1D;
import jfi.fuzzy.membershipfunction.PolynomialFunction2D;
import jfi.texture.AmadasunCoarsenessMeasure;
import jfi.texture.CorrelationCoarsenessMeasure;
import jfi.texture.TextureMeasure;

/**
 *
 * @author Jesús Chamorro Martínez <jesus@decsai.ugr.es>
 */
public class TextureMembershipFunctionFactory {
    
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
    public static TextureMembershipFunction getInstance(int type){

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
    private static TextureMembershipFunction getAmadasunInstance(){
        double alpha = 0.1727;
        double beta = 0.5858;
        double coeficients[] = {1.8707, -6.4835, 9.4901, -6.6128};
        PolynomialFunction1D mfunction = new PolynomialFunction1D(coeficients,alpha,beta);
        TextureMeasure measure = new AmadasunCoarsenessMeasure();
        return new TextureMembershipFunction(mfunction,measure);
    }
    
    /**
     * Create a new polynomial membership function for the Correlation texture 
     * measure. For this purpouse, the coeficients proposed in <cite> <\cite> 
     * are used by default
     * 
     * @return a default polynomial membership function for Correlation measure
     */
     private static TextureMembershipFunction getCorrelationInstance(){
        double alpha = 0.0301;
        double beta = 0.7711;
        double coeficients[] = {1.0486, -1.7013, 2.9961, -3.3110};
        PolynomialFunction1D mfunction = new PolynomialFunction1D(coeficients,alpha,beta);
        TextureMeasure measure = new CorrelationCoarsenessMeasure();
        return new TextureMembershipFunction(mfunction,measure);
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
     private static TextureMembershipFunction getAmadasunCorrelationInstance(){
        double coeficients[] = {-12.156, -27.351, 15.194, 1.5584, 51.149, -4.5174, -0.6475, 0.0, -38.064, 0.4175};
        PolynomialFunction2D mfunction = new PolynomialFunction2D(coeficients);
        TextureMeasure measure1 = new AmadasunCoarsenessMeasure();
        TextureMeasure measure2 = new CorrelationCoarsenessMeasure();
        return new TextureMembershipFunction(mfunction, measure1, measure2);
    } 
     
}
