/*
  Fuzzy contour represented as a collection of fuzzy points

  @author Jesús Chamorro Martínez - UGR
*/
package jfi.shape;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import jfi.fuzzy.FuzzySet;


public class FuzzyContour extends Contour implements FuzzySet<Point2D> {

    String label;

    /**
     * Constructs an empty fuzzy contour.
     */
    public FuzzyContour() {
        super();
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
    public FuzzyContour(ImageMask mask) {
        super(mask);
        //NOTA: La llamada anterior no asigna grados
    }

    /**
     * Constructs a fuzzy contour from a crisp contour setting all the
     * membership values to 1.0.
     *
     * @param c the crisp contour
     */
    public FuzzyContour(Contour c) {
        //TODO
    }

    /**
     * Appends the specified point to the end of this list.
     *
     * @param p point to be appended to this list
     * @return <tt>true</tt>
     */
    @Override
    public boolean add(Point2D p) {
        Point2D fp = p;
        if (p != null && !(p instanceof FuzzyPoint)) {
            fp = new FuzzyPoint(p, 1.0f);
        }
        return super.add(fp);
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted
     * @param p element to be inserted
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public void add(int index, Point2D p) {
        Point2D fp = p;
        if (p != null && !(p instanceof FuzzyPoint)) {
            fp = new FuzzyPoint(p, 1.0f);
        }
        super.add(index, fp);
    }

    /**
     * Override the inherited method from ArrayList. This type of adding is not
     * supported
     */
    @Override
    public boolean addAll(Collection<? extends Point2D> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Override the inherited method from ArrayList. This type of adding is not
     * supported
     */
    @Override
    public boolean addAll(int index, Collection<? extends Point2D> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Replaces the point at the specified position in this list with the
     * specified element.
     *
     * @param index index of the point to replace
     * @param p point to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public Point2D set(int index, Point2D p) {
        return super.set(index, p);
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
            img = super.toImage();
        }
        return img;
    }

    /**
     * Return the label associated to the fuzzy contour
     *
     * @return the label associated to the fuzzy contour
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Set the label associated to the fuzzy contour
     *
     * @param label the new label
     */
    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Return the alpha-cut of the fuzzy contour for a given alpha
     *
     * @param alpha the alpha
     * @return the alpha-cut
     */
    @Override
    public Contour getAlphaCut(double alpha) {
        FuzzyPoint fp;
        Contour alpha_cut = new Contour();
        for (Point2D p : this) {
            fp = (FuzzyPoint) p;
            if (fp.degree >= alpha) {
                alpha_cut.add(fp.getElement());
            }
        }
        return alpha_cut;
    }

    /**
     * Return the kernel of the fuzzy contour
     *
     * @return the kernel of the fuzzy contour
     */
    @Override
    public Contour getKernel() {
        return getAlphaCut(1.0f);
    }

    /**
     * Return the support of the fuzzy contour
     *
     * @return the support of the fuzzy contour
     */
    @Override
    public Contour getSupport() {
        return getAlphaCut(0.0f);
    }

    /**
     * Implements the method of the interface FuzzySet, but in the case of 
     * FuzzyContour it is not supported. 
     * Use instead the method wich accept an index as parameter
     * @param p point of the contour
     */
    @Override
    public double getMembershipValue(Point2D p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Return the membership degree of index-element of the fuzzy contour
     * 
     * @param index index of the element
     * @return the membership degree
     */
    public double getMembershipValue(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return ((FuzzyPoint) this.get(index)).getDegree();
    }

}
