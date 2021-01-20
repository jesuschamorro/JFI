package jfi.region.fuzzy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import jfi.fuzzy.operators.TNorm;

/**
 * Class implementing a fuzyy segmentation based on a region growing process.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es) and Míriam Mengíbar
 * Rodríguez (mirismr@correo.ugr.es)
 */
public class FuzzySegmentationOrderedListOp {

    /**
     * The seeds os this segmentation.
     */
    private final List<Point> seeds;
    /**
     * The boundary of the region which give us the set of candidates in each
     * iterarion.
     */
    OrderedList candidates;
    /**
     * A binary (1-bit) image used for checking the state (visited or not) of a
     * pixel in the region growing process.
     */
    private BufferedImage check = null;
    /**
     * A flag to set if the seeds are calculated automatically.
     */
    private boolean auto;
    /**
     * Pixel resemblance operator used to compare adjacent pixels
     */
    PixelResemblanceOp resemblanceOp;
    /**
     * Pixel resemblance operator used by default.
     */
    static private PixelResemblanceOp DEFAULT_RESEMBLANCE = new DefaultResemblanceOp();
    /**
     * T-norm used to aggregate resemblances.
     */
    TNorm tnorm = TNorm.PRODUCT;
    /**
     * A flag to set the type of output, that is, if the fuzzy regions are grey
     * images or color ones with alpha channel.
     */
    private boolean outputAsGrayImage = false;

    /**
     * A binary (1-bit) image used for checking if a point is present in
     * candidates list.
     */
    private BufferedImage checkPresentPoints = null;

    /**
     * Constructs a new fuzzy segmentation operator. The resemblance operator
     * {@link #DEFAULT_RESEMBLANCE} is used by default.
     *
     * @param seed the first seed.
     * @param seeds the others seeds.
     */
    public FuzzySegmentationOrderedListOp(Point seed, Point... seeds) {
        this.seeds = new ArrayList(Arrays.asList(seeds));
        this.seeds.add(0, seed);
        this.candidates = new OrderedList();
        this.auto = false;
        this.resemblanceOp = DEFAULT_RESEMBLANCE;
    }

    /**
     * Constructs a new fuzzy segmentation operator using as seeds the ones in
     * the given collection. The resemblance operator
     * {@link #DEFAULT_RESEMBLANCE} is used by default.
     *
     * @param seeds list of seeds.
     */
    public FuzzySegmentationOrderedListOp(Collection<Point> seeds) {
        this.seeds = new ArrayList(seeds); //A copy is created
        this.candidates = new OrderedList();
        this.auto = false;
        this.resemblanceOp = DEFAULT_RESEMBLANCE;
    }

    /**
     * Constructs a new fuzzy segmentation operator. The seeds are calculated
     * automatically.
     *
     * @param resemblanceOp the pixel resemblance operator used to compare
     * adjacent pixels.
     */
    public FuzzySegmentationOrderedListOp(PixelResemblanceOp resemblanceOp) {
        this.seeds = new ArrayList(); //Empty list
        //Seeds should be calculated automatically for each image.
        this.auto = true;
        this.resemblanceOp = resemblanceOp;
    }

    /**
     * Constructs a new fuzzy segmentation operator. The seeds are calculated
     * automatically and the resemblance operator {@link #DEFAULT_RESEMBLANCE}
     * is used by default.
     *
     */
    public FuzzySegmentationOrderedListOp() {
        this(DEFAULT_RESEMBLANCE);
    }

    /**
     * Apply this operator to the given image.
     *
     * @param image the image to be segmented.
     * @return the image segmentation
     */
    public FuzzySegmentation apply(BufferedImage image) {
        if (auto) {
            seeds.clear();
            //TODO: Call to the method that calculates the seeds automatically
            throw new UnsupportedOperationException("Automatically seed selection "
                    + "is not supported yet.");
        }
        FuzzySegmentation fSegmentation = new FuzzySegmentation();
        //The check image is created as a binary (1-bit) image
        check = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        checkPresentPoints = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        //For each seed, a fuzzy region is calculated
        for (Point seed : seeds) {
            if (isInsideImageBounds(seed)) {
                FuzzyRegion fRegion = calculateFuzzyRegion(image, seed);
                fSegmentation.add(fRegion);
            }
        }
        check = null;
        checkPresentPoints = null;
        System.gc();

        return fSegmentation;
    }

