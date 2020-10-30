package jfi.texture.fuzzy;

import java.awt.image.BufferedImage;
import jfi.fuzzy.membershipfunction.MembershipFunction;
import jfi.fuzzy.membershipfunction.PolynomialFunction;
import jfi.fuzzy.membershipfunction.TrapezoidalFunction;
import jfi.texture.TextureMeasure;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class TextureMembershipFunction implements MembershipFunction<BufferedImage>{
    TextureMeasure<Double> measure1=null, measure2=null, measures[];
    PolynomialFunction mfunction;
    
    
    public TextureMembershipFunction(PolynomialFunction mfunction, TextureMeasure measure){
        this.mfunction = mfunction;
        this.measure1 = measure;
        this.measure2 = null;
    }
    
    public TextureMembershipFunction(PolynomialFunction mfunction, TextureMeasure measure1, TextureMeasure measure2){
        this.mfunction = mfunction;
        this.measure1 = measure1;
        this.measure2 = measure2;
    }

    
    public TextureMembershipFunction(TrapezoidalFunction mfunction, TextureMeasure measure1, TextureMeasure measure2){
        //this.mfunction = mfunction;
        this.measure1 = measure1;
        this.measure2 = measure2;
    }
    
    @Override
    public Double apply(BufferedImage image) {
        Double m1_value, m2_value, output;
        m1_value = measure1.apply(image);
        m2_value = measure2!=null? measure2.apply(image):null;
        if(measure2==null) // 1D case
            output = ((PolynomialFunction)mfunction).apply(m1_value);
        else  // 2D case
            output = ((PolynomialFunction)mfunction).apply(m1_value,m2_value);
        return output;
    }
    
    public Double apply(Double measureValue) {
        return ((PolynomialFunction)mfunction).apply(measureValue);
    }
    

}
