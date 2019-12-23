package jfi.region;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Interface representing an image region model. Each subclass implementing this
 * interface must determine how a region is represented internally.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public interface RegionModel {
    /**
     * Returns the image associated to the region model.
     * 
     * @return the image associated to the region model.
     */
    public BufferedImage getSource();
    
    /**
     * Tests if a specified {@link Point2D} is inside the boundary of this
     * region.
     *
     * @param p the specified <code>Point2D</code> to be tested
     * @return <code>true</code> if the specified <code>Point2D</code> is inside
     * the boundary of the region; <code>false</code> otherwise.
     */
    public boolean isInside(Point2D p);
    
    /**
     * Tests if a specified point is inside the boundary of this region.
     *
     * @param x the X coordinate of the pixel (in relation to the image origin).
     * @param y the Y coordinate of the pixel (in relation to the image origin).
     * @return <code>true</code> if the specified <code>Point2D</code> is inside
     * the boundary of the region; <code>false</code> otherwise.
     */
    public boolean isInside(int x, int y);
    
    /**
     * Returns the RGB color of the specified pixel inside the region.
     *
     * @param x the X coordinate of the pixel (in relation to the image origin).
     * @param y the Y coordinate of the pixel (in relation to the image origin).
     * @return the RGB color of the specified pixel; <code>null</code> if the
     * pixel is not inside the region.
     */
    public Color getRGB(int x, int y);
    
     /**
     * Adds a new pixel to the region.
     * 
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     */
    public void addPixel(int x, int y);
    
    /**
     * Adds a new pixel to the region.
     * 
     * @param position pixel coordinate.
     */
    public void addPixel(Point position);
    
    /**
     * Adds the set of pixels within the given shape to the region.
     * 
     * @param shape the shape bounding the set of pixels.
     */
    public void addShape(Shape shape);
    
    /**
     * Returns a bounding rectangle that completely encloses the region.
     *
     * @return the bounding rectangle for the region.
     */
    public Rectangle getBounds();
}
