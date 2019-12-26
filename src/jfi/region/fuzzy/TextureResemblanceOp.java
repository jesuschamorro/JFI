package jfi.region.fuzzy;

import java.awt.Point;
import java.awt.image.BufferedImage;
import jfi.texture.fuzzy.FuzzyTexture;
import jfi.texture.fuzzy.FuzzyTextureFactory;

/**
 *
 * @author Jesús
 */
public class TextureResemblanceOp implements PixelResemblanceOp<Point> {

    FuzzyTexture ftF;
    private BufferedImage source = null;
    private BufferedImage CoarsenessMap = null;
    
    public TextureResemblanceOp(BufferedImage source){
        this();
        this.source = source;
        
        //Calcular mapeos
        // CoarsenessMap = 
    }
    
    public TextureResemblanceOp(){
        ftF = FuzzyTextureFactory.getInstance(FuzzyTextureFactory.TYPE_COARSENESS_AMADASUN);
    }
    
    @Override
    public Double apply(Point t, Point u, BufferedImage image) {
        
        if(image == source ){
            return apply(t,u);
        }
        
        BufferedImage imt = image.getSubimage(0, 0, 0, 0);
        BufferedImage imu = image.getSubimage(0, 0, 0, 0);
        
        double degreeF_T = ftF.membershipDegree(imt);
        double degreeF_U = ftF.membershipDegree(imu);
        
        return degreeF_T-degreeF_U;
    }
    
    public Double apply(Point t, Point u) {
        return 1.0;
    }
}
