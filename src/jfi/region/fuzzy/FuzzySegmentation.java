package jfi.region.fuzzy;

import java.awt.Point;
import java.util.Collection;
import jfi.fuzzy.FuzzySetCollection;

/**
 * Class representing a fuzzy segmentation output, that is, a colletion of fuzzy
 * regions.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzySegmentation extends FuzzySetCollection<FuzzyRegion,Point>{
    /**
     * Constructs an empty segmentation (that is, without fuzzy regions).
     */
    public FuzzySegmentation() {
        super();
    }
    
    /**
     * Constructs a new fuzzy segmentation object containing the fuzzy regions
     * of the specified collection.
     *
     * @param fuzzyRegions the collection of fuzzy regions.
     * @throws NullPointerException if the specified collection is null.
     */
    public FuzzySegmentation(Collection<FuzzyRegion> fuzzyRegions) {
        super(fuzzyRegions);
    }
}
