package jfi.texture;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;
import jfi.color.ColorConvertOp;
import jfi.color.GreyColorSpace;

/**
 *
 * @author Pedro M. Martínez Jiménez
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.)
 */
public class AmadasunCoarsenessMeasure implements TextureMeasure<Double> {

    private int distance;
    private int greyLevels;

    public static final int DEFAULT_DISTANCE = 1;
    public static final int DEFAULT_GREY_LEVELS = 256;

    /**
     * Constructs the measure object using the default parameters.
     */
    public AmadasunCoarsenessMeasure() {
        this(DEFAULT_DISTANCE, DEFAULT_GREY_LEVELS);
    }
    
    /**
     * Constructs the measure object using the parameter "distance".
     *
     * @param distance distance parameter of the Amadasun measure.
     */
    public AmadasunCoarsenessMeasure(int distance) {
        this(distance, DEFAULT_GREY_LEVELS);
    }

    /**
     * Constructs the measure object using the parameters "distance" and "greyLevels".
     *
     * @param distance distance parameter of the Amadasun measure.
     * @param greyLevels number of grey levels of the image (Only 256 grey levels supported).
     */
    public AmadasunCoarsenessMeasure(int distance, int greyLevels) {
        if (greyLevels != DEFAULT_GREY_LEVELS)
            throw new InvalidParameterException("Only 256 grey levels supported");
        else{
            this.distance = distance;
            this.greyLevels = greyLevels;
        }
    }

    @Override
    public Double apply(BufferedImage image) {
        BufferedImage grayscaleImage;
        if (image.getRaster().getNumBands() == 1)
            grayscaleImage = image;        
        else{
            ColorSpace cs = new GreyColorSpace();
            ColorConvertOp op = new ColorConvertOp(cs, null);  
            grayscaleImage = op.filter(image, null, false);
        }
        return amadasunMeasure(grayscaleImage);
    }

    
    private double amadasunMeasure(BufferedImage I) {
        int d = distance;
        int rows = I.getHeight();
        int cols = I.getWidth();
        int[] img = null;
        img = I.getRaster().getSamples(0, 0, cols, rows, 0, img);
        final double cte = java.lang.Double.MIN_VALUE;
        int suma, indice, pixel, n_2;
        double A_i, grosor;
        double W = Math.pow(2.0 * d + 1, 2);

        // This reduction is needed to make the measure independent to the image size.
        int reduccion = 32;
        int tam_p = greyLevels / reduccion;
        
        // These two vectors are initialized to 0.
        double[] s = new double[tam_p];
        double[] p = new double[tam_p];

        if ((rows - d < 0) || (cols - d < 0))
            throw new IllegalArgumentException("The image size is too small to apply the measure.");

        for (int k = d; k < rows - d; k++) {
            for (int l = d; l < cols - d; l++) {
                pixel = img[k * cols + l];
                suma = 0;
                for (int m = k - d; m <= k + d; m++) {
                    for (int j = l - d; j <= l + d; j++) {
                        suma += img[m * cols + j];
                    }
                }
                A_i = ((double) (suma - pixel)) / (W - 1);
                indice = (int) Math.floor((double) pixel / (double) reduccion);
                p[indice] += 1;
                s[indice] += Math.abs(pixel - A_i);
            }
        }
        grosor = 0.0;
        n_2 = (rows - 2 * d) * (cols - 2 * d);
        for (int i = 0; i < tam_p; i++) {
            grosor += (p[i] / n_2) * s[i];
        }
        grosor /= n_2;
        grosor = 1.0 / (grosor + cte);
        return grosor;
    }
    
    
    @Override
    public String toString(){
        return "Amadasun fineness measure";
    }

    /*public int[] transformaNivelesGris(BufferedImage bi) {
        WritableRaster wr = bi.getRaster();
        int[] imagGris = null;
        int ancho = bi.getWidth(), alto = bi.getHeight(), tam = ancho * alto;

        if (wr.getNumBands() == 3) {
            int[] x = new int[tam];
            int[] y = new int[tam];
            int[] z = new int[tam];
            imagGris = new int[tam];
            x = wr.getSamples(0, 0, ancho, alto, 0, x);
            y = wr.getSamples(0, 0, ancho, alto, 1, y);
            z = wr.getSamples(0, 0, ancho, alto, 2, z);
            for (int i = 0; i < tam; i++) {
                imagGris[i] = (int) Math.round(0.30 * x[i] + 0.59 * y[i] + 0.11 * z[i]);
            }
        } else if (wr.getNumBands() == 1) {
            imagGris = new int[tam];
            imagGris = wr.getSamples(0, 0, ancho, alto, 0, imagGris);
        }
        return imagGris;
    }*/

}
