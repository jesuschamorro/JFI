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
     * Constant 
     */
    protected static final double PI2 = Math.PI * 2.0;

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
        throw new UnsupportedOperationException("Transform to RGB from HSL is not supported yet."); 
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
        float H = 0, S, L;
        float var_R = rgbvalue[0];
        float var_G = rgbvalue[1];
        float var_B = rgbvalue[2];

        float var_Max = (float) Math.max(var_R, Math.max(var_G, var_B));
        float var_Min = (float) Math.min(var_R, Math.min(var_G, var_B));
        float del_Max = var_Max - var_Min; //Delta RGB value

        L = (var_Max + var_Min) / 2.0f;

        if (del_Max == 0) { //This is a gray, no chroma...
            H = 0; //HSL results = 0 1
            S = 0;
        } else { //Chromatic data...
            if (L < 0.5f) {
                S = del_Max / (var_Max + var_Min);
            } else {
                S = del_Max / (2.0f - var_Max - var_Min);
            }

            float del_R = (((var_Max - var_R) / 6.0f) + (del_Max / 2.0f)) / del_Max;
            float del_G = (((var_Max - var_G) / 6.0f) + (del_Max / 2.0f)) / del_Max;
            float del_B = (((var_Max - var_B) / 6.0f) + (del_Max / 2.0f)) / del_Max;
            if (var_R == var_Max) {
                H = del_B - del_G;
            } else if (var_G == var_Max) {
                H = (1.0f / 3.0f) + del_R - del_B;
            } else if (var_B == var_Max) {
                H = (2.0f / 3.0f) + del_G - del_R;
            }

            if (H < 0.0f) {
                H += 1.0f;
            }
            if (H > 1.0f) {
                H -= 1.0f;
            }
        }

        float[] hsl = new float[3];
        hsl[0] = H * (float) this.getMaxValue(0);
        hsl[1] = S * (float) this.getMaxValue(1);
        hsl[2] = L * (float) this.getMaxValue(2);
        
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
                return (float)PI2;
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
    

}
