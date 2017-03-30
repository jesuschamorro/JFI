package jfi.image;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author Jesús Chamorro
 */
public abstract class BufferedImageOpAdapter implements BufferedImageOp {
      
    @Override
    public abstract BufferedImage filter(BufferedImage src, BufferedImage dest);

    @Override
    public Rectangle2D getBounds2D(BufferedImage src) { 
         return src.getRaster().getBounds();
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
      if (destCM == null) destCM = src.getColorModel();
      WritableRaster wr = destCM.createCompatibleWritableRaster(src.getWidth(),src.getHeight());
      return new BufferedImage(destCM,wr,destCM.isAlphaPremultiplied(), null);
    }

    @Override
    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        if(dstPt==null) dstPt=(Point2D)srcPt.clone();
        else dstPt.setLocation(srcPt);
        return dstPt;
    }

    @Override
    public RenderingHints getRenderingHints() {
        return null; 
    }

}
