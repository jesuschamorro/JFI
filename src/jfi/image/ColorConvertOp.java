package jfi.image;

import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * This class performs a pixel-by-pixel color conversion of the data in the
 * source image. 
 * 
 * It is an extension of the {@link java.awt.image.ColorConvertOp} class that
 * may be used instead of its superclass with the same behavior. The novelty is
 * a new
 * {@link #filter(java.awt.image.BufferedImage, java.awt.image.BufferedImage, boolean)}
 * method which allows to decide if to use or not the CIEXYZ color space as
 * intermediate in the convesion procedure. By default, the
 * {@link java.awt.image.ColorConvertOp} class performs a sequence of
 * conversions to and from the CIEXYZ color space (that may slow down the
 * process). In addition, the equations to convert from/to de CIEXXX are not
 * always known for all the spaces and depends of a white point reference; it
 * is more common to know the equations for converting from/to RGB. To face the 
 * above, the new filter method allows to decide if to use the CIEXYZ, as usual,
 * or to use the RGB one (in that case, if the source image is an RGB image, 
 * only one corvesion is needed).
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class ColorConvertOp extends java.awt.image.ColorConvertOp{
    /**
     * The source color space.
     */
    private ColorSpace scr_cspace = null;
    /**
     * The destination color space.
     */
    private ColorSpace dst_cspace = null;

    /**
     * Constructs a new ColorConvertOp which will convert from a source color
     * space to a destination color space. The RenderingHints argument may be
     * null. This operation can be used only with BufferedImages, and will
     * convert directly from the ColorSpace of the source image to that of the
     * destination. The destination argument of the filter method cannot be
     * specified as null.
     *
     * @param hints the <code>RenderingHints</code> object used to control the
     * color conversion, or <code>null</code>
     */
    public ColorConvertOp(RenderingHints hints) {
        super(hints);
    }

     /**
     * Constructs a new ColorConvertOp from a ColorSpace object. The
     * RenderingHints argument may be null. This operation can be used only with
     * BufferedImages, and is primarily useful when the
     * {@link #filter(BufferedImage, BufferedImage) filter} method is invoked
     * with a destination argument of null. In that case, the ColorSpace defines
     * the destination color space for the destination created by the filter
     * method. Otherwise, the ColorSpace defines an intermediate space to which
     * the source is converted before being converted to the destination space.
     *
     * @param cspace defines the destination <code>ColorSpace</code> or an
     * intermediate <code>ColorSpace</code>
     * @param hints the <code>RenderingHints</code> object used to control the
     * color conversion, or <code>null</code>
     * @throws NullPointerException if cspace is null
     */
    public ColorConvertOp(ColorSpace cspace, RenderingHints hints) {
        super(cspace, hints);
        this.dst_cspace = cspace; // CSList[0] in superclass
    }

    /**
     * Constructs a new ColorConvertOp from two ColorSpace objects. The
     * RenderingHints argument may be null. This Op is primarily useful for
     * calling the filter method on Rasters, in which case the two ColorSpaces
     * define the operation to be performed on the Rasters. In that case, the
     * number of bands in the source Raster must match the number of components
     * in srcCspace, and the number of bands in the destination Raster must
     * match the number of components in dstCspace. For BufferedImages, the two
     * ColorSpaces define intermediate spaces through which the source is
     * converted before being converted to the destination space.
     *
     * @param srcCspace the source <code>ColorSpace</code>
     * @param dstCspace the destination <code>ColorSpace</code>
     * @param hints the <code>RenderingHints</code> object used to control the
     * color conversion, or <code>null</code>
     * @throws NullPointerException if either srcCspace or dstCspace is null
     */
    public ColorConvertOp(ColorSpace srcCspace, ColorSpace dstCspace, RenderingHints hints) {
        super(srcCspace, dstCspace, hints);
        this.scr_cspace = srcCspace; //CSList[0] in superclass
        this.dst_cspace = dstCspace; //CSList[1] in superclass
    }

    /**
     * Constructs a new ColorConvertOp from an array of ICC_Profiles. The
     * RenderingHints argument may be null. The sequence of profiles may include
     * profiles that represent color spaces, profiles that represent effects,
     * etc. If the whole sequence does not represent a well-defined color
     * conversion, an exception is thrown.
     * <p>
     * For BufferedImages, if the ColorSpace of the source BufferedImage does
     * not match the requirements of the first profile in the array, the first
     * conversion is to an appropriate ColorSpace. If the requirements of the
     * last profile in the array are not met by the ColorSpace of the
     * destination BufferedImage, the last conversion is to the destination's
     * ColorSpace.
     * <p>
     * For Rasters, the number of bands in the source Raster must match the
     * requirements of the first profile in the array, and the number of bands
     * in the destination Raster must match the requirements of the last profile
     * in the array. The array must have at least two elements or calling the
     * filter method for Rasters will throw an IllegalArgumentException.
     *
     * @param profiles the array of <code>ICC_Profile</code> objects
     * @param hints the <code>RenderingHints</code> object used to control the
     * color conversion, or <code>null</code>
     * @exception IllegalArgumentException when the profile sequence does not
     * specify a well-defined color conversion
     * @exception NullPointerException if profiles is null
     */
    public ColorConvertOp(ICC_Profile[] profiles, RenderingHints hints) {
        super(profiles, hints);
    }
    
    /**
     * Constructs a new ColorConvertOp from a ColorSpace object with null
     * RenderingHints. This operation can be used only with BufferedImages, and
     * is primarily useful when the
     * {@link #filter(BufferedImage, BufferedImage) filter} method is invoked
     * with a destination argument of null. In that case, the ColorSpace defines
     * the destination color space for the destination created by the filter
     * method. Otherwise, the ColorSpace defines an intermediate space to which
     * the source is converted before being converted to the destination space.
     *
     * @param cspace defines the destination color space
     * @throws NullPointerException if cspace is null
     */
    public ColorConvertOp(ColorSpace cspace) {
        this(cspace, null);
    }
    
    /**
     * ColorConverts the source BufferedImage. If the destination image is null,
     * a BufferedImage will be created with an appropriate ColorModel.
     *
     * @param src the source <code>BufferedImage</code> to be converted
     * @param dst the destination <code>BufferedImage</code>, or
     * <code>null</code>
     * @param useCIEXYZ <tt>true</tt> for using CIEXYZ color space as
     * intermediate in the convesion procedure; <tt>false</tt> for RGB-based
     * conversion.
     *
     * @return <code>dest</code> color converted from <code>src</code> or a new,
     * converted <code>BufferedImage</code> if <code>dest</code> is
     * <code>null</code>.
     * 
     * @exception IllegalArgumentException if (1) dest is null and this op was
     * constructed using the constructor which takes only a RenderingHints
     * argument, since the operation is ill defined; (2) the source/destination
     * image color space (if available) does not match with this operator 
     * parameters; (3) width or height of source and destination (if available) 
     * images do not match. 
     */
    public BufferedImage filter(BufferedImage src, BufferedImage dst, boolean useCIEXYZ) {
        return useCIEXYZ ? this.filter(src, dst) : this._filter(src, dst);
    }
   
    /**
     * ColorConverts the source BufferedImage. If the destination image is null,
     * a BufferedImage will be created with an appropriate ColorModel.
     *
     * @param src the source <code>BufferedImage</code> to be converted
     * @param dest the destination <code>BufferedImage</code>, or
     * <code>null</code>
     *
     * @return <code>dest</code> color converted from <code>src</code> or a new,
     * converted <code>BufferedImage</code> if <code>dest</code> is
     * <code>null</code>.
     * 
     * @exception IllegalArgumentException if (1) dest is null and this op was
     * constructed using the constructor which takes only a RenderingHints
     * argument, since the operation is ill defined; (2) the source/destination
     * image color space (if available) does not match with this operator 
     * parameters; (3) width or height of source and destination (if available) 
     * images do not match. 
     */
    private BufferedImage _filter(BufferedImage src, BufferedImage dst) {
        IllegalArgumentException check = checkFilterParameters(src, dst);
        if( check != null ) throw check;
        
        // We set the source and destination color spaces. If they (one or both)
        // were set when this operator was constructed, at this point we know 
        // that they are compatibles with the source/destination images. If they
        // were not set, the image ones will be used. 
        ColorSpace srcColorSpace, dstColorSpace;
        if (src.getColorModel() instanceof IndexColorModel) {
            IndexColorModel icm = (IndexColorModel) src.getColorModel();
            src = icm.convertToIntDiscrete(src.getRaster(), true);
        }
        srcColorSpace = src.getColorModel().getColorSpace();
        if(dst==null){
            dst = createCompatibleDestImage(src, null); // Use superclss CSList
        } 
        dstColorSpace = dst.getColorModel().getColorSpace();
        
        // We start the conversion of color spaces using the toRGB and fromRGB
        // methods. The use of alpha component will be taken into account.        
        Raster srcRas = src.getRaster();
        WritableRaster dstRas = dst.getRaster();
        ColorModel srcCM = src.getColorModel();
        ColorModel dstCM = dst.getColorModel();
        int w = src.getWidth();
        int h = src.getHeight();
        int srcNumComp = srcCM.getNumColorComponents();
        int dstNumComp = dstCM.getNumColorComponents();
        boolean dstHasAlpha = dstCM.hasAlpha();
        boolean needSrcAlpha = srcCM.hasAlpha() && dstHasAlpha;
        Object spixel = null;
        Object dpixel = null;
        float[] color = null;
        float[] tmpColor;
        float[] dstColor;
        
        dstColor = dstHasAlpha ? new float[dstNumComp + 1] : new float[dstNumComp];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                spixel = srcRas.getDataElements(x, y, spixel);
                color = srcCM.getNormalizedComponents(spixel, color, 0);
                tmpColor = srcColorSpace.toRGB(color);
                tmpColor = dstColorSpace.fromRGB(tmpColor);
                for (int i = 0; i < dstNumComp; i++) {
                    dstColor[i] = tmpColor[i];
                }
                if (needSrcAlpha) {
                    dstColor[dstNumComp] = color[srcNumComp];
                } else if (dstHasAlpha) {
                    dstColor[dstNumComp] = 1.0f;
                }
                dpixel = dstCM.getDataElements(dstColor, 0, dpixel);
                dstRas.setDataElements(x, y, dpixel);
            }
        }
        return dst;
    }
    
    /**
     * Check if the filtering parameters are correct.
     * 
     * @param src the source <code>BufferedImage</code> to be converted
     * @param dst the destination <code>BufferedImage</code>, or
     * <code>null</code>
     * @return an exception object if the parameters are no correct, null if 
     * they are OK.
     */
    private IllegalArgumentException checkFilterParameters(BufferedImage src, BufferedImage dst) {
        if (scr_cspace != null && src.getColorModel().getColorSpace().getType() != scr_cspace.getType()) {
            return new IllegalArgumentException("Source image color space does not match with this operator parameters");
        }
        if (dst != null && dst_cspace != null && dst.getColorModel().getColorSpace().getType() != dst_cspace.getType()) {
            return new IllegalArgumentException("Destination image color space does not match with this operator parameters");
        }
        if (dst != null && (src.getHeight() != dst.getHeight() || src.getWidth() != dst.getWidth())) {
            return new IllegalArgumentException("Width or height of source and destination images do not match");
        }
        if (dst == null && this.dst_cspace == null) {
            return new IllegalArgumentException("Destination image is null and there is no information about destination color space");
        }
        return null;
    }
    
}
