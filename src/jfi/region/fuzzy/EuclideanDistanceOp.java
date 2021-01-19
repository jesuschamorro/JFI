package jfi.region.fuzzy;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Class representing a pixel resemblance operator where the resemblance is
 * calculated on the basis of Euclidean distance of the coordinates (no color
 * information is used).
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class EuclideanDistanceOp implements PixelResemblanceOp<Point> {

    /**
     * If defined, the distance is calculated to this point
     */
    private Point seed = null;

    /**
     * Constructs a new Euclidean-based resemblance operator.
     */
    public EuclideanDistanceOp() {
        this(null);
    }

    /**
     * Constructs a new Euclidean-based resemblance operator.
     * 
     * @param seed if is not null, the distance is calculated to this point.
     */
    public EuclideanDistanceOp(Point seed) {
        this.seed = seed;
    }

    /**
     * Apply this pixel resemblance operator. The operator is based on the
     * Euclidean distance of the coordinates (no color information is used).
     *
     * @param t first point.
     * @param u second point.
     * @param image the image.
     * @return the resemblance between pixels.
     */
    @Override
    public Double apply(Point t, Point u, BufferedImage image) {
        if (seed != null) {
            return apply(u, image);
        }
        double MAX = Math.sqrt(image.getWidth() * image.getWidth() + image.getHeight() * image.getHeight());
        double difx = Math.pow(t.x - u.x, 2);
        double dify = Math.pow(t.y - u.y, 2);
        double dif = Math.sqrt(difx + dify);

        return 1.0 - (Math.min(1.0, dif / MAX));
    }

    public Double apply(Point u, BufferedImage image) {
        double MAX = Math.sqrt(image.getWidth() * image.getWidth() + image.getHeight() * image.getHeight());
        double difx = Math.pow(seed.x - u.x, 2);
        double dify = Math.pow(seed.y - u.y, 2);
        double dif = Math.sqrt(difx + dify);

        return 1.0 - (Math.min(1.0, dif / MAX));
    }
}
