package jfi.region.fuzzy;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import jfi.color.ISCCColorMap;
import jfi.color.fuzzy.FuzzyColor;
import jfi.color.fuzzy.FuzzyColorSpace;
import jfi.geometry.Point3D;
import jfi.image.fuzzy.FuzzyMappingOp;
import jfi.image.fuzzy.PixelFuzzyMappingOp;
import java.awt.Color;

/**
 * Class representing a pixel resemblance operator where the resemblance is
 * calculated on the basis of fuzzy colors.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class ColorResemblanceOp implements PixelResemblanceOp<Point> {  
    /**
     * Fuzzy color space associated to this operator.
     */
    private FuzzyColorSpace<Point3D> fcs;
    /**
     * A source image associated to this operator (if available).
     */
    private BufferedImage source = null;   
    /**
     * List of image maps.
     */
    private ArrayList<BufferedImage> maps = null;
    
    /**
     * Constructs a new color resemblance operator using as fuzzy color space
     * the one given by parameter. The given image will be associated to this
     * operator; this implies that a series of preliminary calculations will be
     * made in order to make the application of this operator more efficient if
     * the image on which it is applied is the one passed in this constructor.
     *
     * @param source image associated to this operator.
     * @param fcs the fuzzy color space
     */
    public ColorResemblanceOp(BufferedImage source, FuzzyColorSpace fcs) {
        this.fcs = fcs;
        this.source = source;
        if(source!=null) this.sourceMapping(); //The texture maps are calculated
    }    
    
    /**
     * Constructs a new color resemblance operator using as fuzzy color space
     * the one given by parameter.
     *
     * @param fcs the fuzzy color space
     */
    public ColorResemblanceOp(FuzzyColorSpace fcs) {
        this(null,fcs);
    }
     
    /**
     * Constructs a new color resemblance operator using as default fuzzy color
     * space the sphere based one with the basic ISCC set as prototypes. The
     * given image will be associated to this operator; this implies that a
     * series of preliminary calculations will be made in order to make the
     * application of this operator more efficient if the image on which it is
     * applied is the one passed in this constructor.
     *
     * @param source image associated to this operator.
     */
    public ColorResemblanceOp(BufferedImage source) {
        this(source, FuzzyColorSpace.Factory.createSphereBasedFCS(new ISCCColorMap(ISCCColorMap.TYPE_BASIC), 0.5));
    }
    
    /**
     * Constructs a new color resemblance operator using as default fuzzy color
     * space the sphere based one with the basic ISCC set as prototypes..
     */
    public ColorResemblanceOp(){       
        this((BufferedImage)null);
    }
        
    /**
     * Calculates the mappings of the source image. 
     */
    private void sourceMapping() {
        if (this.maps == null) {
            this.maps = new ArrayList();
        } else {
            maps.clear();
        }
        FuzzyMappingOp mapColor;
        for (FuzzyColor fc : fcs) {
            mapColor = new PixelFuzzyMappingOp(fc);
            maps.add(mapColor.filter(this.source, null, false));
        }
    }
    
    /**
     * Apply this resemblance operator.
     * 
     * @param t the coordinates of the first pixel. 
     * @param u the coordinates of the second pixel.
     * @param image the image associated to the pixel coordinates.
     * @return the resemblance result.
     */   
    @Override
    public Double apply(Point t, Point u, BufferedImage image) {
        //If the image is the one associated to this operator, the faster apply
        //method is used (in that case, the precalculated degrees will be used)
        if(image == source ){
            return apply(t,u);
        }
        Color ct = new Color(image.getRGB(t.x, t.y));
        Color cu = new Color(image.getRGB(u.x, u.y));

        //The resemblance is calculated on the basis of the color fuzzy sets. 
        //It is assumed that the resemblance between two fuzzy sets is 1.0 if
        //and only if they are the same set, 0.0 in other case. This assumption
        //simplifies the resemblance calculus
        double degreeT, degreeU;
        double resemblanceTU, resemblance = 0.0;
        for (FuzzyColor fc : fcs) {
            degreeT = fc.membershipDegree(ct);
            degreeU = fc.membershipDegree(cu);

            //Author´s proposal for the case of fuzzy colors
            resemblanceTU = (degreeU > 0.25 && degreeT > 0.25) ? 1.0 - Math.abs(degreeU - degreeT) : 0.0;
            resemblance = Math.max(resemblance, resemblanceTU);
        }
        return resemblance;
    }

    /**
     * Apply this resemblance operator to the associated image (if available).
     * 
     * @param t the coordinates of the first pixel. 
     * @param u the coordinates of the second pixel. 
     * @return the resemblance result. 
     * @throws NullPointerException if there is not image associated to this operator.
     */
    public Double apply(Point t, Point u) {
        if (this.source == null) {
            throw new NullPointerException("There is not image associated to this operator");
        }
        //Since the membership degrees are precalculated, we just need to access
        //to the degree values in each fuzzy texture map.
        double degreeT, degreeU;
        double resemblanceTU, resemblance = 0.0;
        for(BufferedImage img: this.maps){
            degreeT = ((double)img.getRaster().getSample(t.x,t.y,0)) / 255.0;
            degreeU = ((double)img.getRaster().getSample(u.x,u.y,0)) / 255.0;
            //See the comments  about resemblance calculus in the previous apply method
            resemblanceTU = (degreeU > 0.25 && degreeT > 0.25) ? 1.0 - Math.abs(degreeU - degreeT) : 0.0;
            resemblance = Math.max(resemblance, resemblanceTU);
        }        
        return resemblance;
    }
    
}
