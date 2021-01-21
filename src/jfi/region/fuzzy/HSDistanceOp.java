package jfi.region.fuzzy;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.color.ColorSpace;
import jfi.color.HSIColorSpace;

/**
 * Class representing a pixel resemblance operator where the resemblance is
 * calculated on the basis of HSL distance.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class HSDistanceOp implements PixelResemblanceOp<Point> {
    /**
     * A source image associated to this operator (if available).
     */
    private BufferedImage source = null; 
    /**
     * The source image transformed to the HSL color space.
     */
    private BufferedImage source_hsl = null;
    /**
     * Flag to set if the chromatic areas are used to calculate the distance.
     */
    private boolean useChromaticAreas = true;
    /**
     * Threshold to delimit the semichromatic area
     */
    private static final float THRESHOLD_TS = 0.2f*255.0f; //In [0,255]
    /**
     * Threshold to delimit the achromatic area
     */
    private static final float THRESHOLD_TI = 60f; //In [0,255]
    /**
     * Constant equals to square root of three
     */
    private static final double SQRT3 = Math.sqrt(3);
    /**
     * Constant equals to square root of two
     */
    private static final double SQRT2 = Math.sqrt(2);
    /**
     * Number PI normalizez to [0,255] with 255 being 2PI
     */
    private static final double PI_255 = 127.0;
    /**
     * Number 2PI normalizez to [0,255] with 255 being 2PI
     */
    private static final double PI2_255 = 255.0;
    

    /**
     * Constructs a new HS-based resemblance operator.
     * 
     * @param source source image.
     */
    public HSDistanceOp(BufferedImage source) {
        this(source,new HSIColorSpace(),true);
    }
    
    /**
     * Constructs a new HS-based resemblance operator.
     *
     * @param source source image.
     * @param cs a color space based on hue and saturation (HSI, HSV, HSL)
     * @param useChromaticAreas <tt>true</tt> if the chromatic areas want to be
     * used in the distance calculos, <tt>false</tt> for a standard euclidean
     * distance between components.
     */
    public HSDistanceOp(BufferedImage source, ColorSpace cs, boolean useChromaticAreas) {
        if (cs.getType() != ColorSpace.TYPE_HLS && cs.getType() != ColorSpace.TYPE_HSV) {
            throw new IllegalArgumentException("The color space must be a HSI/HSL/HSV color space");
        }
        this.source = source;
        this.useChromaticAreas = useChromaticAreas;
        if (source != null) {
            //The source image is transformed to HS*
            jfi.color.ColorConvertOp op = new jfi.color.ColorConvertOp(cs, null);
            source_hsl = op.filter(source, null, false); //XYZ is not used
        }
    }
    
 
    /**
     * Apply this pixel resemblance operator. By default, the distance is
     * calculated taking into account the chromatic areas.
     *
     * @param t first point.
     * @param u second point.
     * @param image the image, thah must be the same associted to this operator
     * (the one used in the construction of this object).
     * @return the resemblance between pixels.
     */
    @Override
    public Double apply(Point t, Point u, BufferedImage image) {
        if (this.source_hsl == null) {
            throw new NullPointerException("There is not image associated to this operator");
        }
        if (this.source != image) {
            throw new IllegalArgumentException("The given image is not the source image associated to this operator");
        }
        return useChromaticAreas ? applyUsingChromaticAreas(t,u) : applyEuclidean(t,u);
    }

    /**
     * Apply this pixel resemblance operator on the basis of the eucliden
     * distance without taking into account the chromatic areas.
     *
     * @param t first point.
     * @param u second point.
     * @return the resemblance between pixels.
     */
    private Double applyEuclidean(Point t, Point u) { 
        //HSL values are in [0,255]
        int ht = source_hsl.getRaster().getSample(t.x,t.y,0);
        int st = source_hsl.getRaster().getSample(t.x,t.y,1);
        int lt = source_hsl.getRaster().getSample(t.x,t.y,2);
        int hu = source_hsl.getRaster().getSample(u.x,u.y,0);
        int su = source_hsl.getRaster().getSample(u.x,u.y,1);
        int lu = source_hsl.getRaster().getSample(u.x,u.y,2);           
        //Differences for each component normalized in [0,1]        
        double hDif = Math.abs(ht - hu);
        hDif = hDif <= PI_255 ? hDif / PI_255 : (PI2_255 - hDif) / PI_255; //Circular case                
        double sDif = Math.abs(st - su) / 255.0;
        double lDif = Math.abs(lt - lu) / 255.0;
        double dif = SQRT3 * Math.sqrt(Math.pow(hDif, 2) + Math.pow(sDif, 2) + Math.pow(lDif, 2));
        //The resemblance is calculated on the basis of the distance (in [0,1])
        return 1.0 - (Math.min(1.0, dif ));
    }
    
    /**
     * Apply this pixel resemblance operator taking into account the chromatic 
     * areas. 
     *
     * @param t first point.
     * @param u second point.
     * @return the resemblance between pixels.
     */
    private Double applyUsingChromaticAreas(Point t, Point u) {   
        //HSL values are in [0,255]
        int ht = source_hsl.getRaster().getSample(t.x,t.y,0);
        int st = source_hsl.getRaster().getSample(t.x,t.y,1);
        int lt = source_hsl.getRaster().getSample(t.x,t.y,2);
        int hu = source_hsl.getRaster().getSample(u.x,u.y,0);
        int su = source_hsl.getRaster().getSample(u.x,u.y,1);
        int lu = source_hsl.getRaster().getSample(u.x,u.y,2);                      
        //The distance is calculated on the basis of the chormatic area. The
        //differences are normalized to [0,1]
        double dif;
        if(isAchormatic(ht,st,lt) || isAchormatic(ht,st,lt)){
            dif = Math.abs(lt-lu)/255.0;
        } else {
            double sDif = Math.abs(st - su) / 255.0;
            double lDif = Math.abs(lt - lu) / 255.0;
            if (isChormatic(ht, st, lt) && isChormatic(ht, st, lt)) {
                double hDif = Math.abs(ht - hu);
                hDif = hDif <= PI_255 ? hDif / PI_255 : (PI2_255 - hDif) / PI_255; //Circular case                
                dif = SQRT3 * Math.sqrt(Math.pow(hDif, 2) + Math.pow(sDif, 2) + Math.pow(lDif, 2));
            } else {
                dif = SQRT2 * Math.sqrt(Math.pow(sDif, 2) + Math.pow(lDif, 2));
            }
        }
        //The resemblance is calculated on the basis of the distance (in [0,1])
        return 1.0 - (Math.min(1.0, dif ));
    }
    
    /**
     * Check if the given HSL color is inside the achromatic area.
     *
     * @param h the hue of the color
     * @param s the saturation of the color
     * @param l the luminance of the color
     * @return <tt>true</tt> if the given HSL color is inside the achromatic
     * area, <tt>false</tt> in other case.
     */
    private boolean isAchormatic(int h, int s, int l){
        return l <= THRESHOLD_TI;
    }
    
    /**
     * Check if the given HSL color is inside the semi-chromatic area.
     *
     * @param h the hue of the color
     * @param s the saturation of the color
     * @param l the luminance of the color
     * @return <tt>true</tt> if the given HSL color is inside the semi-chromatic
     * area, <tt>false</tt> in other case.
     */
    private boolean isSemiAchormatic(int h, int s, int l){
        return l > THRESHOLD_TI && s <= THRESHOLD_TS;
    }
    
    /**
     * Check if the given HSL color is inside the chromatic area.
     *
     * @param h the hue of the color
     * @param s the saturation of the color
     * @param l the luminance of the color
     * @return <tt>true</tt> if the given HSL color is inside the chromatic
     * area, <tt>false</tt> in other case.
     */
    private boolean isChormatic(int h, int s, int l){
        return l > THRESHOLD_TI && s > THRESHOLD_TS;
    }

    /**
     * Returns the flag associated to the use of chromatic areas to calculate
     * the distance between color.
     *
     * @return <tt>true</tt> if the chromatic areas are used, <tt>false</tt> in
     * other case.
     */
    public boolean isUseChromaticAreas() {
        return useChromaticAreas;
    }

    /**
     * Set the flag associated to the use of chromatic areas to calculate the
     * distance between color.
     *
     * @param useChromaticAreas <tt>true</tt> if the chromatic areas want to be
     * used, <tt>false</tt> in other case
     */
    public void setUseChromaticAreas(boolean useChromaticAreas) {
        this.useChromaticAreas = useChromaticAreas;
    }

    
}
