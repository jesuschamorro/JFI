package jfi.texture.fuzzy;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import jfi.fuzzy.membershipfunction.PolynomialFunction2D;
import jfi.texture.TextureMeasure;
import jfi.utils.Interval;

/**
 *
 * @author Jesús Chamorro Martínez <jesus@decsai.ugr.es>
 */
public class TextureMembershipFunction2D implements TextureMembershipFunction{
    TextureMeasure measure1=null, measure2=null;
    PolynomialFunction2D mfunction;
    
    public TextureMembershipFunction2D(double coeficients[], TextureMeasure measure1, TextureMeasure measure2){
        mfunction = new PolynomialFunction2D(coeficients);
        this.measure1 = measure1;
        this.measure2 = measure2;
    }
    
    @Override
    public Double apply(BufferedImage image) {
        Double m1_value = measure1.apply(image);
        Double m2_value = measure2.apply(image);
        return mfunction.apply(new Point2D.Double(m1_value,m2_value));
    }

    @Override
    public Interval<Number> getAlphaCut(double alpha) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
