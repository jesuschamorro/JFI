package jfi.image;

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
 * stored as result. The output is a grey level image where a white value means
 * membership degree of 1.0, while a black value means membership degree of 0.0.
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
     * A producer of <tt>T</tt> objects from an image (needed in the mapping
     * process)
     * 
     * See {@link jfi.image.FuzzyMappingOp.ItemMappingProducer} 
     */   
    protected PixelDataProducer<T> producer;       
    /**
     * The maximum grey level value.
     */
    static final private int MAX_LEVEL = 255;
    
    /**
     * Constructs a new fuzzy mapping operator.
     * 
     * @param fuzzyset the fuzzy set of this fuzzy operator.
     * @param producer the <tt>T</tt> object producer.
     */
    public FuzzyMappingOp(FuzzySet<T> fuzzyset, PixelDataProducer<T> producer){
        this.setFuzzySet(fuzzyset);
        this.setPixelDataProducer(producer);
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
     * Set the pixel data producer of this mapping operator.
     * 
     * @param producer the new pixel data producer set of this mapping operator.
     * @throws NullPointerException if the producer is null.
     */
    public final void setPixelDataProducer(PixelDataProducer<T> producer){
        if (producer == null) {
            throw new NullPointerException("Producer is null");
        }
        this.producer = producer;
    }
    
    /**
     * Returns the pixel data producer of this mapping operator.
     * 
     * @return the pixel data producer of this mapping operator.
     */
    public PixelDataProducer<T> getPixelDataProducer(){
        return this.producer;
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
        
        int width = src.getWidth();
        int height = src.getHeight();             
        double degree;
        T item;
        // The fuzzy filtering is applied pixel by pixel
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                item = producer.get(src, x, y);
                degree = item!=null ? fuzzyset.membershipDegree(item) : 0.0;
                destRaster.setSample(x, y, 0, (byte)(degree*MAX_LEVEL));                
            }
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
    
    /**
     * Inner interface representing a producer of <tt>T</tt> objects from an
     * image pixel. In the mapping process, an object of type <tt>T</tt> (the
     * domain of the fuzzy set) must be provided for each pixel in order to
     * calculate the membership degree associated to that pixel. Depending on
     * the filter, this object could be a three-dimesional point representing a
     * color (RGB, for example), a subimage centered in the pixel (for example,
     * for texture analysis), a region, etc. Therefore, when a
     * {@link jfi.image.FuzzyMappingOp} filter is constructed, it is needed to
     * provide a {@link jfi.image.FuzzyMappingOp.ItemMappingProducer}, which
     * implements the way a <tt>T</tt> object is calculated from a given image
     * at a given location.
     *
     * @param <T> the domain of the fuzzy set associated to the operator.
     */
    public interface PixelDataProducer<T> {
        /**
         * Returns and object of type <tt>T</tt> (the domain of the fuzzy set
         * associated to the fuzzy mapping) from the given image at the location
         * (x,y).
         *
         * @param source the source image.
         * @param x the x-coordinate of the pixel.
         * @param y the x-coordinate of the pixel.
         * @return an object of the type of the domain of the fuzzy set 
         * associated to the fuzzy mapping, <code>null</code> if not available.
         */
        public T get(BufferedImage source, int x, int y);
    }
}
