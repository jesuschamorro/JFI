package jfi.shape.fuzzy;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import jfi.fuzzy.DiscreteFuzzySet;
import jfi.shape.Contour;
import jfi.shape.ImageMask;

/**
 * A <code>FuzzyContour</code> represents a fuzzy set over the countour-points
 * domain. It has two main uses:
 * <ul>
 * <li>To represent a fuzzy property associated to the contour-points (for example, 
 *     the verticity degree of each point in the contour)
 * </li>
 * <li>To model a contour in a fuzzy way (i.e, in which degree a given point 
 *     belongs to a contour shape)
 * </li>
 * </ul>
 *
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public final class FuzzyContour extends DiscreteFuzzySet<Point2D> {

    /**
     * Constructs an empty fuzzy contour with an empty label.
     */
    public FuzzyContour() {
        super();
    }

    /**
     * Constructs an empty fuzzy contour.
     *
     * @param label label of the fuzzy set
     */
    public FuzzyContour(String label) {
        super(label);
    }

    /**
     * Constructs a fuzzy contour from a mask image setting all the membership
     * values to 1.0.
     *
     * It is assumed that the mask contains a single connected component. If
     * not, only the contour of the first shape (starting from the top-left) is
     * created
     *
     * @param label label of the fuzzy set
     * @param mask the mask image
     */
    public FuzzyContour(String label, ImageMask mask) {
        this(label, new Contour(mask));
    }

    /**
     * Constructs a fuzzy contour from a mask image with an empty label and all 
     * the membership values to 1.0.
     *
     * It is assumed that the mask contains a single connected component. If
     * not, only the contour of the first shape (starting from the top-left) is
     * created
     *
     * @param mask the mask image
     */
    public FuzzyContour(ImageMask mask) {
        this("",mask);
    }
    
    /**
     * Constructs a fuzzy contour from a crisp contour setting all the
     * membership values to 1.0.
     *
     * @param label label of the fuzzy se
     * @param contour the crisp contour
     */
    public FuzzyContour(String label, Contour contour) {
        this(label);
        for(Point2D point:contour){
            this.add(point);  //By default, membership degree is set to 1.0
        }
    }
    
    /**
     * Constructs a fuzzy contour from a crisp contour with an empty label and 
     * setting all the membership values to 1.0.
     *
     * @param contour the crisp contour
     */
    public FuzzyContour(Contour contour) {
        this("",contour);
    }

    /**
     * Appends the specified point to fuzzy contour with membership degree 1.0.
     *
     * @param point point to be appended to this list
     * @return <tt>true</tt>
     */
    public boolean add(Point2D point) {
        return this.add(point, 1.0);

    }

    /**
     * Draws the contour points into an image, using the membership degrees for
     * greylevel estimation.
     * 
     * @param bounded if <tt>>true</tt>, the image size will be set to the 
     * rectangle that bounds the contour (in that case, the countour point 
     * locations in the image will not necessarily match with the actual 
     * coordinates values); if <tt>>false</tt>, the actual coordinates 
     * values will be used.
     * @return an image with the contour drawn
     */
    public BufferedImage toImage(boolean bounded) {
        BufferedImage img = null;
        if (this.size() > 0) {
            Rectangle bounds = this.getBounds();
            int width = bounded ? bounds.width : bounds.width + bounds.x;
            int height = bounded ? bounds.height : bounds.height + bounds.y;
            Point offset = bounded ? bounds.getLocation() : new Point(0,0);
            img = new BufferedImage(width,height, BufferedImage.TYPE_BYTE_GRAY);
            int x, y, grey_level;

            WritableRaster imgRaster = img.getRaster();
            for (Entry<Point2D, Double> e : this) {
                x = (int) Math.round(e.getKey().getX()) - offset.x;
                y = (int) Math.round(e.getKey().getY()) - offset.y;
                grey_level = (int) (255.0 * e.getValue());
                imgRaster.setSample(x, y, 0, grey_level);
            }
        }
        return img;
    }

    /**
     * Draws the contour points into an image using the actual coordinates 
     * values (without bounded fit). For greylevel estimation, the membership 
     * degrees are used
     *       
     * @return an image with the contour drawn
     */
    public BufferedImage toImage() {
        return toImage(false);
    }
    
    /**
     * Returns an integer Rectangle that completely encloses the contour
     *
     * @return an integer Rectangle that completely encloses the contour.
     */
    public Rectangle getBounds() {
        int maxX = 0, maxY = 0;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (Entry<Point2D,Double> e : this) {
            Point2D point = e.getKey();
            if (maxX < Math.round(point.getX())) {
                maxX = (int) Math.round(point.getX());
            }
            if (minX > Math.round(point.getX())) {
                minX = (int) Math.round(point.getX());
            }
            if (maxY < Math.round(point.getY())) {
                maxY = (int) Math.round(point.getY());
            }
            if (minY > Math.round(point.getY())) {
                minY = (int) Math.round(point.getY());
            }
        }
        return new Rectangle(minX, minY, maxX-minX+1, maxY-minY+1);
    }

    /**
     * Return the crisp contour corresponding to the reference set 
     * @return the crisp contour corresponding to the reference set 
     */
    public Contour getContourReferenceSet(){
        Set reference_set = super.getReferenceSet();
        return new Contour(reference_set);
    }
    
    /**
     * Return the local maxima, in the sense of degrees, of this fuzzy contour
     * 
     * @return the local maxima
     */
    public DiscreteFuzzySet<Point2D> localMaxima(){
        FuzzyContour maxima = new FuzzyContour();
        ArrayList<Map.Entry> entry_list = new ArrayList(this.entrySet());
        double i_degree, w_degree;
        int wsize_half, w_index, w, i;
        boolean is_maximum;
        
        wsize_half = (int) (Contour.DEFAULT_WINDOW_RATIO_SIZE * entry_list.size())/2;
        for (i = 0; i < entry_list.size(); i++) {
            i_degree = (Double) entry_list.get(i).getValue();
            for (w = -wsize_half, is_maximum = true; w <= wsize_half && is_maximum; w++) {
                w_index = (i + w + entry_list.size()) % entry_list.size();
                w_degree = (Double) entry_list.get(w_index).getValue();
                if (w != 0 && i_degree <= w_degree) {
                    is_maximum = false;
                }
            }
            if (is_maximum) {
                maxima.add((Point2D) entry_list.get(i).getKey(), i_degree);
            }
        }
        return maxima;
    }
}
