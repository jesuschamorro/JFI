package jfi.region;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class representing an image region.
 * 
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class Region {    
    /**
     * The region model. It determines how a region is represented internally.
     */
    private RegionModel model;
    
    
    /**
     * Constructs a new empty region. By default, the region model
     * {@link jfi.region.MaskRegionModel} is used.
     *
     * @param image the source image. 
     */
    public Region(BufferedImage image) {
        model = new MaskRegionModel(image);
    }
    
    /**
     * Constructs a new region with the set of pixels within the given shape. 
     * By default, the region model {@link jfi.region.ShapeRegionModel} is used.
     * 
     * @param image the source image.
     * @param shape the shape of the region
     */
    public Region(BufferedImage image, Shape shape){
        model = new ShapeRegionModel(image, shape);
    }

    /**
     * Constructs a new empty region with the given region model.
     * 
     * @param image the source image.
     * @param model the region model.
     */
    public Region(BufferedImage image, Class<? extends RegionModel> model){
        try {
            Constructor constructor = model.getConstructor(BufferedImage.class);
            this.model = (RegionModel)constructor.newInstance(image);
        } catch (NoSuchMethodException | SecurityException | InstantiationException 
                | IllegalAccessException | IllegalArgumentException 
                | InvocationTargetException ex) {
            Logger.getLogger(Region.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Returns the region model associated to this region.
     * 
     * @return the region model associated to this region.
     */
    public RegionModel getRegionModel(){
        return model;
    }
    
    /**
     * Returns the image associated to this region.
     * 
     * @return the image associated to this region.
     */
    public BufferedImage getSource() {
        return model.getSource(); 
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
    public boolean isInside(Point2D p) {
        return model.isInside(p);
    }
    
    /**
     * Tests if a specified {@link Point2D} is inside the boundary of this
     * region, as described by the
     *  <a href="{@docRoot}/java/awt/Shape.html#def_insideness">
     * definition of insideness</a>.
     *
     * @param x the X coordinate of the pixel (in relation to the image origin).
     * @param y the Y coordinate of the pixel (in relation to the image origin).
     * @return <code>true</code> if the specified <code>Point2D</code> is inside
     * the boundary of the region; <code>false</code> otherwise.
     */
    public boolean isInside(int x, int y) {
        return model.isInside(x,y);
    }
    
    /**
     * Returns the RGB color of the specified pixel inside the region.
     *
     * @param x the X coordinate of the pixel (in relation to the image origin).
     * @param y the Y coordinate of the pixel (in relation to the image origin).
     * @return the RGB color of the specified pixel; <code>null</code> if the
     * pixel is not inside the region.
     */
    public Color getRGB(int x, int y) {        
        return model.getRGB(x, y);
    }
       
    
    /**
     * Returns a bounding rectangle that completely encloses this region.
     *
     * @return the bounding rectangle for the region.
     */
    public Rectangle getBounds() {
        return model.getBounds();
    }
    
    /**
     * Creates an image with the pixels inside this region.
     * 
     * @return an image with the pixels inside this region.
     */
    public BufferedImage createImage(){
        int width = model.getBounds().width;
        int height = model.getBounds().height;
        
        BufferedImage output = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
        RegionIterator.Pixel it = new RegionIterator.Pixel(this);
        Point location = this.getBounds().getLocation();
        Color color;       
        while(it.hasNext()){
            color = it.next();
            output.setRGB(it.getX()-location.x, it.getY()-location.y, color.getRGB());  
        }
        return output;
    }
    
    
    /**
     * Creates a binary (1-bit) image associated to this region. The white color 
     * will be associated to pixels inside the region. 
     * 
     * @return a binary (1-bit) image associated to this region.
     */
    public BufferedImage createImageMask() {
        if (model.getClass() == MaskRegionModel.class) {
            //Mask-based model case
            return ((MaskRegionModel) model).getImageMask();
        } else {
            BufferedImage mask = new BufferedImage(this.getSource().getWidth(), this.getSource().getHeight(), BufferedImage.TYPE_BYTE_BINARY);
            if (model.getClass() == ShapeRegionModel.class) {
                //Shape-based model case
                mask.createGraphics().fill(((ShapeRegionModel)model).getShape());
            } else {
                //General case
                RegionIterator.Pixel it = new RegionIterator.Pixel(this);
                while (it.hasNext()) {
                    it.next();
                    ((WritableRaster)mask.getRaster()).setSample(it.getX(), it.getY(), 0, 1);
                }
            }
            return mask;
        }
    }
    
    
    /**
     * Adds a new pixel to this region.
     * 
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     */
    public void addPixel(int x, int y){
        model.addPixel(x, y);
    }
    
    /**
     * Adds a new pixel to this region.
     * 
     * @param position pixel coordinate.
     */
    public void addPixel(Point position){
        model.addPixel(position);
    }
    
    /**
     * Adds the set of pixels within the given shape to this region.
     * 
     * @param shape the shape bounding the set of pixels.
     */
    public void addShape(Shape shape){
        model.addShape(shape);
    }
}
