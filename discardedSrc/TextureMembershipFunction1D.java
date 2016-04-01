package jfi.texture.fuzzy;

import java.awt.image.BufferedImage;
import jfi.fuzzy.membershipfunction.PolynomialFunction1D;
import jfi.texture.TextureMeasure;
import jfi.utils.Interval;

/**
 *
 * @author Jesús Chamorro Martínez <jesus@decsai.ugr.es>
 */
public class TextureMembershipFunction1D implements TextureMembershipFunction{
    TextureMeasure measure=null;
    PolynomialFunction1D mfunction;
    
    public TextureMembershipFunction1D(double coeficients[], double alpha, double beta, TextureMeasure measure){
        mfunction = new PolynomialFunction1D(coeficients,alpha,beta);
        this.measure = measure;
    }
    
    @Override
    public Double apply(BufferedImage image) {
        Double m1_value = measure.apply(image);
        return mfunction.apply(m1_value);
    }

    @Override
    public Interval<Number> getAlphaCut(double alpha) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
