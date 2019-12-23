package jfi.region;

import java.awt.Color;
import java.awt.Point;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator over an image region. In each iteration, it produces an object of 
 * type <tt>T</tt>.
 * 
 * @param <T> the type of elements returned by this iterator.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public abstract class RegionIterator<T> implements Iterator<T> {
    /**
     * The source image region associated to this iterator.
     */
    protected Region source;

    /**
     * Constructs a new iterator for the given region. The purpose of this
     * (unique) constructor is to force its call from all the subclases in order
     * to set the source region (and others parameters dependent on it).
     *
     * @param source the source region associated to this iterator.
     */
    protected RegionIterator(Region source) {
        this.setRegion(source);
    }

    /**
     * Set the source region (and others subclass parameters, if applicable).
     * 
     * @param region the source region. 
     */
    public abstract void setRegion(Region region);

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
     * Inner class defining a particular region iterator that (1) goes over all
     * the pixels in the region and (2) for each pixel, returns a
     * {@link java.awt.Color} objetc representing the color at the given
     * location. It is the standard iterator for an image region.
     */
    public static class Pixel extends RegionIterator<Color> {        
        /**
         * The bounds width of the source region
         */
        private int width;
        /**
         * The length of the region bounds (that is, the number of pixels in the
         * region bounds). Note that this lengtn could be different that the
         * region length (that is, the number of pixels inside the region)
         */
        private int length;
        /**
         * Current position in the iteration.
         */
        private int pos;
        /**
         * The x-coordiante of the current pixel in the iteration.
         */
        private int nextx ;
        /**
         * The x-coordiante of the next pixel in the iteration.
         */
        private int currentx;
        /**
         * The y-coordiante of the current pixel in the iteration.
         */
        private int nexty;
        /**
         * The y-coordiante of the next pixel in the iteration.
         */
        private int currenty;
        /**
         *  Location (upper-left corner) of the region inside the image.
         */
        private Point location;
        /**
         * Color analyzed in each 'next' call. For reasons of efficiency, it is
         * declared as a class member variable (instead of a local one in the
         * 'next' method).
         */
        private Color color;
        
        /**
         * Constructs a new pixel-based iterator.
         * 
         * @param region the source image region. 
         */
        public Pixel(Region region) {
            super(region);
        }
        
        /**
         * Set the source image and initializes the local parameters.
         *
         * @param region the source region.
         */
        @Override
        public void setRegion(Region region) {
            this.source = region;
            if (region != null) {
                width = region.getBounds().width;
                length = region.getBounds().height * width;
                location = region.getBounds().getLocation();                
                init(); //Sets iterator position to the initial one     
            } else {
                pos = length = width = 0;
                currentx = currenty = -1;
            }
        }
        
        /**
         * Sets iterator position to the initial one.
         */
        public void init(){
            pos = 0;            
            nextx = location.x;
            nexty = location.y;
            while (pos<length && !source.isInside(nextx,nexty)) {
                pos++;
                nextx = (pos % width)+location.x;
                nexty = (pos / width)+location.y;
            }
            //At this point, (x,y) stores the coordinate of the first point in 
            //the region
            currentx = currenty = -1;
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
         * {@link java.awt.Color}, where the three components of the color 
         * are stored.
         * @throws NoSuchElementException if the iteration has no more elements.
         */
        @Override
        public Color next() {
            if (pos >= length) {
                throw new NoSuchElementException("No more pixels");
            }
            color = source.getRGB(nextx,nexty);
            // The current position is stored. It corresponds to the pixel 
            // position whose color is returned by this method
            currentx = nextx;      
            currenty = nexty;
            this.updatePosition(); // The next position is updated

            return color;
        }

        /**
         *  Updates the current position and the associated pixel coordinates.
         */
        private void updatePosition() {
            do {
                pos++;
                nextx = (pos % width) + location.x;
                nexty = (pos / width) + location.y;                
            } while (pos < length && !source.isInside(nextx, nexty));
        }
        
        /**
         * Returns the x-coordiante of the current pixel in the iteration.
         *
         * @return the x-coordiante of the current pixel.
         * @throws NoSuchElementException if the iteration has not been started.
         */
        @Override
        public int getX() {
            if (currentx<0) {
                throw new NoSuchElementException("The iteration must have "
                        + "started to know the current value of x");
            }
            return currentx;
        }

        /**
         * Returns the y-coordiante of the current pixel in the iteration.
         *
         * @return the y-coordiante of the current pixel.
         * @throws NoSuchElementException if the iteration has not been started.
         */
        @Override
        public int getY() {
            if (currenty < 0) {
                throw new NoSuchElementException("The iteration must have "
                        + "started to know the current value of y");
            }
            return currenty;
        }
    } // End of inner class Pixel
    
}
