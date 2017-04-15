package jfi.image;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.NoSuchElementException;
import jfi.fuzzy.FuzzySet;
import jfi.geometry.Point3D;

/**
 * Class representing a fuzzy filtering on an image where the domain of the 
 * fuzzy set is a three-dimensional point. 
 * 
 * @see jfi.image.FuzzyMappingOp
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class PixelFuzzyMappingOp extends FuzzyMappingOp<Point3D>{
    /**
     * Constructs a new fuzzy mapping operator.
     * 
     * @param fuzzyset the fuzzy set of this fuzzy operator.
     */
    public PixelFuzzyMappingOp(FuzzySet<Point3D> fuzzyset){
        //super(fuzzyset, new ComponentsProducer());
        super(fuzzyset, new BufferedImageIterator.Pixel(null));
    }
    
    /**
     * Inner class representing a producer of three-dimensional points from an
     * image representing the three components of the color at a given location.
     */
    static public class ComponentsProducer implements PixelDataProducer<Point3D>, Iterator<Point3D>  {
        /**
         * Image point analyzed in each 'get' call. For reasons of efficiency,
         * it is declared as a class member variable (instead of a local one in
         * the 'get' method).
         */
        private final Point3D p = new Point3D();
        /**
         * Color analyzed in each 'get' call. For reasons of efficiency, it is
         * declared as a class member variable (instead of a local one in the
         * 'get' method).
         */
        private int color;
        
        
        private int height=0, width=0, length=0;
        private int pos = 0,x,y;
        private BufferedImage source=null;
        
        
        public ComponentsProducer(){
            
        }
        
        public ComponentsProducer(BufferedImage img) {
            this.setImage(img);
        }       

        @Override
        public void setImage(BufferedImage image){
            this.source = image;
            height = image.getHeight();
            width = image.getWidth();
            length = height * width;
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            return (pos < length);
        }

        @Override
        public Point3D next() {
            if (pos >= length) {
                throw new NoSuchElementException("No more pixels");
            }
            x = pos % width;
            y = pos / width;
            color = source.getRGB(x, y);
            p.x = (color >> 16) & 0xFF; // Red
            p.y = (color >> 8) & 0xFF;  // Green
            p.z =  color & 0xFF;        // Blue
            pos++;
            return p;
        }
        
        @Override
        public int getX(){
            return x;
        }
        
        @Override
        public int getY(){
            return y;
        }
        
        
        /**
         * Returns a three-dimensional point (the domain of the fuzzy set
         * associated to the fuzzy mapping) from the given image at the location
         * (x,y).
         *
         * @param source the source image.
         * @param x the x-coordinate of the pixel.
         * @param y the x-coordinate of the pixel.
         * @return a three-dimensional point with the three components of the 
         * color at a given location.
         */
        @Override
        public Point3D get(BufferedImage source, int x, int y) {            
            color = source.getRGB(x, y);
            p.x = (color >> 16) & 0xFF; // Red
            p.y = (color >> 8) & 0xFF;  // Green
            p.z =  color & 0xFF;        // Blue
            return p;
        }        
    }
}
