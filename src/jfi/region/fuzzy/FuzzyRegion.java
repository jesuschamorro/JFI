package jfi.region.fuzzy;

import java.awt.image.BufferedImage;
import jfi.image.fuzzy.FuzzyImage;

/**
 * A class representing an image fuzzy region. In a fuzzy region, each pixel of
 * the source image has a membership degree to that region. For this reason, a
 * fuzzy region can be modeled as a {@link jfi.image.fuzzy.FuzzyImage} where the
 * membership degree represents the degree to which the pixel belongs to the
 * region.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */

public class FuzzyRegion extends FuzzyImage{
    /**
     * Source image.
     */ 
    private BufferedImage source = null;
    
    /**
     * Constructs a new fuzzy region associated to the given image (and with the
     * same size). By default, the membership degrees are initialized to 0.0.
     *
     * @param src the source image.
     */
    public FuzzyRegion(BufferedImage src) {
        // By default in the super call, if the source image is a gray one, a 
        // fuzzy image of type TYPE_FUZZY_ALPHA_GREY is created; else, a 
        // TYPE_FUZZY_ALPHA_RGB one is constructed.
        super(src);  
        //The source is also stored as part of the region (the membership degrees
        //are located in the alpha channel; in any case, the original one is 
        //assigned to the variable 'source'
        this.source = src;
        //By default, the membership degrees are set to 0.0
        fillDegreeData(0.0);
    }
    
    /**
     * Constructs a new fuzzy region with the given size using the default image
     * type {@link #TYPE_FUZZY_NOALPHA_GREY}. The source image is set to
     * <code>null</code> and the membership degrees are initialized to 0.0.
     *
     * to the given image. By default, the membership degrees are initialized to
     * 1.0
     *
     * @param width the width of the image region.
     * @param height the height of the image region.
     */
    public FuzzyRegion(int width, int height) {
        // By default in the super call, a fuzzy image of type 
        // TYPE_FUZZY_NOALPHA_GREY is created 
        super(width, height);
        this.source = null;
        fillDegreeData(0.0);
    }
    
    /**
     * Returns the image associated to this region model.
     * 
     * @return the image associated to this fuzzy region.
     */
    public BufferedImage getImageSource() {
        return source; 
    }
}
