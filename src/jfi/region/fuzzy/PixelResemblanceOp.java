
package jfi.region.fuzzy;

import java.awt.Point;
import java.awt.image.BufferedImage;
import jfi.fuzzy.FuzzySet;
import jfi.fuzzy.operators.TNorm;

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
     * Flag associated to the Goguen implication operator.
     */
    public final static int GOGUEN_IMPLICATION_TYPE = 1;
    /**
     * Flag associated to the Lukasiewicz implication operator.
     */
    public final static int LUKASIEWICZ_IMPLICATION_TYPE = 2;
    /**
     * The default implication operator.
     */
    public static int DEFAULT_IMPLICATION_TYPE = GOGUEN_IMPLICATION_TYPE;

    /**
     * Applies this resemblance function to the given arguments.
     *
     * @param t the coordinates of the first pixel 
     * @param u the coordinates of the second pixel 
     * @param image the image associated to the pixel coordinates
     * @return the function result
     */
    Double apply(T t, T u, BufferedImage image);
    
    /**
     * Calculates the resemblance between two fuzzy sets. By default, the
     * resemblance is calculated a 1.0 if boths fuzzy sets are the same, 0.0 in
     * other case.
     *
     * @param fs1 first fuzzy set.
     * @param fs2 second fuzzy set.
     * @return
     */
    default double resemblanceFuzzySet(FuzzySet fs1, FuzzySet fs2) {
        return fs1.equals(fs2) ? 1.0 : 0.0;
    }
    
    /**
     * Fuzzy implication operator. It measures the degree of 'a->b'
     *
     * @param a firts degree in 'a->b'
     * @param b second degree in 'a->b'
     * @param type type of implication operator.
     * @return the 'a->b' implication degree.
     */
    default double implication(double a, double b, int type) {
        switch (type) {
            case GOGUEN_IMPLICATION_TYPE:
                return a <= b ? 1 : b / a;
            case LUKASIEWICZ_IMPLICATION_TYPE:
                return Math.min(1.0, 1.0 - a + b);
        }
        return 0.0;
    }
    
    /**
     * Fuzzy inclusion operator. It measures the degree in which 'a/fa' is
     * included in 'b/fb'.
     *
     * @param a first degree, which is asociated to the fuzzy set 'fsa'.
     * @param fsa first fuzzy set.
     * @param b second degree, which is asociated to the fuzzy set 'fsb'.
     * @param fsb seconf fuzzy set.
     * @param tnorm t-norm used to aggegate the resemblance between fsa and fsb,
     * and the implication 'a->b'
     * @return
     */
    default double inclusion(double a, FuzzySet fsa, double b, FuzzySet fsb, TNorm tnorm){
        return tnorm.apply(this.implication(a, b, DEFAULT_IMPLICATION_TYPE),
                           this.resemblanceFuzzySet(fsa,fsb));
    }
}
