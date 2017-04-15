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
 * there are a fuzzy set which domain is a {@link java.awt.image.BufferedImage};
 * on the basis of this fuzzy set, for each pixel of the source image, the
 * membership of the subimage (window) centered on that pixel is calculated and
 * stored as result. The output is a grey level image where a white value means
 * membership degree of 1.0, while a black value means membership degree of 0.0.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FastTiledFuzzyMappingOp extends BufferedImageOpAdapter{
    /**
     * Fuzzy set associated to this operator.
     */
    private FuzzySet<BufferedImage> fuzzyset;
    /**
     * The width of the tile in pixels.
     */
    private int tileWidth=1;
    /**
     * The height of the tile in pixels.
     */
    private int tileHeight=1;
    /**
     * The maximum grey level value.
     */
    static final private int MAX_LEVEL = 255;
    
    /**
     * Constructs a new fuzzy mapping operator with a tile size of 1x1.
     * 
     * @param fuzzyset the fuzzy set of this fuzzy operator.
     * 
     */
    public FastTiledFuzzyMappingOp(FuzzySet<BufferedImage> fuzzyset){
        this(fuzzyset,1,1);
    }
    
    /**
     * Constructs a new fuzzy mapping operator.
     * 
     * @param fuzzyset the fuzzy set of this fuzzy operator.
     * @param tileWidth the width of the tile in pixels.
     * @param tileHeight the height of the tile in pixels.
     */
    public FastTiledFuzzyMappingOp(FuzzySet<BufferedImage> fuzzyset, int tileWidth, int tileHeight){
        this.setFuzzySet(fuzzyset);
        this.setTileSize(tileWidth, tileHeight);       
    }
    
    /**
     * Set the size of the tile.
     * 
     * @param tileWidth the width of the tile in pixels. It must be a value 
     * greater than 1 (if not, it is set automatically to 1). 
     * @param tileHeight the height of the tile in pixels. It must be a value 
     * greater than 1 (if not, it is set automatically to 1).
     */
    public final void setTileSize(int tileWidth, int tileHeight){
        this.tileWidth = Math.max(1,tileWidth);
        this.tileHeight = Math.max(1,tileWidth);
    } 
    
    /**
     * Returns the width of the tile in pixels.
     * 
     * @return the width of the tile.
     */
    public int getTileWidth(){
        return tileWidth;
        
    }
    
    /**
     * Returns the height of the tile in pixels.
     * 
     * @return the height of the tile.
     */
    public int getTileHeight(){
        return tileHeight;
        
    }
    
    /**
     * Set the fuzzy set of this mapping operator.
     * 
     * @param fuzzyset the new fuzzy set of this mapping operator.
     * @throws NullPointerException if the fuzzy set is null.
     */
    public final void setFuzzySet(FuzzySet<BufferedImage> fuzzyset){
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
    public FuzzySet<BufferedImage> getFuzzySet(){
        return this.fuzzyset;
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
        
        int in_width = Math.max(0,src.getWidth()-tileWidth)+1;
        int in_height = Math.max(0,src.getHeight()-tileHeight)+1;
        int dx = (int)tileWidth/2;
        int dy = (int)tileHeight/2;     
        double degree;
        BufferedImage subimage;
        // The fuzzy filtering is applied pixel by pixel 
        for (int x = 0; x < in_width; x++) {
            for (int y = 0; y < in_height; y++) {
                subimage = src.getSubimage(x, y, tileWidth, tileHeight);
                degree = fuzzyset.membershipDegree(subimage);
                destRaster.setSample(x+dx, y+dy, 0, (byte)(degree*MAX_LEVEL));                
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
}
