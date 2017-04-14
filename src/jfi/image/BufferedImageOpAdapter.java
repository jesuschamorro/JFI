package jfi.image;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * An abstract adapter class for image operators. It provides a default
 * implementation for the {@link java.awt.image.BufferedImageOp} interface,
 * except for the method
 * {@link java.awt.image.BufferedImageOp#filter(java.awt.image.BufferedImage, java.awt.image.BufferedImage)}
 * that should be implemented in the subclass.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public abstract class BufferedImageOpAdapter implements BufferedImageOp {
    
    /**
     * Performs a single-input/single-output operation on an image.
     * 
     * @param src the image to be filtered
     * @param dest image in which to store the results
     *
     * @return the filtered image.
     *
     * @throws IllegalArgumentException If the source and/or destination image
     * is not compatible with the types of images allowed by the class
     * implementing this filter.
     */
    @Override
    public abstract BufferedImage filter(BufferedImage src, BufferedImage dest);

    /**
     * Returns the bounding box of the filtered destination image. By default, 
     * the source image bounding box is returned.
     *
     * @param src the image to be filtered
     *
     * @return a rectangle representing the destination image's bounding box.
     */
    @Override
    public Rectangle2D getBounds2D(BufferedImage src) { 
         return src.getRaster().getBounds();
    }

    /**
     * Creates a zeroed destination image with the correct size and number of
     * bands. By default, an image of the same size is created.
     *
     * @param src the image to be filtered.
     * @param destCM color model of the destination.  If null, the color model 
     * of the source is used.
     *
     * @return The zeroed destination image.
     */
    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
      if (destCM == null) destCM = src.getColorModel();
      WritableRaster wr = destCM.createCompatibleWritableRaster(src.getWidth(),src.getHeight());
      return new BufferedImage(destCM,wr,destCM.isAlphaPremultiplied(), null);
    }

    /**
     * Returns the location of the corresponding destination point given a point
     * in the source image. If <code>dstPt</code> is specified, it is used to
     * hold the return value. By default, source point is returned.
     *
     * @param srcPt the point that represents the point in the source image.
     * @param dstPt the point in which to store the result.
     *
     * @return the point in the destination image that corresponds to the
     * specified point in the source image.
     */
    @Override
    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        if(dstPt==null) dstPt=(Point2D)srcPt.clone();
        else dstPt.setLocation(srcPt);
        return dstPt;
    }

    /**
     * Returns the rendering hints for this operation. By default, it returns
     * <code>null</code>
     *
     * @return the rendering hints for this operation.
     */
    @Override
    public RenderingHints getRenderingHints() {
        return null; 
    }

}
