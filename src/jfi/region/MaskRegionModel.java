package jfi.region;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.NoSuchElementException;

/**
 * A class representing a region model where a region is represented internally
 * by means an image mask.
 * 
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class MaskRegionModel implements RegionModel{
    /**
     * Source image.
     */ 
    private final BufferedImage source;
    /**
     * Image mask
     */
    private BufferedImage mask = null;
    /**
     * Bounds of the region.
     */
    private Rectangle bounds = null;
    
    /**
     * Constructs a new region model associated to an empty region. 
     * 
     * @param image the source image.
     */
    public MaskRegionModel(BufferedImage image){
        this.source = image;
        if(image!=null){
            mask = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
            bounds = null; //It must be updated with the fisrt insertion
        }
    }
    
    /**
     * Constructs a new region with the set of pixels within the given shape.
     * 
     * @param image the source image.
     * @param shape the shape of the region
     */
    public MaskRegionModel(BufferedImage image, Shape shape){
        this.source = image;
        if(image!=null){
            mask = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
            mask.createGraphics().fill(shape);            
            bounds = shape.getBounds();
        }
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
        return this.isInside((int)p.getX(),(int)p.getY());
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
        return mask.getRaster().getSample(x, y, 0)>0;
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
        return bounds != null ? bounds.width : 0;
    }
    
    /**
     * Returns the height of the region bounds.
     * 
     * @return the height of the region bounds.
     */
    public int getHeight() {
        return bounds != null ? bounds.height : 0;
    }
    
    /**
     * Returns the location (upper-left corner) of the rectangle associated to 
     * the region bounds.
     * 
     * @return the location of the region bounds; <code>null</code> if the
     * case of an empty region.
     */
    public Point getLocation() {
        return bounds != null ? bounds.getLocation() : null;
    }
    
    /**
     * Returns a bounding rectangle that completely encloses this region.
     *
     * @return the bounding rectangle for the region. In the case of an empty
     * region, a {@link java.awt.Rectangle} with parameters (0,0,0,0) is
     * returned.
     */
    @Override
    public Rectangle getBounds() {
        return bounds != null ? this.bounds: new Rectangle();
    }
    
    /**
     * Returns a binary (1-bit) image associated to this region. The white color 
     * will be associated to pixels inside the region. 
     * 
     * @return a binary (1-bit) image associated to this region.
     */
    public BufferedImage getImageMask(){
        return mask;
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
        ((WritableRaster)mask.getRaster()).setSample(x, y, 0, 1);
        //Bounds update
        if(bounds!=null) bounds.add(new Rectangle(x,y,1,1));
        else bounds = new Rectangle(x,y,1,1);
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
         mask.createGraphics().fill(shape);
        //Bounds update
        if(bounds!=null) bounds.add(shape.getBounds());
        else bounds = shape.getBounds();
    }
}
