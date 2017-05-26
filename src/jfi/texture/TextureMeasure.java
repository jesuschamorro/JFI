package jfi.texture;

import java.awt.image.BufferedImage;
import java.util.function.Function;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 * @param <T> the domain of the measure 
 */
@FunctionalInterface
public interface TextureMeasure<T> extends Function<BufferedImage, T>{
    /**
     * Applies the texture measure to the given image.

     * @param image the function argument 
     * @return the texture measure
     */
    @Override
    public T apply(BufferedImage image);
}
