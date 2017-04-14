package jfi.image;

import java.awt.color.ColorSpace;
import static java.awt.color.ColorSpace.CS_CIEXYZ;
import java.awt.color.ICC_ColorSpace;

/**
 * Class representing the YCbCr color space. 
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class YCbCrColorSpace extends ColorSpace {
    /**
     * Number of components of this color space
     */
    private static final int NUM_COMPONENTS = 3;
    /**
     * Inner CIE XYZ color space used for transforms from/to this color space.
     */
    private static final ICC_ColorSpace xyzCS = (ICC_ColorSpace)ColorSpace.getInstance (CS_CIEXYZ);
    
    /**
     * Constructs a new grey level color space. It is one-component color space
     * where, in fact, there is no color but a grey level.
     * 
     */
    public YCbCrColorSpace(){
        super(ColorSpace.TYPE_YCbCr,NUM_COMPONENTS);
    }
    
    /**
     * Transforms a color value assumed to be in this color space into a value
     * in the RGB color space.
     *
     * @param yCbCr a float array with length of at least the number of
     * components in this ColorSpace.
     * @return a float array of length 3 with the RGB values.
     * @throws ArrayIndexOutOfBoundsException if array length is not at least
     * the number of components in this ColorSpace.
     */
    @Override
    public float[] toRGB(float[] yCbCr) {
        float[] rgb = new float[3];
        rgb[0] = Math.max(0,Math.min(yCbCr[0] + 1.4020f * yCbCr[2],1));
        rgb[1] = Math.max(0,Math.min(yCbCr[0] - 0.3441f * yCbCr[1] - 0.7141f * yCbCr[2],1));
        rgb[2] = Math.max(0,Math.min(yCbCr[0] + 1.7720f * yCbCr[1],1));
        
        return rgb;
    }

    /**
     * Transforms a color value assumed to be in the RGB color space into this
     * color space.
     *
     * @param rgbvalue a float array with length of at least 3.
     * @return a float array of length 3 with the YCbCr value.
     * @throws ArrayIndexOutOfBoundsException if array length is not at least 3
     */
    @Override
    public float[] fromRGB(float[] rgbvalue) {   
        float[] yCbCr = new float[NUM_COMPONENTS];
        float r = rgbvalue[0];
        float g = rgbvalue[1];
        float b = rgbvalue[2];
        yCbCr[0] = ( 0.299f   * r + 0.587f   * g + 0.114f   * b);
	yCbCr[1] = (-0.16874f * r - 0.33126f * g + 0.50000f * b);
	yCbCr[2] = ( 0.50000f * r - 0.41869f * g - 0.08131f * b);
        return yCbCr;
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
        return xyzCS.fromRGB(this.toRGB(colorvalue));
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
        return this.fromRGB( xyzCS.toRGB(colorvalue) );       
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
        if (component<0 || component>=NUM_COMPONENTS) {
            throw new IllegalArgumentException("Index component out of bounds");
        }
        switch (component) {
            case 0:
                return 1.0f;
            case 1:
                return 0.5f;
            case 2:
                return 0.5f;
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
        if (component<0 || component>=NUM_COMPONENTS) {
            throw new IllegalArgumentException("Index component out of bounds");
        }
        switch (component) {
            case 0:
                return (float) 0.0f;
            case 1:
                return -0.5f;
            case 2:
                return -0.5f;
            default:
                return super.getMinValue(component);
        }
    }
	
}
