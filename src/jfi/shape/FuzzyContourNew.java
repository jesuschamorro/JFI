package jfi.shape;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import jfi.fuzzy.DiscreteFuzzySet;

/**
 *
 * @author Jesús Chamorro Martínez
 */
public class FuzzyContourNew extends DiscreteFuzzySet<Point2D> {

    /**
     * Constructs an empty fuzzy contour with an empty label.
     */
    public FuzzyContourNew() {
    }

    /**
     * Constructs an empty fuzzy contour.
     *
     * @param label label of the fuzzy set
     */
    public FuzzyContourNew(String label) {
        super(label);
    }

    /**
     * Constructs a fuzzy contour from an mask image setting all the membership
     * values to 1.0.
     *
     * It is assumed that the mask contains a single connected component. If
     * not, only the contour of the first shape (starting from the top-left) is
     * created
     *
     * @param mask the mask image
     */
    public FuzzyContourNew(ImageMask mask) {
        //TODO
    }

    /**
     * Constructs a fuzzy contour from a crisp contour setting all the
     * membership values to 1.0.
     *
     * @param c the crisp contour
     */
    public FuzzyContourNew(Contour c) {
        //TODO
    }

    /**
     * Appends the specified point to fuzzy contour with degree 1.0.
     *
     * @param p point to be appended to this list
     * @return <tt>true</tt>
     */
    public boolean add(Point2D p) {
        return this.add(p, 1.0);

    }

    /**
     * Draws the contour points into an image
     *
     * @param use_degrees if true, the membership degrees will be used for
     * greylevel estimation
     * @return an image with the contour drawn
     */
    public BufferedImage toImage(boolean use_degrees) {
        BufferedImage img = null;
        if (use_degrees) {
            //TODO
        } else {
            //TODO
        }
        return img;
    }

}
