package jfi.image.fuzzy;

import jfi.color.GreyColorSpace;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import jfi.fuzzy.FuzzySet;
import jfi.image.BufferedImageIterator;
import jfi.image.BufferedImageOpAdapter;

/**
 * Class representing a fuzzy filtering on an image. Associated to this operator
 * there are a fuzzy set which domain is a <tt>T</tt>; on the basis of this
 * fuzzy set, for each pixel of the source image, a membership is calculated and
 * stored as result.
 * 
 * <p>
 * In the mapping process, it is necessary to iterate over the image pixels and,
 * in each iteration, an object of type <tt>T</tt> (the domain of the fuzzy set)
 * must be provided in order to calculate the membership degree associated to
 * the pixel. Depending on the filter, this object could be a three-dimesional
 * point representing a color (RGB, for example), a subimage centered in the
 * pixel (for example, for texture analysis), a region, etc. Therefore, when a
 * {@link jfi.image.fuzzy.FuzzyMappingOp} filter is constructed, it is needed to
 * provide a {@link jfi.image.BufferedImageIterator}, which implements both (1)
 * the iteration rules and (2) the way a <tt>T</tt> object is calculated at a
 * given location.
 *
 * <p>
 * By default, the output is a grey level image where a white value means
 * membership degree of 1.0, while a black value means membership degree of 0.0.
 * Nevertheless, this operator allows to retain the original color values by
 * means a parameter in the filter method; in that case, the alpha component
 * will be used for storing the membership degree: the pixel transparency will
 * be proportional to the degree of that pixel, being opaque if the membership
 * degree is 1.0, and full transparent if it is 0.0.
 *
 *
 * @param <T> the domain of the fuzzy set associated to this operator.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzyMappingOp<T> extends BufferedImageOpAdapter{
    /**
     * Fuzzy set associated to this operator.
     */
    protected FuzzySet<T> fuzzyset;
    /**
     * Iterator over the image. In each iteration, it produces an object of 
     * type <tt>T</tt> (needed in the mapping process).
     * 
     * See {@link jfi.image.BufferedImageIterator} 
     */   
    protected BufferedImageIterator<T> iterator;
    /**
     * The maximum grey level value.
     */
    static final private int MAX_LEVEL = 255;
    
    /**
     * Constructs a new fuzzy mapping operator.
     * 
     * @param fuzzyset the fuzzy set of this fuzzy mapping operator.
     * @param iterator the iterator over the image. In each iteration, it must 
     * produce an object of type <tt>T</tt>.
     */
    public FuzzyMappingOp(FuzzySet<T> fuzzyset, BufferedImageIterator<T> iterator){
        this.setFuzzySet(fuzzyset);
        this.setIterator(iterator);
    }
    
    /**
     * Set the fuzzy set of this mapping operator.
     * 
     * @param fuzzyset the new fuzzy set of this mapping operator.
     * @throws NullPointerException if the fuzzy set is null.
     */
    public final void setFuzzySet(FuzzySet<T> fuzzyset){
        if (fuzzyset == null) {
            throw new NullPointerException("Fuzzy set is null");
        }
        this.fuzzyset = fuzzyset;
    }
    
    /**
     * Returns the fuzzy set of this mapping operator.
     * 
     * @return the fuzzy set of this mapping operator.
     */
    public FuzzySet<T> getFuzzySet(){
        return this.fuzzyset;
    }
    
    /**
     * Set the image iterator of this mapping operator.
     *
     * @param iterator iterator over the image.
     * @throws NullPointerException if the iterator is null.
     */
    public final void setIterator(BufferedImageIterator<T> iterator){
        if (iterator == null) {
            throw new NullPointerException("Iterator is null");
        }
        this.iterator = iterator;
    }
    
    /**
     * Returns the image iterator of this mapping operator.
     * 
     * @return the image  of this mapping operator.
     */
    public BufferedImageIterator<T> getIterator(){
        return this.iterator;
    }
    
    /**
     * Performs the single-input/single-output fuzzy mapping operation on an
     * image. By default, the output is a grey level image where a white value
     * means membership degree of 1.0, while a black value means membership
     * degree of 0.0.
     *
     * @param src the image to be filtered.
     * @param dest image in which to store the results.
     *
     * @return the filtered image.
     */
    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest){
        return filter(src,dest,false);
    }
    
    /**
     * Performs the single-input/single-output fuzzy mapping operation on an
     * image, allowing to mantain the original color values. If the original
     * values are retained, the alpha component will be used for storing the
     * membership degree: the pixel transparency will be proportional to the
     * degree of that pixel, being opaque if the membership degree is 1.0, and
     * full transparent if it is 0.0; if not, a grey level image will be
     * generated as output, where the grey level will be proportional to the
     * degree of that pixel, being black (grey level 0) if the membership degree
     * is 1.0, and white (grey level 255) if it is 0.0.
     *
     * @param src the image to be filtered.
     * @param dest image in which to store the results.
     * @param originalColors if <tt>true</tt>, the original color values are
     * retained.
     *
     * @return the filtered image.
     */
    public BufferedImage filter(BufferedImage src, BufferedImage dest, boolean originalColors) {
        if (src == null) {
            throw new NullPointerException("Source image is null");
        }
        // Setting the destination raster for filtering
        BufferedImage savdest = null;
        if(dest!=null && dest.getType()!=BufferedImage.TYPE_BYTE_GRAY) {
            savdest = dest; //Grey level image is needed
            dest = null;
        }
        if (dest == null) {
            //dest = createCompatibleDestImage(src, null);
            dest = createCompatibleDestImage(src, null, originalColors);
        }
        WritableRaster destRaster = originalColors?dest.getAlphaRaster():dest.getRaster();
        // The fuzzy filtering is applied pixel by pixel using the iterator
        try {
            double degree;
            T item;
            iterator.setImage(src);
            while (iterator.hasNext()) {
                item = (T) iterator.next();
                degree = fuzzyset.membershipDegree(item);
                destRaster.setSample(iterator.getX(), iterator.getY(), 0, 
                                    (byte)(degree * MAX_LEVEL));
            }
        } catch (Exception ex) {
            System.err.println("Error in fuzzy filtering: "+ex);
        }
        
        if(savdest!=null){ 
            dest = this.copyImage(dest,savdest);
        }
        return dest;
    }
    
    /**
     * Creates a a destination image with the same size of the source one. By
     * default, a zeroed grey level image is created.
     *
     * @param src the image to be filtered.
     * @param destCM color model of the destination (not used).
     *
     * @return the zeroed destination image.
     */
    @Override
     public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM){
         return createCompatibleDestImage(src, destCM, false);
     }
     
    /**
     * Creates a destination image with the same size of the source one. If the
     * original colors want to be retained, an image of type
     * {@link java.awt.image.BufferedImage#TYPE_INT_ARGB} is created and filled
     * with the original color values (the alpha component will be used for
     * storing the membership degrees); if not, a zeroed grey level is created
     * with a {@link java.awt.image.ComponentColorModel} color model (only one
     * band) and a {@link jfi.color.GreyColorSpace} color space. Note that it
     * does not use the default {@link java.awt.color.ColorSpace#CS_GRAY} color
     * space to avoid its internal transformation to/from the CIEXYZ (and its
     * darkering of the image).
     *
     * @param src the image to be filtered.
     * @param destCM color model of the destination (not used).
     * @param originalColors if <tt>true</tt>, the original color values are
     * retained.
     *
     * @return the destination image.
     */ 
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM, boolean originalColors) { 
        BufferedImage out;
        if (originalColors) {
            out = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
            out.createGraphics().drawImage(src, 0, 0, null);
        } else {
            int[] nBits = {8};
            ColorModel cm = new ComponentColorModel(new GreyColorSpace(), nBits,
                    false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
            WritableRaster wr = cm.createCompatibleWritableRaster(src.getWidth(), src.getHeight());
            out = new BufferedImage(cm, wr, false, null);
        }
        return out;
    }
    
    /**
     * Copy the source image to the destination one.
     * 
     * @param srcImage source image (not null)
     * @param dstImage destination image (not null)
     * @return 
     */
    private BufferedImage copyImage(BufferedImage srcImage, BufferedImage dstImage) {
        java.awt.Graphics2D big = dstImage.createGraphics();
        try {
            big.drawImage(srcImage, 0, 0, null);
        } finally {
            big.dispose();
        }
        return dstImage;
    }    
}