    /**
     * Calculates the fuzzy region associated to the given seed.
     *
     * @param image the image to be segmented.
     * @param seed the region seed.
     * @return
     */
    private FuzzyRegion calculateFuzzyRegion(BufferedImage image, Point seed) {
        //A fuzzy refion is created; by default, membership degrees are set to zero
        FuzzyRegion fRegion = outputAsGrayImage
                ? new FuzzyRegion(image.getWidth(), image.getHeight()) //Grey image
                : new FuzzyRegion(image);      // Color image with alpha channel
        //The check images is reset to zero-value and also the candidate set
        clearCheckImage();
        candidates.clear();
        //The seed is added to the region, and their neighborhood as candidates
        addRegionPoint(new PointData(seed.x, seed.y, 1.0, 1.0), fRegion, image);
        //Region growing process
        PointData selected;
        while (!candidates.isEmpty()) {
            //We select the candidate with the greatest degree. Since the 
            //list is ordered, by default, using the natural ordering, it will 
            //be the last one of the list.
            selected = candidates.poll();
            addRegionPoint(selected, fRegion, image);
        }
        return fRegion;
    }

    /**
     * Adds the given point to the region.
     *
     * @param p the point to be added.
     */
    private void addRegionPoint(PointData p, FuzzyRegion fRegion, BufferedImage image) {
        //If the pixel is not yet in the region...
        if (check.getRaster().getSample(p.x, p.y, 0) == 0) {
            //The pixel is set as visited
            ((WritableRaster) check.getRaster()).setSample(p.x, p.y, 0, 1);
            //The neighborhood pixels are added as candidates 
            this.addNeighborhoodPixels(p, image);
            //The membership degree is calculated when the point is selected as 
            //candidate (and, if applicable, it is updated in the growing process)
            fRegion.setMembershipDegree(p, p.degree);
        }
    }

