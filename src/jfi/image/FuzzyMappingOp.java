package jfi.image;

import jfi.image.color.GreyColorSpace;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import jfi.fuzzy.FuzzySet;

/**
 * Class representing a fuzzy filtering on an image. Associated to this operator
 * there are a fuzzy set which domain is a <tt>T</tt>; on the basis of this
 * fuzzy set, for each pixel of the source image, a membership is calculated and
 * stored as result.
 *
 * In the mapping process, it is necessary to iterate over the image pixels and,
 * in each iteration, an object of type <tt>T</tt> (the domain of the fuzzy set)
 * must be provided in order to calculate the membership degree associated to
 * the pixel. Depending on the filter, this object could be a three-dimesional
 * point representing a color (RGB, for example), a subimage centered in the
 * pixel (for example, for texture analysis), a region, etc. Therefore, when a
 * {@link jfi.image.FuzzyMappingOp} filter is constructed, it is needed to
 * provide a {@link jfi.image.BufferedImageIterator}, which implements both (1)
 * the iteration rules and (2) the way a <tt>T</tt> object is calculated at a
 * given location.
 *
 * The output is a grey level image where a white value means membership degree
 * of 1.0, while a black value means membership degree of 0.0
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
     * Performs the single-input/single-output operation on an image. For each
     * pixel of the source image, a window of tile size and centered on that 
     * pixel is analyzed (if tile size is greater than image size, an single
     * tile of image size is considered).
     * 
     * @param src the image to be filtered
     * @param dest image in which to store the results
     *
     * @return the filtered image.
     */
    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
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
            dest = createCompatibleDestImage(src, null);
        }
        WritableRaster destRaster = dest.getRaster();
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
            System.out.println("Error: "+ex);
        }
        
        if(savdest!=null){ 
            dest = this.copyImage(dest,savdest);
        }
        return dest;
    }
    
    /**
     * Creates a zeroed grey level destination image with the same size of the
     * source one. It has a {@link java.awt.image.ComponentColorModel} with a
     * {@link jfi.image.GreyColorSpace} color space and only one band. Note that
     * it does not use the default {@link java.awt.color.ColorSpace#CS_GRAY}  
     * color space to avoid its internal transformation to/from the CIEXYZ  
     * (and its darkering of the image).
     *
     * @param src the image to be filtered.
     * @param destCM color model of the destination (not used).
     *
     * @return the zeroed destination image.
     */
    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) { 
      int[] nBits = {8};
      ColorModel cm = new ComponentColorModel(new GreyColorSpace(), nBits, 
                        false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE); 
      WritableRaster wr = cm.createCompatibleWritableRaster(src.getWidth(), src.getHeight());
      BufferedImage out = new BufferedImage(cm,wr,false,null);   
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
