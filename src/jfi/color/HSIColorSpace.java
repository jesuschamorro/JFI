package jfi.color;

import java.awt.color.ColorSpace;
import static java.awt.color.ColorSpace.CS_CIEXYZ;
import java.awt.color.ICC_ColorSpace;

/**
 * Class representing the HSI color space.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class HSIColorSpace extends ColorSpace {
    /**
     * Number of components of this color space
     */
    private static final int NUM_COMPONENTS = 3;
    /**
     * Inner CIE XYZ color space used for transforms from/to this color space.
     */
    private static final ICC_ColorSpace XYZCS = (ICC_ColorSpace) ColorSpace.getInstance(CS_CIEXYZ);    
    /**
     * Constant PI/2
     */
    protected static final double PI2 = Math.PI / 2.0;
    /**
     * Constant 2*PI
     */
    protected static final double PIx2 = Math.PI * 2.0;
    /**
     * Constant equals to square root of three
     */
    private static final double SQRT3 = Math.sqrt(3);
    /**
     * The formula used to calculate the hue value is based on a atan equation.
     */
    public static final int HUE_TYPE_ATAN = 1;
    /**
     * The formula used to calculate the hue value is based on a acos equation.
     */
    public static final int HUE_TYPE_ACOS = 2;
    /**
     * Formula used to calculate the hue value.
     */
    private int hue_type = HUE_TYPE_ACOS;

    /**
     * Constructs a new grey level color space. It is one-component color space
     * where, in fact, there is no color but a grey level.
     *
     */
    public HSIColorSpace() {
        super(ColorSpace.TYPE_HLS, NUM_COMPONENTS);
    }

    /**
     * Transforms a color value assumed to be in this color space into a value
     * in the RGB color space.
     * 
     * Not supported yet.
     *
     * @param hsi a float array with length of at least the number of
     * components in this ColorSpace.
     * @return a float array of length 3 with the RGB values.
     * @throws ArrayIndexOutOfBoundsException if array length is not at least
     * the number of components in this ColorSpace.
     */
    @Override
    public float[] toRGB(float[] hsi) {
        throw new UnsupportedOperationException("Transform to RGB from HSI is not supported yet."); 
    }
    

    /**
     * Transforms a color value assumed to be in the RGB color space into this
     * color space.
     *
     * @param rgbvalue a float array with the RGB values in [0..1].
     * @return a float array of length 3 with the YCbCr values.
     * @throws ArrayIndexOutOfBoundsException if array length is not at least 3
     */
    @Override
    public float[] fromRGB(float[] rgbvalue) {        
        float r = rgbvalue[0];
        float g = rgbvalue[1];
        float b = rgbvalue[2];
                
        float[] hsl = new float[3];    
        float i = (r+g+b) / 3.0f;  //In [0,1] 
        hsl[1] = 1.0f - (Math.min(r, Math.min(g, b))/i); //In [0,1];
        hsl[2] = i; //In [0,1];      
        if (hue_type == HUE_TYPE_ATAN) {
            //Transformation from paper “Fuzzy Homogeneity Measures for  
            //Path-based Colour Image Segmentation”, FuzzIEEE 2005
            float h = (float) Math.atan((SQRT3 * (g + b)) / (2.0 * r + g + b)); //In [-PI/2, PI/2]
            hsl[0] = (float) ((h + PI2) / Math.PI); //In [0,1] 
        } else {
            //Transformation from Belen´s thesis (only hue changes)
            float delta = (float) Math.acos(((r - g) + (r - b)) / (2.0 * Math.sqrt((r - g) * (r - g) + (r - b) * (g - b)))); //In [0, PI]
            float h = b < g ? delta : (float) PIx2 - delta;
            hsl[0] = (float) (h / PIx2); //In [0,1]
        }        
        return hsl;
    }

    /**
     * Transforms a color value assumed to be in this color space into the
     * CS_CIEXYZ conversion color space.
     *
     * <p>
     * See the {@link java.awt.color.ICC_ColorSpace#toCIEXYZ(float[]) toCIEXYZ}
     * method of <code>ICC_ColorSpace</code> for further information.
     * <p>
     * @param colorvalue a float array with length of at least the number of
     * components in this color space
     * @return a float array of length 3 with the XYZ value.
     * @throws ArrayIndexOutOfBoundsException if array length is not at least
     * the number of components in this ColorSpace.
     */
    @Override
    public float[] toCIEXYZ(float[] colorvalue) {
        return XYZCS.fromRGB(this.toRGB(colorvalue));
    }

    /**
     * Transforms a color value assumed to be in the CS_CIEXYZ conversion color
     * space into this ColorSpace.
     * <p>
     * See the
     * {@link java.awt.color.ICC_ColorSpace#fromCIEXYZ(float[]) fromCIEXYZ}
     * method of <code>ICC_ColorSpace</code> for further information.
     * <p>
     * @param colorvalue a float array with length of at least 3
     * @return a float array of length 1 with the grey level value.
     * @throws ArrayIndexOutOfBoundsException if array length is not at least 3
     */
    @Override
    public float[] fromCIEXYZ(float[] colorvalue) {
        return this.fromRGB(XYZCS.toRGB(colorvalue));
    }

    /**
     * Returns the maximum normalized color component value for the specified
     * component.
     *
     * @param component the component index
     * @return the maximum normalized component value
     * @throws IllegalArgumentException if component is less than 0 or greater
     * than numComponents - 1
     */
    @Override
    public float getMaxValue(int component) {
        if (component < 0 || component >= NUM_COMPONENTS) {
            throw new IllegalArgumentException("Index component out of bounds");
        }
        switch (component) {
            case 0:
                return (float)1.0;
            case 1:
                return 1.0f;
            case 2:
                return 1.0f;
            default:
                return super.getMaxValue(component);
        }
    }
    
    /**
     * Returns the minimum normalized color component value for the specified
     * component.
     *
     * @param component the component index
     * @return the minimum normalized component value
     * @throws IllegalArgumentException if component is less than 0 or greater
     * than numComponents - 1
     */
    @Override
    public float getMinValue(int component) {
        if (component < 0 || component >= NUM_COMPONENTS) {
            throw new IllegalArgumentException("Index component out of bounds");
        }
        switch (component) {
            default:
                return super.getMinValue(component);
        }
    }

    /**
     * Set the formula type used to calculate the hue value.
     * @param hue_type 
     */
    public void setHueType(int hue_type) {
        if (hue_type != HUE_TYPE_ATAN && hue_type != HUE_TYPE_ACOS) {
            throw new IllegalArgumentException("Hue type no valid");
        }
        this.hue_type = hue_type;
    }    
}
