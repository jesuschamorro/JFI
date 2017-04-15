package jfi.image;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.NoSuchElementException;
import jfi.fuzzy.FuzzySet;

/**
 * Class representing a fuzzy filtering on an image where the domain of the
 * fuzzy set is a subimage.
 *
 * Associated to this operator there are a fuzzy set which domain is a
 * {@link java.awt.image.BufferedImage}; on the basis of this fuzzy set, for
 * each pixel of the source image, the membership of the subimage (window)
 * centered on that pixel is calculated and stored as result. 
 * 
 * @see jfi.image.FuzzyMappingOp
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class TiledFuzzyMappingOp extends FuzzyMappingOp<BufferedImage>{    
    /**
     * The width of the tile in pixels.
     */
    private int tileWidth;
    /**
     * The height of the tile in pixels.
     */
    private int tileHeight;
    
    /**
     * Constructs a new fuzzy mapping operator with a tile size of 1x1.
     * 
     * @param fuzzyset the fuzzy set of this fuzzy operator.
     * 
     */
    public TiledFuzzyMappingOp(FuzzySet<BufferedImage> fuzzyset){
        this(fuzzyset,1,1);
    }
    
    /**
     * Constructs a new fuzzy mapping operator.
     * 
     * @param fuzzyset the fuzzy set of this fuzzy operator.
     * @param tileWidth the width of the tile in pixels.
     * @param tileHeight the height of the tile in pixels.
     */
    public TiledFuzzyMappingOp(FuzzySet<BufferedImage> fuzzyset, int tileWidth, int tileHeight){
        //super(fuzzyset, new TileProducer(tileWidth, tileHeight));BufferedImageIterator
        super(fuzzyset, new BufferedImageIterator.Tile(null,tileWidth, tileHeight));
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
        //((TileProducer)producer).setTileSize(this.tileWidth, this.tileHeight);
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
     * Inner class representing a producer of subimages from an image. 
     */
    static private class TileProducer implements PixelDataProducer<BufferedImage>  {
        /**
         * The width of the tile in pixels.
         */
        private int tileWidth;
        /**
         * The height of the tile in pixels.
         */
        private int tileHeight;
        /**
         * Displacement between the center of the tile and its top-left corner.
         */
        private int dx, dy;
        
        /**
         * Constructs a new tile producer.
         * 
         * @param tileWidth
         * @param tileHeight 
         */
        public TileProducer(int tileWidth, int tileHeight) {
            this.setTileSize(tileWidth, tileHeight);
        }

        /**
         * Set the size of the tile.
         *
         * @param tileWidth the width of the tile in pixels. It must be a value
         * greater than 1 (if not, it is set automatically to 1).
         * @param tileHeight the height of the tile in pixels. It must be a
         * value greater than 1 (if not, it is set automatically to 1).
         */
        public final void setTileSize(int tileWidth, int tileHeight) {
            this.tileWidth = Math.max(1, tileWidth);
            this.tileHeight = Math.max(1, tileWidth);
            dx = (int)tileWidth/2;
            dy = (int)tileHeight/2;
        }
        
        /**
         * Returns a subimage (the domain of the fuzzy set associated to the
         * fuzzy mapping) from the given image at the location (x,y).
         *
         * @param source the source image.
         * @param x the x-coordinate of the pixel.
         * @param y the x-coordinate of the pixel.
         * @return a subimage at the given location, <code>null</code> if the
         * location is out of bounds.
         */
        @Override
        public BufferedImage get(BufferedImage source, int x, int y) { 
            try{
                return source.getSubimage(x-dx, y-dy, tileWidth, tileHeight);
            } catch(Exception ex){
                return null;
            }
            
        }        

        
        private int length=0;
        
        int in_width, in_height;
        
        
        private int pos = 0,x,y;
        private BufferedImage source=null;
//        
//        
//        public TileProducer(BufferedImage img) {
//            source = img;
//            height = img.getHeight();
//            width = img.getWidth();
//            length = height * width;
//        }
        
        
        @Override
        public void setImage(BufferedImage image) {
            this.source = image;
                     
            in_width = Math.max(0,image.getWidth()-tileWidth)+1;
            in_height = Math.max(0,image.getHeight()-tileHeight)+1;
            length = in_height * in_width;
            
            pos = 0;
        }
        
        
        @Override
        public boolean hasNext() {
            return (pos < length);
        }

        @Override
        public BufferedImage next() {

            if (pos >= length) {
                throw new NoSuchElementException("No more pixels");
            }
            x = (pos % in_width);
            y = (pos / in_width);
            pos++;

            return source.getSubimage(x, y, tileWidth, tileHeight);
        }

        @Override
        public int getX() {
            return x+dx;
        }

        @Override
        public int getY() {
            return y+dy;
        }

        
    }
}
