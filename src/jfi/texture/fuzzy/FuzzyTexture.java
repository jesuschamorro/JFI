package jfi.texture.fuzzy;

import java.awt.image.BufferedImage;
import jfi.fuzzy.FunctionBasedFuzzySet;



public class FuzzyTexture extends FunctionBasedFuzzySet<BufferedImage> {

    /**
     *
     * @param label the label associated to the fuzzy set.
     * @param mfunction the membership function associated to the fuzzy set.
     */
    public FuzzyTexture(String label, TextureMembershipFunction mfunction) {
        super(label, mfunction);
    }

    /**
     *
     * @param mfunction the membership function associated to the fuzzy set.
     */
    public FuzzyTexture(TextureMembershipFunction mfunction) {
        super(mfunction);
    }
}