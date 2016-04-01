package jfi.texture.fuzzy;

import java.awt.image.BufferedImage;
import jfi.fuzzy.FunctionBasedFuzzySet;



public class FuzzyTexture extends FunctionBasedFuzzySet<BufferedImage> {

    /**
     *
     * @param label
     * @param mfunction
     */
    public FuzzyTexture(String label, TextureMembershipFunction mfunction) {
        super(label, mfunction);
    }

    /**
     *
     * @param mfunction
     */
    public FuzzyTexture(TextureMembershipFunction mfunction) {
        super(mfunction);
    }
}