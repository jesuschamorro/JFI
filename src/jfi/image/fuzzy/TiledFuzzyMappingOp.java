package jfi.image.fuzzy;

import java.awt.image.BufferedImage;
import jfi.fuzzy.FuzzySet;
import jfi.image.BufferedImageIterator;

/**
 * Class representing a fuzzy filtering on an image where the domain of the
 * fuzzy set is a subimage.
 *
 * <p>
 * Associated to this operator there are a fuzzy set which domain is a
 * {@link java.awt.image.BufferedImage}; on the basis of this fuzzy set, for
 * each pixel of the source image, the membership of the subimage (window)
 * centered on that pixel is calculated and stored as result. 
 * 
 * @see jfi.image.fuzzy.FuzzyMappingOp
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class TiledFuzzyMappingOp extends FuzzyMappingOp<BufferedImage>{    
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
        super(fuzzyset, new BufferedImageIterator.Tile(null,tileWidth, tileHeight));
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
        ((BufferedImageIterator.Tile)iterator).
                setTileSize(Math.max(1,tileWidth),Math.max(1,tileHeight));
    } 
    
    /**
     * Returns the width of the tile in pixels.
     * 
     * @return the width of the tile.
     */
    public int getTileWidth(){
        return ((BufferedImageIterator.Tile)iterator).getTileWidth();
    }
    
    /**
     * Returns the height of the tile in pixels.
     * 
     * @return the height of the tile.
     */
    public int getTileHeight(){
        return ((BufferedImageIterator.Tile)iterator).getTileHeight();        
    }
}
