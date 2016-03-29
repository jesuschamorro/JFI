package jfi.texture.fuzzy;

import jfi.fuzzy.membershipfunction.MembershipFunction;
import jfi.fuzzy.membershipfunction.PolynomialFunction1DFactory;
import jfi.texture.AmadasunCoarsenessMeasure;
import jfi.texture.TextureMeasure;

/**
 *
 * @author Jesús Chamorro
 */
public class FuzzyTextureFactory {
    public static final int TYPE_COARSENESS_AMADASUN = 1;
    public static final int TYPE_COARSENESS_CORRELATION = 2;
    
    
    public static FuzzyTexture getInstance(int type){

        switch (type) {
        case TYPE_COARSENESS_AMADASUN:
            return getCoarsenessAmadasunInstance();
        default:
            return null;
        }
    }
    
    private static FuzzyTexture getCoarsenessAmadasunInstance(){
        MembershipFunction<Double> mfunction = PolynomialFunction1DFactory.getInstance(TYPE_COARSENESS_AMADASUN);
        TextureMeasure measure = new AmadasunCoarsenessMeasure();
        
        return new FuzzyTexture("Texture.Coarseness.Amadasun.1D", mfunction, measure);
    }
}
