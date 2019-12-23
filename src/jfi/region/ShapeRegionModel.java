package jfi.region;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.NoSuchElementException;

/**
 * A class representing a region model where a region is represented internally
 * by means a {@link java.awt.Shape} object.
 *
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class ShapeRegionModel implements RegionModel{
    /**
     * Source image.
     */ 
    private final BufferedImage source;
    /**
     * The shape of the region
     */
    private Area shape;
    
    /**
     * Constructs a new shape-based region model associated to an empty region.
     *
     * @param image the source image.
     */
    public ShapeRegionModel(BufferedImage image) {
        this.source = image;
        this.shape = null;
    }
    
    /**
     * Constructs a new shape-based region model using as shape the one given by 
     * parameter.
     * 
     * @param image the source image.
     * @param shape the shape of the region
     */
    public ShapeRegionModel(BufferedImage image, Shape shape){
        this.source = image;
        this.shape = new Area(shape); 
        //We ensure that the shape is inside the image; To do this, we intersect 
        //the shape passed by parameter with the image frame
        Area imageBound = new Area(new Rectangle(image.getWidth(),image.getHeight()));
        this.shape.intersect(imageBound);
    }

    /**
     * Returns the image associated to this region model.
     * 
     * @return the image associated to this region model.
     */
    @Override
    public BufferedImage getSource() {
        return source; 
    }

    /**
     * Returns the shape of this region model.
     *
     * @return the shape of this region model; <code>null</code> if the case of
     * an empty region.
     */
    public Shape getShape() {
        return shape;
    }
    
    /**
     * Tests if a specified {@link Point2D} is inside the boundary of this
     * region, as described by the
     *  <a href="{@docRoot}/java/awt/Shape.html#def_insideness">
     * definition of insideness</a>.
     *
     * @param p the specified <code>Point2D</code> to be tested
     * @return <code>true</code> if the specified <code>Point2D</code> is inside
     * the boundary of the region; <code>false</code> otherwise.
     */
    @Override
    public boolean isInside(Point2D p) {
        return shape!=null ? shape.contains(p) : false;
    }
    
    /**
     * Tests if a specified point is inside the boundary of this region, as
     * described by the
     * <a href="{@docRoot}/java/awt/Shape.html#def_insideness">
     * definition of insideness</a>.
     *
     * @param x the X coordinate of the pixel (in relation to the image origin).
     * @param y the Y coordinate of the pixel (in relation to the image origin).
     * @return <code>true</code> if the specified <code>Point2D</code> is inside
     * the boundary of the region; <code>false</code> otherwise.
     */
    @Override
    public boolean isInside(int x, int y) {
        return shape!=null ? shape.contains(x,y) : false;
    }
    
    /**
     * Returns the RGB color of the specified pixel inside the region.
     *
     * @param x the X coordinate of the pixel (in relation to the image origin).
     * @param y the Y coordinate of the pixel (in relation to the image origin).
     * @return the RGB color of the specified pixel; <code>null</code> if the
     * pixel is not inside the region.
     */
    @Override
    public Color getRGB(int x, int y) {
        if (!isInside(x,y)) {
            return null;
        }
        return new Color(source.getRGB(x, y));
    }
    
    /**
     * Returns the width of the region bounds.
     * 
     * @return the width of the region bounds.
     */
    public int getWidth() {
        return shape!=null ? shape.getBounds().width : 0;
    }
    
    /**
     * Returns the height of the region bounds.
     * 
     * @return the height of the region bounds.
     */
    public int getHeight() {
        return shape!=null ? shape.getBounds().height : 0;
    }
    
    /**
     * Returns the location (upper-left corner) of the rectangle associated to 
     * the region bounds.
     * 
     * @return the location of the region bounds; <code>null</code> if the
     * case of an empty region.
     */
    public Point getLocation() {
        return shape!=null ? shape.getBounds().getLocation() : null;
    }
    
    /**
     * Returns a bounding rectangle that completely encloses this region.
     *
     * @return the bounding rectangle for the region; In the case of an empty
     * region, a {@link java.awt.Rectangle} with parameters (0,0,0,0) is
     * returned.
     */
    @Override
    public Rectangle getBounds() {
        return shape!=null ? shape.getBounds() : new Rectangle();
    }
    
    /**
     * Adds a new pixel to this region.
     * 
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     */
    @Override
    public void addPixel(int x, int y){
        if (x<0 || x>=source.getWidth() || y<0 || y>=source.getHeight()) {
            throw new NoSuchElementException("(x,y) coordinates out of bounds");
        }
        Area pixelArea = new Area(new Rectangle(x,y,1,1));
        if(shape!=null) shape.add(pixelArea);
        else shape = pixelArea;
    }
    
    /**
     * Adds a new pixel to this region.
     * 
     * @param position pixel coordinate.
     */
    @Override
    public void addPixel(Point position){
        this.addPixel(position.x,position.y);        
    }
    
    /**
     * Adds the set of pixels within the given shape to this region.
     * 
     * @param shape the shape bounding the set of pixels.
     */
    @Override
    public void addShape(Shape shape){
        if(shape!=null) this.shape.add(new Area(shape));
        else this.shape = new Area(shape); 
        Area imageBound = new Area(new Rectangle(source.getWidth(),source.getHeight()));
        this.shape.intersect(imageBound);
    }
}