    /**
     * Adds as candidates the neighborhood pixels of the given point.
     *
     * @param p the point.
     */
    private void addNeighborhoodPixels(PointData p, BufferedImage image) {
        PointData pcandidate;
        int newx, newy;
        double resemblance, degree;

        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                newx = p.x + ix;
                newy = p.y + iy;
                if (newx >= 0 && newx < check.getWidth() && newy >= 0 && newy < check.getHeight()) {
                    //If the point is inside the image bounds...
                    if (check.getRaster().getSample(newx, newy, 0) == 0) {
                        //...and it is not inside the current region, is candidate
                        resemblance = resemblanceOp.apply(p, new Point(newx, newy), image);
                        degree = tnorm.apply(p.degree, resemblance);
                        pcandidate = new PointData(newx, newy, resemblance, degree);
                        //The new candidate is added
                        candidates.add(pcandidate);
                    }
                }
            }
        }
    }

    /**
     * Resets the check images to zero-values.
     */
    private void clearCheckImage() {
        if (check != null) {
            Graphics2D g2d = check.createGraphics();
            g2d.setPaint(Color.BLACK);
            g2d.fill(new Rectangle(0, 0, check.getWidth(), check.getHeight()));
        }
        if (checkPresentPoints != null) {
            Graphics2D g2d = checkPresentPoints.createGraphics();
            g2d.setPaint(Color.BLACK);
            g2d.fill(new Rectangle(0, 0, checkPresentPoints.getWidth(), checkPresentPoints.getHeight()));
        }
    }

    /**
     * Adds the specified seed to the list of seeds.
     *
     * @param seed seed to be appended to this list
     * @return <tt>true</tt> if added.
     */
    public boolean addSeed(Point seed) {
        return seeds.add(seed);
    }

    /**
     * Set the pixel resemblance operator for this object. It allows the use of
     * lambda expressions
     *
     * @param op the new pixel resemblance operator.
     */
    final public void setPixelResemblanceOp(PixelResemblanceOp op) {
        this.resemblanceOp = op;
    }

    /**
     * Set the default pixel resemblance operator for this class. This operator
     * is used when a specific one is not provided in the object construction.
     *
     * @param op the new pixel resemblance operator. If the given parameter is
     * null, a {@link #DEFAULT_RESEMBLANCE} operator is assigned.
     */
    static public void setDefaultPixelResemblanceOp(PixelResemblanceOp op) {
        DEFAULT_RESEMBLANCE = op != null ? op : new DefaultResemblanceOp();
    }

    /**
     * Set the t-norm used to aggregate resemblances.
     *
     * @param tnorm t-norm used to aggregate resemblances.
     */
    public void setTNorm(TNorm tnorm) {
        if (tnorm != null) {
            this.tnorm = tnorm;
        }
    }

    /**
     * Set the type of image output, that is, if the fuzzy regions are gray
     * images or color ones with alpha channel.
     *
     * @param gray if <tt>true</tt>, gray images are used as output; else, color
     * ones with alpha channel.
     */
    public void setGrayAsOutput(boolean gray) {
        this.outputAsGrayImage = gray;
    }

    /**
     * Check if the given point is inside the image bounds.
     *
     * @param p the point to be checked.
     * @return <tt>true</tt> if the point is inside; <tt>false</tt> otherwise.
     */
    private boolean isInsideImageBounds(Point p) {
        return p.x >= 0 && p.x < check.getWidth()
                && p.y >= 0 && p.y < check.getHeight();
    }

    /**
     * Inner class representing a point data.
     */
    static public class PointData extends Point implements Comparable<PointData> {

        public static final double NO_VALUE = -1.0;
        /**
         * The resemblance value associated to this point
         */
        public java.lang.Double resemblance;
        /**
         * The membership degree associated to this point
         */
        public java.lang.Double degree;

        /**
         * Constructs and initializes a point at the specified {@code (x,y)}
         * location in the coordinate space.
         *
         * @param x the X coordinate
         * @param y the Y coordinate
         * @param resemblance the resemblance data associated to this point.
         */
        public PointData(int x, int y, double resemblance) {
            super(x, y);
            this.resemblance = resemblance;
            this.degree = NO_VALUE;
        }

        public PointData(int x, int y, double resemblance, double degree) {
            super(x, y);
            this.resemblance = resemblance;
            this.degree = degree;
        }

        /**
         * Compares this point data with the given parameter for order. Returns
         * a negative integer, zero, or a positive integer as this point data is
         * less than, equal to, or greater than the given parameter.
         *
         * @param p the point data to be compared
         * @return the comparision results
         */
        @Override
        public int compareTo(PointData p) {
            return degree.compareTo(p.degree);
        }

        @Override
        public boolean equals(Object e) {
            PointData pt = (PointData) e;
            return (x == pt.x) && (y == pt.y);
        }

        /**
         * Returns a string representation of this objetc.
         *
         * @return a string representation of this object.
         */
        @Override
        public String toString() {
            return "[" + x + "," + y + "](" + resemblance + ")";
        }
    }

    public class OrderedList extends ArrayList<PointData> {

        private OrderedList() {
        }

        private boolean orderedAdd(PointData p) {
            int index = 0;
            if (!this.isEmpty()) {
                PointData pi = this.get(index);
                while (pi.compareTo(p) == -1 && index < this.size() - 1) {
                    index++;
                    pi = this.get(index);
                }
                super.add(index, p);
            } else {
                super.add(0, p);
            }

            return true;
        }

        @Override
        public boolean add(PointData candidate) {
            boolean added = false;
            // candidate is present in candidates list
            if (checkPresentPoints.getRaster().getSample(candidate.x, candidate.y, 0) == 1) {
                // check if any point have less resemblance than candidate
                PointData p = this.stream().filter(pcurrent -> pcurrent.equals(candidate) && pcurrent.compareTo(candidate) == -1).findFirst().orElse(null);
                // update if any point have less resemblance with the new resemblance
                if (p != null) {
                    super.remove(p);
                    added = this.orderedAdd(candidate);
                }
            } // if not present, add candidate
            else {
                added = this.orderedAdd(candidate);
            }

            if (added) {
                ((WritableRaster) checkPresentPoints.getRaster()).setSample(candidate.x, candidate.y, 0, 1);
            }
            return added;
        }

        public PointData poll() {
            PointData out = this.get(this.size() - 1);
            this.remove(this.size() - 1);
            return out;
        }
    }

    /**
     * Functional (inner) class implementing a pixel resemblance operator.
     */
    static public class DefaultResemblanceOp implements PixelResemblanceOp<Point> {

        static private final double MAX_RGBDIST = 441.673; //~sqrt(255^2+255^2+255^2) 
        static private double NORMALIZATION_VALUE = MAX_RGBDIST;

        /**
         * Apply this pixel resemblance operator. The operator is based on the
         * RGB distance; specifically, it is defined as:
         *
         * 1.0 - MIN(1.0, RGBDiff/{@link #NORMALIZATION_VALUE})
         *
         * with RGBDiff the Euclidean distance between p and q RGB values.
         *
         *
         * @param t first point.
         * @param u second point.
         * @param image the image.
         * @return the resemblance between pixels.
         */
        @Override
        public Double apply(Point t, Point u, BufferedImage image) {
            Color c1 = new Color(image.getRGB(t.x, t.y));
            Color c2 = new Color(image.getRGB(u.x, u.y));
            double rDif = Math.pow(c1.getRed() - c2.getRed(), 2);
            double gDif = Math.pow(c1.getGreen() - c2.getGreen(), 2);
            double bDif = Math.pow(c1.getBlue() - c2.getBlue(), 2);
            double dif = Math.sqrt(rDif + gDif + bDif);

            return 1.0 - (Math.min(1.0, dif / NORMALIZATION_VALUE));
        }

        /**
         * Set the normalization value used to calculate the resemblance degree.
         *
         * @param value the normalization value. It must be a value between 1
         * and {@link #MAX_RGBDIST}.
         *
         */
        public void setNormalizationValue(double value) {
            NORMALIZATION_VALUE = Math.max(Math.min(value, MAX_RGBDIST), 1.0);
        }
    }
}