package jfi.color;

import java.awt.color.ColorSpace;
import static java.awt.color.ColorSpace.CS_CIEXYZ;
import java.awt.color.ICC_ColorSpace;

/**
 * Class representing a grey level color space. It is one-component color space
 * where, in fact, there is no color but a grey level data as pseudo-color.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class GreyColorSpace extends ColorSpace {
    /**
     * Number of components of this color space
     */
    private static final int NUM_COMPONENTS = 1;
    /**
     * Inner CIE XYZ color space used for transforms from/to this color space.
     */
    private final ICC_ColorSpace xyzCS = (ICC_ColorSpace)ColorSpace.getInstance (CS_CIEXYZ);
    /**
     * The default weight of the red component used in the transform from RGB.
     */
    public static float DEFAULT_RED_WEIGHT =  0.299f;
    /**
     * The default weight of the green component used in the transform from RGB.
     */
    public static float DEFAULT_GREEN_WEIGHT =  0.587f;
    /**
     * The default weight of the blue component used in the transform from RGB.
     */
    public static float DEFAULT_BLUE_WEIGHT =  0.114f;
    /**
     * The weight of the red component used in the transform from RGB.
     */
    private float redWeight =  DEFAULT_RED_WEIGHT;
    /**
     * The weight of the red component used in the transform from RGB.
     */
    private float greenWeight =  DEFAULT_GREEN_WEIGHT;
    /**
     * The weight of the red component used in the transform from RGB.
     */
    private float blueWeight =  DEFAULT_BLUE_WEIGHT;
    
    /**
     * Constructs a new grey level color space. It is one-component color space
     * where, in fact, there is no color but a grey level.
     * 
     */
    public GreyColorSpace(){
        super(ColorSpace.TYPE_GRAY,NUM_COMPONENTS);
    }
    
    /**
     * Set the weights of the RGB component used in the transform from RGB to 
     * grey level. The sum of the weights must be 1.0
     * 
     * @param rWeight the weight of the red component.
     * @param gWeight the weight of the green component.
     * @param bWeight the weight of the blue component.
     * @throws IllegalArgumentException if the sum of the weights are not 1.0.
     */
    public void setRGBWeights(float rWeight, float gWeight, float bWeight){
        if(rWeight+gWeight+bWeight != 1.0){
            throw new IllegalArgumentException("The sum of the weights must be 1.0");
        }
        redWeight = rWeight;
        greenWeight = gWeight;
        blueWeight = bWeight;
    }
    
    /**
     * Returns the weight of the red component used in the transform from RGB to 
     * grey level.
     * 
     * @return the weight of the red component.
     */
    public float getRedWeight(){
        return this.redWeight;
    }
    
    /**
     * Returns the weight of the green component used in the transform from RGB 
     * to grey level.
     * 
     * @return the weight of the green component.
     */
    public float getGreenWeight(){
        return this.greenWeight;
    }
    
    /**
     * Returns the weight of the blue component used in the transform from RGB  
     * to grey level.
     * 
     * @return the weight of the blue component.
     */
    public float getBlueWeight(){
        return this.redWeight;
    }
    
    /**
     * Transforms a color value assumed to be in this color space into a value
     * in the RGB color space. Specifically, it generates a RGB value with the 
     * three components equal to the grey level.
     *
     * @param colorvalue a float array with length of at least the number of
     * components in this ColorSpace.
     * @return a float array of length 3 with the RGB values.
     * @throws ArrayIndexOutOfBoundsException if array length is not at least
     * the number of components in this ColorSpace.
     */
    @Override
    public float[] toRGB(float[] colorvalue) {
        float grey = colorvalue[0];
        float[] rgb = {grey, grey, grey};
        return rgb;
    }

    /**
     * Transforms a color value assumed to be in the RGB color space into this
     * color space.
     * 
     *  <p>
     * Specifically, the following equation based on the ITU-R BT.601 conversion
     * is used:
     * <code>
     * Y' = + 0.299 * R' + 0.587 * G' + 0.114 * B' <br>
     * </code> 
     * with R', G', B' and Y' in [0..1]
     *
     *
     * @param rgbvalue a float array with the RGB values in [0..1].
     * @return a float array of length 1 with the grey level value.
     * @throws ArrayIndexOutOfBoundsException if array length is not at least 3
     */
    @Override
    public float[] fromRGB(float[] rgbvalue) {   
        float[] greyLevel = new float[NUM_COMPONENTS];
        float r = rgbvalue[0];
        float g = rgbvalue[1];
        float b = rgbvalue[2];
        greyLevel[0] = (redWeight*r + greenWeight*g + blueWeight*b);
        return greyLevel;
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
	
}
