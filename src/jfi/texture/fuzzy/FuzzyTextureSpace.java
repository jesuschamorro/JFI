package jfi.texture.fuzzy;

import java.awt.image.BufferedImage;
import java.util.Collection;
import jfi.fuzzy.FuzzySetCollection;

/**
 * A fuzzy texture space represented as a finite collection of fuzzy textures.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzyTextureSpace extends FuzzySetCollection<FuzzyTexture,BufferedImage>{
     /**
     * Constructs an empty fuzzy texture space.
     */
    public FuzzyTextureSpace() {
        super();
    }

    /**
     * Constructs a new fuzzy color space containing the fuzzy colors of the 
     * specified collection.
     *
     * @param fuzzyTextures the collection of fuzzy colors to be placed into this 
     * fuzzy color space.
     * @throws NullPointerException if the specified collection is null.
     */
    public FuzzyTextureSpace(Collection<FuzzyTexture> fuzzyTextures) {
        super(fuzzyTextures);
    }

    /**
     * Constructs a new fuzzy color space containing the fuzzy colors of the 
     * specified list of arguments.
     *
     * @param fuzzyTextures the list of fuzzy colors to be placed into this fuzzy 
     * color space.
     */
    public FuzzyTextureSpace(FuzzyTexture...fuzzyTextures) {
        super();
        for(FuzzyTexture ft: fuzzyTextures){
            this.add(ft);
        }
    }
    
    
    /**
     * Class for generating fuzyy texture spaces.
     */
    static public class Factory{
        
        /**
         * Creates a default fuzzy texture space. 
         * @return a default fuzzy texture space.
         */
        static public FuzzyTextureSpace getDefault(){                       
            return null;
        }
        
        static public FuzzyTextureSpace getDefault2(){                       
            FuzzyTextureSpace tSpace = new FuzzyTextureSpace();
            
            
            TextureMembershipFunction mfunction = null;
            FuzzyTexture ft = new FuzzyTexture(mfunction);
            tSpace.add(ft);
            
            return tSpace;
        }
        
    } // End of factory
    
}
