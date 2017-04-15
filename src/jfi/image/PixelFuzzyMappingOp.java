package jfi.image;

import jfi.fuzzy.FuzzySet;
import jfi.geometry.Point3D;
import jfi.image.BufferedImageIterator;
import jfi.image.FuzzyMappingOp;

/**
 * Class representing a fuzzy filtering on an image where the domain of the 
 * fuzzy set is a three-dimensional point. 
 * 
 * @see jfi.image.FuzzyMappingOp
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class PixelFuzzyMappingOp extends FuzzyMappingOp<Point3D>{
    /**
     * Constructs a new fuzzy mapping operator.
     * 
     * @param fuzzyset the fuzzy set of this fuzzy operator.
     */
    public PixelFuzzyMappingOp(FuzzySet<Point3D> fuzzyset){
        super(fuzzyset, new BufferedImageIterator.Pixel(null));
    }
}
