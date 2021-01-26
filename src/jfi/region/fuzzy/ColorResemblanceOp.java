package jfi.region.fuzzy;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import jfi.color.ISCCColorMap;
import jfi.color.fuzzy.FuzzyColor;
import jfi.color.fuzzy.FuzzyColorSpace;
import jfi.geometry.Point3D;
import jfi.image.fuzzy.FuzzyMappingOp;
import jfi.image.fuzzy.PixelFuzzyMappingOp;
import java.awt.Color;
import jfi.fuzzy.membershipfunction.TrapezoidalFunction;

/**
 * Class representing a pixel sigma_count operator where the sigma_count is
 calculated on the basis of fuzzy colors.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class ColorResemblanceOp implements PixelResemblanceOp<Point> {

    /**
     * Fuzzy color space associated to this operator.
     */
    private FuzzyColorSpace<Point3D> fcs;
    /**
     * A source image associated to this operator (if available).
     */
    private BufferedImage source = null;
    /**
     * List of image maps.
     */
    private ArrayList<BufferedImage> maps = null;
    /**
     * Type of color sigma_count applied
     */
    private int type = TYPE_AT_LEAST_ONE;
    /**
     * Type of sigma_count defines as the mean of individual resemblances.
     */
    public static final int TYPE_MEAN = 1;
    /**
     * Type of sigma_count defines as "at least" one label is resemblant.
     */
    public static final int TYPE_AT_LEAST_ONE = 2;
    /**
     * Type of sigma_count defines as "at least" one label is resemblant.
     */
    public static final int TYPE_EUCLIDEAN = 3;
    /**
     * Type of sigma_count defines as "fuzzy majority" labels are resemblants.
     */
    public static final int TYPE_MAJORITY = 4;
    /**
     * Majority function alpha parameter.
     */
    public static final double ALPHA_MAJORITY = 0.3;
    /**
     * Majority function beta parameter.
     */
    public static final double BETA_MAJORITY = 0.7;
    /**
     * Type of sigma_count defines as "weighted mean" labels are resemblants.
     */
    public static final int TYPE_WEIGHTED_MEAN = 5;
    /**
     * Membership degree threshold used to consider a label as significant.
     */
    public static final double DEGREE_THRESHOLD = 0.0;

    /**
     * Constructs a new color resemblance operator using as fuzzy color space
     * the one given by parameter. The given image will be associated to this
     * operator; this implies that a series of preliminary calculations will be
     * made in order to make the application of this operator more efficient if
     * the image on which it is applied is the one passed in this constructor.
     *
     * @param source image associated to this operator.
     * @param fcs the fuzzy color space
     */
    public ColorResemblanceOp(BufferedImage source, FuzzyColorSpace fcs) {
        this.fcs = fcs != null ? fcs : FuzzyColorSpace.Factory.createSphereBasedFCS(new ISCCColorMap(ISCCColorMap.TYPE_BASIC), 0.5);
        this.source = source;
        if (source != null) {
            this.sourceMapping(); //The texture maps are calculated
        }
    }

    /**
     * Constructs a new color sigma_count operator using as fuzzy color space
 the one given by parameter.
     *
     * @param fcs the fuzzy color space
     */
    public ColorResemblanceOp(FuzzyColorSpace fcs) {
        this(null, fcs);
    }

    /**
     * Constructs a new color resemblance operator using as default fuzzy color
     * space the sphere based one with the basic ISCC set as prototypes. The
     * given image will be associated to this operator; this implies that a
     * series of preliminary calculations will be made in order to make the
     * application of this operator more efficient if the image on which it is
     * applied is the one passed in this constructor.
     *
     * @param source image associated to this operator.
     */
    public ColorResemblanceOp(BufferedImage source) {
        this(source, null);
    }

    /**
     * Constructs a new color sigma_count operator using as default fuzzy color
 space the sphere based one with the basic ISCC set as prototypes..
     */
    public ColorResemblanceOp() {
        this(null, null);
    }

    /**
     * Calculates the mappings of the source image.
     */
    private void sourceMapping() {
        if (this.maps == null) {
            this.maps = new ArrayList();
        } else {
            maps.clear();
        }
        FuzzyMappingOp mapColor;
        for (FuzzyColor fc : fcs) {
            mapColor = new PixelFuzzyMappingOp(fc);
            maps.add(mapColor.filter(this.source, null, false));
        }
    }

    /**
     * Returns the type of color sigma_count applied.
     *
     * @return the type of color sigma_count applied.
     */
    public int getType() {
        return type;
    }

    /**
     * Set the type of color sigma_count to be applied.
     *
     * @param type the type of color sigma_count to be applied.
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Apply this sigma_count operator.
     *
     * @param t the coordinates of the first pixel.
     * @param u the coordinates of the second pixel.
     * @param image the image associated to the pixel coordinates.
     * @return the sigma_count result.
     */
    @Override
    public Double apply(Point t, Point u, BufferedImage image) {
        //If the image is the one associated to this operator, the faster apply
        //method is used (in that case, the precalculated degrees will be used)
        if (image != null && image == source) {
            return apply(t, u);
        }
        Color ct = new Color(image.getRGB(t.x, t.y));
        Color cu = new Color(image.getRGB(u.x, u.y));
        double resemblance = 0.0;
        switch (type) {
            case TYPE_MEAN:
                resemblance = applyMean(ct, cu);
                break;
            case TYPE_AT_LEAST_ONE:
                resemblance = applyAtLeastOne(ct, cu);
                break;
            case TYPE_EUCLIDEAN:
                resemblance = applyEuclidean(ct, cu);
                break;
            case TYPE_MAJORITY:
                resemblance = applyMajority(ct, cu);
                break;
            case TYPE_WEIGHTED_MEAN:
                resemblance = applyWeightedMean(ct, cu);
                break;
        }
        return resemblance;
    }

    /**
     * Apply this sigma_count operator to the associated image (if available).
     *
     * @param t the coordinates of the first pixel.
     * @param u the coordinates of the second pixel.
     * @return the sigma_count result.
     * @throws NullPointerException if there is not image associated to this
     * operator.
     */
    public Double apply(Point t, Point u) {
        if (this.source == null) {
            throw new NullPointerException("There is not image associated to this operator");
        }
        double resemblance = 0.0;
        switch (type) {
            case TYPE_MEAN:
                resemblance = applyMean_Pre(t, u);
                break;
            case TYPE_AT_LEAST_ONE:
                resemblance = applyAtLeastOne_Pre(t, u);
                break;
            case TYPE_EUCLIDEAN:
                resemblance = applyEuclidean_Pre(t, u);
                break;
            case TYPE_MAJORITY:
                resemblance = applyMajority_Pre(t, u);
                break;
            case TYPE_WEIGHTED_MEAN:
                resemblance = applyWeightedMean_Pre(t, u);
                break;
        }
        return resemblance;
    }

    /**
     * Apply this sigma_count operator using the "at least one" approach.
     *
     * @param ct the color of the first pixel.
     * @param cu the color of the second pixel.
     * @return the sigma_count result.
     */
    private Double applyAtLeastOne(Color ct, Color cu) {
        //The sigma_count is calculated on the basis of the color fuzzy sets. 
        //It is assumed that the sigma_count between two fuzzy sets is 1.0 if
        //and only if they are the same set, 0.0 in other case. This assumption
        //simplifies the sigma_count calculus
        double degreeT, degreeU;
        double resemblanceTU, resemblance = 0.0;
        for (FuzzyColor fc : fcs) {
            degreeT = fc.membershipDegree(ct);
            degreeU = fc.membershipDegree(cu);
            //Author´s proposal: "At least one" approach            
            resemblanceTU = (degreeU > DEGREE_THRESHOLD && degreeT > DEGREE_THRESHOLD) ? 1.0 - Math.abs(degreeU - degreeT) : 0.0;
            resemblance = Math.max(resemblance, resemblanceTU);
        }
        return resemblance;
    }

    /**
     * Apply this sigma_count operator using the "at least one" approach and the
 pre-calculated membership degrees associated to the source image.
     *
     * @param ct the color of the first pixel.
     * @param cu the color of the second pixel.
     * @return the sigma_count result.
     */
    private Double applyAtLeastOne_Pre(Point t, Point u) {
        //Since the membership degrees are precalculated, we just need to access
        //to the degree values in each fuzzy texture map.
        double degreeT, degreeU;
        double resemblanceTU, resemblance = 0.0;
        for (BufferedImage img : this.maps) {
            degreeT = ((double) img.getRaster().getSample(t.x, t.y, 0)) / 255.0;
            degreeU = ((double) img.getRaster().getSample(u.x, u.y, 0)) / 255.0;
            //Author´s proposal: "At least one" approach
            resemblanceTU = (degreeU > DEGREE_THRESHOLD && degreeT > DEGREE_THRESHOLD) ? 1.0 - Math.abs(degreeU - degreeT) : 0.0;
            resemblance = Math.max(resemblance, resemblanceTU);
        }
        return resemblance;
    }

    /**
     * Apply this sigma_count operator using the "mean" approach.
     *
     * @param ct the color of the first pixel.
     * @param cu the color of the second pixel.
     * @return the sigma_count result.
     */
    private Double applyMean(Color ct, Color cu) {
        //The sigma_count is calculated on the basis of the color fuzzy sets. 
        //It is assumed that the sigma_count between two fuzzy sets is 1.0 if
        //and only if they are the same set, 0.0 in other case. This assumption
        //simplifies the sigma_count calculus
        double degreeT, degreeU;
        double resemblance = 0.0;
        int n_pair = 0;
        for (FuzzyColor fc : fcs) {
            degreeT = fc.membershipDegree(ct);
            degreeU = fc.membershipDegree(cu);
            //Author´s proposal: mean of resemblances  
            if (degreeU > DEGREE_THRESHOLD && degreeT > DEGREE_THRESHOLD) {
                resemblance += 1.0 - Math.abs(degreeU - degreeT);
                n_pair++;
            }
        }
        return n_pair > 0 ? resemblance / n_pair : 0.0;
    }

    /**
     * Apply this sigma_count operator using the "mean" approach and the
 pre-calculated membership degrees associated to the source image.
     *
     * @param ct the color of the first pixel.
     * @param cu the color of the second pixel.
     * @return the sigma_count result.
     */
    private Double applyMean_Pre(Point t, Point u) {
        //Since the membership degrees are precalculated, we just need to access
        //to the degree values in each fuzzy texture map.
        double degreeT, degreeU;
        double resemblance = 0.0;
        int n_pair = 0;
        for (BufferedImage img : this.maps) {
            degreeT = ((double) img.getRaster().getSample(t.x, t.y, 0)) / 255.0;
            degreeU = ((double) img.getRaster().getSample(u.x, u.y, 0)) / 255.0;
            //Author´s proposal: mean of resemblances  
            if (degreeU > DEGREE_THRESHOLD && degreeT > DEGREE_THRESHOLD) {
                resemblance += 1.0 - Math.abs(degreeU - degreeT);
                n_pair++;
            }
        }
        return n_pair > 0 ? resemblance / n_pair : 0.0;
    }

    /**
     * Apply this sigma_count operator using the "Euclidean" approach.
     *
     * @param ct the color of the first pixel.
     * @param cu the color of the second pixel.
     * @return the sigma_count result.
     */
    private Double applyEuclidean(Color ct, Color cu) {
        //The sigma_count is calculated on the basis of the color fuzzy sets. 
        //It is assumed that the sigma_count between two fuzzy sets is 1.0 if
        //and only if they are the same set, 0.0 in other case. This assumption
        //simplifies the sigma_count calculus
        double NORMALIZATION_VALUE = Math.sqrt(fcs.size());
        double degreeT, degreeU;
        double resemblance = 0.0;
        for (FuzzyColor fc : fcs) {
            degreeT = fc.membershipDegree(ct);
            degreeU = fc.membershipDegree(cu);
            resemblance += Math.pow(degreeU - degreeT, 2);
        }
        resemblance = Math.sqrt(resemblance);
        return 1.0 - (Math.min(1.0, resemblance / NORMALIZATION_VALUE));
    }

    /**
     * Apply this sigma_count operator using the "Euclidean" approach and the
 pre-calculated membership degrees associated to the source image.
     *
     * @param ct the color of the first pixel.
     * @param cu the color of the second pixel.
     * @return the sigma_count result.
     */
    private Double applyEuclidean_Pre(Point t, Point u) {
        //Since the membership degrees are precalculated, we just need to access
        //to the degree values in each fuzzy texture map.
        double NORMALIZATION_VALUE = Math.sqrt(fcs.size());
        double degreeT, degreeU;
        double resemblance = 0.0;
        for (BufferedImage img : this.maps) {
            degreeT = ((double) img.getRaster().getSample(t.x, t.y, 0)) / 255.0;
            degreeU = ((double) img.getRaster().getSample(u.x, u.y, 0)) / 255.0;
            resemblance += Math.pow(degreeU - degreeT, 2);
        }
        resemblance = Math.sqrt(resemblance);
        return 1.0 - (Math.min(1.0, resemblance / NORMALIZATION_VALUE));
    }

    /**
     * Apply this color resemblance operator using the "majority" approach.
     *
     * @param ct the color of the first pixel.
     * @param cu the color of the second pixel.
     * @return the resemblance result.
     */
    private Double applyMajority(Color ct, Color cu) {
        double degreeT, degreeU;
        double sigma_count = 0.0;
        int n_pair = 0;
        for (FuzzyColor fc : fcs) {
            degreeT = fc.membershipDegree(ct);
            degreeU = fc.membershipDegree(cu);
            
            if (degreeU > DEGREE_THRESHOLD || degreeT > DEGREE_THRESHOLD) {
                sigma_count += 1.0 - Math.abs(degreeU - degreeT);
                n_pair++;
            }
        }
        TrapezoidalFunction q_majority = new TrapezoidalFunction(ALPHA_MAJORITY, BETA_MAJORITY, 1, 1);
        return q_majority.apply(n_pair > 0 ? sigma_count / n_pair : 0.0);
    }

    /**
     * Apply this color resemblance operator using the "majority" approach and the
 pre-calculated membership degrees associated to the source image.
     *
     * @param ct the color of the first pixel.
     * @param cu the color of the second pixel.
     * @return the sigma_count result.
     */
    private Double applyMajority_Pre(Point t, Point u) {
        //Since the membership degrees are precalculated, we just need to access
        //to the degree values in each fuzzy texture map.
        double degree_t, degree_u;
        double sigma_count = 0.0;
        int n_pair = 0;
        for (BufferedImage img : this.maps) {
            degree_t = ((double) img.getRaster().getSample(t.x, t.y, 0)) / 255.0;
            degree_u = ((double) img.getRaster().getSample(u.x, u.y, 0)) / 255.0;
            
            if (degree_u > DEGREE_THRESHOLD || degree_t > DEGREE_THRESHOLD) {
                if (degree_u == 0 || degree_t == 0){
                    sigma_count += 0;
                }
                else {
                    sigma_count += 1.0 - Math.abs(degree_u - degree_t);
                }
                n_pair++;
            }
        }
        
        TrapezoidalFunction q_majority = new TrapezoidalFunction(ALPHA_MAJORITY, BETA_MAJORITY, 1, 1);
        return q_majority.apply(n_pair > 0 ? sigma_count / n_pair : 0.0);
    }

    /**
     * Apply this color resemblance operator using the "weigthed mean" approach.
     *
     * @param ct the color of the first pixel.
     * @param cu the color of the second pixel.
     * @return the resemblance result.
     */
    private Double applyWeightedMean(Color ct, Color cu) {
        double resemblance = 0;
        double degree_t, degree_u;

        double degreesum_t = 0, degreesum_u = 0;
        for (FuzzyColor fc : fcs) {
            degreesum_t += fc.membershipDegree(ct);
            degreesum_u += fc.membershipDegree(cu);
        }
        for (FuzzyColor fc : fcs) {
            degree_t = fc.membershipDegree(ct);
            degree_u = fc.membershipDegree(cu);
            if (degree_t > degree_u) {
                resemblance += (1 - Math.abs(degree_t - degree_u)) * degree_u / degreesum_u;
            } else {
                resemblance += (1 - Math.abs(degree_t - degree_u)) * degree_t / degreesum_t;
            }
        }

        return resemblance;
    }

    /**
     * Apply this color resemblance operator using the "weigthed mean" approach and
 the pre-calculated membership degrees associated to the source image.
     *
     * @param ct the color of the first pixel.
     * @param cu the color of the second pixel.
     * @return the resemblance result.
     */
    private Double applyWeightedMean_Pre(Point t, Point u) {
        //Since the membership degrees are precalculated, we just need to access
        //to the degree values in each fuzzy texture map.
        double resemblance = 0;
        double degree_t, degree_u;

        double degreesum_t = 0, degreesum_u = 0;
        for (BufferedImage img : this.maps) {
            degreesum_t += ((double) img.getRaster().getSample(t.x, t.y, 0)) / 255.0;
            degreesum_u += ((double) img.getRaster().getSample(u.x, u.y, 0)) / 255.0;
        }

        for (BufferedImage img : this.maps) {
            degree_t = ((double) img.getRaster().getSample(t.x, t.y, 0)) / 255.0;
            degree_u = ((double) img.getRaster().getSample(u.x, u.y, 0)) / 255.0;
            if (degree_t > degree_u) {
                resemblance += (1 - Math.abs(degree_t - degree_u)) * degree_u / degreesum_u;
            } else {
                resemblance += (1 - Math.abs(degree_t - degree_u)) * degree_t / degreesum_t;
            }
        }

        return resemblance;
    }
}
