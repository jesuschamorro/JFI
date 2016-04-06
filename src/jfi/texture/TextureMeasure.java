package jfi.texture;

import java.awt.image.BufferedImage;
import java.util.function.Function;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public interface TextureMeasure extends Function<BufferedImage, Double>{
    /**
     * Applies the texture measure to the given image.

     * @param image the function argument 
     * @return the texture measure
     */
    @Override
    public Double apply(BufferedImage image);
}
