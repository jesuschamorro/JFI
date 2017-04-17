package jfi.image;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.NoSuchElementException;
import jfi.geometry.Point3D;

/**
 * An iterator over an image. In each iteration, it produces an object of 
 * type <tt>T</tt>.
 * 
 * @param <T> the type of elements returned by this iterator.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public abstract class BufferedImageIterator<T> implements Iterator<T> {
    /**
     * The source image associated to this iterator.
     */
    protected BufferedImage source;

    /**
     * Constructs a new iterator for the given image. The purpose of this
     * (unique) constructor is to force its call from all the subclases in order
     * to set the source image (and others parameters dependent on it).
     *
     * @param source the source image associated to this iterator.
     */
    protected BufferedImageIterator(BufferedImage source) {
        this.setImage(source);
    }

    /**
     * Set the source image and others subclass parameters dependent on it.
     * 
     * @param image the source image. 
     */
    public abstract void setImage(BufferedImage image);

    /**
     * Returns the x-coordiante of the current pixel in the iteration.
     * 
     * @return the x-coordiante of the current pixel.
     */
    public abstract int getX();

    /**
     * Returns the y-coordiante of the current pixel in the iteration.
     * 
     * @return the y-coordiante of the current pixel.
     */
    public abstract int getY();
    
    /**
     * Inner class defining a particular image iterator that (1) goes over all
     * the pixels in the image and (2) for each pixel, returns a
     * three-dimensional point representing the three components of the color at
     * the given location. It is the standard iterator for an image.
     */
    public static class Pixel extends BufferedImageIterator<Point3D> {        
        /**
         * The width of the source image
         */
        private int width=0;
        /**
         * The length of the image (that is, the number of pixels)
         */
        private int length=0;
        /**
         * Current position in the iteration.
         */
        private int pos = 0;
        /**
         * The x-coordiante of the current pixel in the iteration.
         */
        private int x;
        /**
         * The y-coordiante of the current pixel in the iteration.
         */
        private int y;
        /**
         * Image point analyzed in each 'next' call. For reasons of efficiency,
         * it is declared as a class member variable (instead of a local one in
         * the 'next' method).
         */
        private final Point3D point = new Point3D();
        /**
         * Color analyzed in each 'next' call. For reasons of efficiency, it is
         * declared as a class member variable (instead of a local one in the
         * 'next' method).
         */
        private int color;
      
      
        /**
         * Constructs a new pixel-based iterator.
         * 
         * @param img the source image. 
         */
        public Pixel(BufferedImage img) {
            super(img);
        }
        
        /**
         * Set the source image and initializes the local parameters.
         *
         * @param image the source image.
         */
        @Override
        public void setImage(BufferedImage image) {
            this.source = image;
            if (image != null) {
                width = image.getWidth();
                length = image.getHeight() * width;
                pos = 0;
            } else {
                pos = length = 0;
            }
        }
        
        /**
         * Sets iterator position to the initial one.
         */
        public void init(){
            pos = 0;
        }

        /**
         * Returns <code>true</code> if the iteration has more elements (in
         * other words, returns <code>true</code> if {@link #next} would return
         * an element rather than throwing an exception).
         *
         * @return <code>true</code> if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return (pos < length);
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration. The type of the element is
         * {@link jfi.geometry.Point3D}, where the three components of the color 
         * are stored.
         * @throws NoSuchElementException if the iteration has no more elements.
         */
        @Override
        public Point3D next() {
            if (pos >= length) {
                throw new NoSuchElementException("No more pixels");
            }
            x = pos % width;  
            y = pos / width;
            // The variables 'color' and 'point' are declared as class member
            // variables for efficiency reasons (this method is call frequently)
            color = source.getRGB(x, y);
            point.x = (color >> 16) & 0xFF; // Red
            point.y = (color >> 8) & 0xFF;  // Green
            point.z =  color & 0xFF;        // Blue
            // The current position is updated
            pos++;
            
            return point;
        }
       
        /**
         * Returns the x-coordiante of the current pixel in the iteration.
         *
         * @return the x-coordiante of the current pixel.
         */
        @Override
        public int getX() {
            return x;
        }

        /**
         * Returns the y-coordiante of the current pixel in the iteration.
         *
         * @return the y-coordiante of the current pixel.
         */
        @Override
        public int getY() {
            return y;
        }
    } // End of inner class Pixel
    
    /**
     * Inner class defining a particular image iterator that (1) goes over all
     * the pixels in the image except those in the border -which size depends on
     * the tile size- and (2) for each pixel, returns the subimage (tile) 
     * centered on that pixel.
     */
    public static class Tile extends BufferedImageIterator<BufferedImage> {
        /**
         * The width of the tile in pixels.
         */
        private int tileWidth;
        /**
         * The height of the tile in pixels.
         */
        private int tileHeight;
        /**
         * Displacement between the center of the tile and its top-left corner.
         */
        private int dx, dy;
        /**
         * The width of the 'inner' image, that is, the source image without
         * the border generated by the tile size.
         */
        private int in_width=0;
        /**
         * The length of the image (that is, the number of pixels)
         */
        private int length=0;     
        /**
         * Current position in the iteration.
         */
        private int pos = 0;
        /**
         * The x-coordiante of the current pixel in the iteration.
         */
        private int x;
        /**
         * The y-coordiante of the current pixel in the iteration.
         */
        private int y;
        
        
        /**
         * Constructs a new tile-based iterator.
         * 
         * @param image the source image.
         * @param tileWidth the width of the tile in pixels. It must be a value
         * greater than 1 (if not, it is set automatically to 1).
         * @param tileHeight the height of the tile in pixels. It must be a
         * value greater than 1 (if not, it is set automatically to 1).
         */
        public Tile(BufferedImage image, int tileWidth, int tileHeight) {
            super(image);
            this.setTileSize(tileWidth, tileHeight);
        }

        /**
         * Set the size of the tile.
         *
         * @param tileWidth the width of the tile in pixels. It must be a value
         * greater than 1 (if not, it is set automatically to 1).
         * @param tileHeight the height of the tile in pixels. It must be a
         * value greater than 1 (if not, it is set automatically to 1).
         */
        public final void setTileSize(int tileWidth, int tileHeight) {
            this.tileWidth = Math.max(1, tileWidth);
            this.tileHeight = Math.max(1, tileWidth);
            dx = (int)tileWidth/2;
            dy = (int)tileHeight/2;
        }
        
        /**
         * Returns the width of the tile in pixels.
         *
         * @return the width of the tile.
         */
        public int getTileWidth() {
            return tileWidth;
        }

        /**
         * Returns the height of the tile in pixels.
         *
         * @return the height of the tile.
         */
        public int getTileHeight() {
            return tileHeight;
        }

        /**
         * Set the source image and initializes the local parameters.
         *
         * @param image the source image.
         */
        @Override
        public void setImage(BufferedImage image) {
            this.source = image;
            if (image != null) {
                in_width = Math.max(0, image.getWidth() - tileWidth) + 1;
                int in_height = Math.max(0, image.getHeight() - tileHeight) + 1;
                length = in_height * in_width;
                pos = 0;
            } else{
                pos = length = 0;
            }
        }
        
        /**
         * Sets iterator position to the initial one.
         */
        public void init(){
            pos = 0;
        }
        
        /**
         * Returns <code>true</code> if the iteration has more elements (in
         * other words, returns <code>true</code> if {@link #next} would return
         * an element rather than throwing an exception).
         *
         * @return <code>true</code> if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return (pos < length);
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration. The type of the element is
         * {@link java.awt.image.BufferedImage}, where the subimage is stored.
         * @throws NoSuchElementException if the iteration has no more elements.
         */
        @Override
        public BufferedImage next() {
            if (pos >= length) {
                throw new NoSuchElementException("No more pixels");
            }
            x = pos % in_width;
            y = pos / in_width;
            // The current position is updated
            pos++;
            
            return source.getSubimage(x, y, tileWidth, tileHeight);
        }

        /**
         * Returns the x-coordiante of the current pixel in the iteration.
         *
         * @return the x-coordiante of the current pixel.
         */
        @Override
        public int getX() {
            return x+dx;
        }

        /**
         * Returns the y-coordiante of the current pixel in the iteration.
         *
         * @return the y-coordiante of the current pixel.
         */
        @Override
        public int getY() {
            return y+dy;
        }
    } // End of inner class Tile
       
}
