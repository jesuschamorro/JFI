package jfi.texture.fuzzy;

import java.awt.image.BufferedImage;
import jfi.fuzzy.membershipfunction.MembershipFunction;

/**
 *
 * @author Jesús Chamorro Martínez <jesus@decsai.ugr.es>
 */
public interface TextureMembershipFunction extends MembershipFunction<BufferedImage>{

    /**
     *
     * @param image
     * @return
     */
    @Override
    public Double apply(BufferedImage image);
}
