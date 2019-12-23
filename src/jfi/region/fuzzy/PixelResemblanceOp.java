
package jfi.region.fuzzy;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Represents a fuzzy resemblance of two pixels (the arguments) within an image. 
 *
 * @param <T> the type of the argument descriptors
 *
 * * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
@FunctionalInterface 
public interface PixelResemblanceOp<T extends Point> {
    /**
     * Applies this resemblance function to the given arguments.
     *
     * @param t the coordinates of the first pixel 
     * @param u the coordinates of the second pixel 
     * @param image the image associated to the pixel coordinates
     * @return the function result
     */
    Double apply(T t, T u, BufferedImage image);
}
