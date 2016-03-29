package jfi.texture.fuzzy;

import java.awt.image.BufferedImage;
import jfi.fuzzy.ContinuousFuzzySet;
import jfi.fuzzy.membershipfunction.MembershipFunction;
import jfi.texture.TextureMeasure;

/**
 *
 * @author Jesús Chamorro
 */

/*
    Pendiente: adaptación a n-dimensiones
*/

public class FuzzyTexture extends ContinuousFuzzySet<Double>{
    protected TextureMeasure measure;
    
    public FuzzyTexture(String label, MembershipFunction<Double> mfunction, TextureMeasure measure) {
        super(label, mfunction);
        this.measure = measure;
    }
    
    public double getMembershipValue(BufferedImage image) {
        double measure_value = measure.apply(image);
        return mfunction.apply(measure_value);
    }
    
}
