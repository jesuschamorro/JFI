package jfi.texture.fuzzy;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import jfi.fuzzy.membershipfunction.MembershipFunction;
import jfi.fuzzy.membershipfunction.PolynomialFunction;
import jfi.fuzzy.membershipfunction.PolynomialFunction1D;
import jfi.fuzzy.membershipfunction.PolynomialFunction2D;
import jfi.texture.TextureMeasure;
import jfi.utils.Interval;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class TextureMembershipFunction implements MembershipFunction<BufferedImage>{
    TextureMeasure measure1=null, measure2=null, measures[];
    PolynomialFunction mfunction;
    
    
    public TextureMembershipFunction(PolynomialFunction1D mfunction, TextureMeasure measure){
        this.mfunction = mfunction;
        this.measure1 = measure;
        this.measure2 = null;
    }
    
    public TextureMembershipFunction(PolynomialFunction2D mfunction, TextureMeasure measure1, TextureMeasure measure2){
        this.mfunction = mfunction;
        this.measure1 = measure1;
        this.measure2 = measure2;
    }
    
//    public TextureMembershipFunction(PolynomialFunction mfunction, TextureMeasure measures[]){
//        this.mfunction = mfunction;
//        this.measures = measures;
//    }
   
    
    @Override
    public Double apply(BufferedImage image) {
        
        
//        for(TextureMeasure measure : measures) {
//            measure.apply(image);
//        }
//        mfunction.apply(this);
        
        Double m1_value, m2_value, output;
        m1_value = measure1.apply(image);
        m2_value = measure2!=null? measure2.apply(image):null;
        if(measure2==null) // 1D case
            output = ((PolynomialFunction1D)mfunction).apply(m1_value);
        else  // 2D case
            output = ((PolynomialFunction2D)mfunction).apply(new Point2D.Double(m1_value,m2_value));
        return output;
    }

    @Override
    public Interval<Number> getAlphaCut(double alpha) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
