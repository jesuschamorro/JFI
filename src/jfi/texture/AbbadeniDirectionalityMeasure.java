package jfi.texture;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

/**
 *
 * @author Pedro M. Martínez Jiménez
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.)
 */
public class AbbadeniDirectionalityMeasure implements TextureMeasure<Double> {

    private int numBins;

    public static final int NUM_BINS = 30;

    /**
     * Constructs the measure object using the default parameter.
     */
    public AbbadeniDirectionalityMeasure() {
        this(NUM_BINS);
    }
    

    /**
     * Constructs the measure object using the parameter "numBins".
     *
     * @param numBins parameter of the Abbadeni directionality measure.
     */
    public AbbadeniDirectionalityMeasure(int numBins) {
        this.numBins = numBins;
    }

    @Override
    public Double apply(BufferedImage image) {
        BufferedImage grayscaleImage;
        if (image.getColorModel().getColorSpace().getType() != ColorSpace.TYPE_GRAY){
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);  
            ColorConvertOp op = new ColorConvertOp(cs, null);  
            grayscaleImage = op.filter(image, null);
        }
        else
            grayscaleImage = image;
        return abbadeniMeasure(grayscaleImage);
    }

    
    private double abbadeniMeasure(BufferedImage I) {
        int rows = I.getHeight();
        int cols = I.getWidth();
        int[] img = null;
        img = I.getRaster().getSamples(0, 0, cols, rows, 0, img);
        double[] f = new double[rows * cols];
        double suma;

        // Compute the autocovariance according Abbadeni
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                suma = 0.f;
                for (int k = 0; k < rows - i; k++)
                    for (int m = 0; m < cols - j; m++) 
                        suma += img[k * cols + m] * img[(k + i) * cols + j + m];
                f[i * cols + j] = suma / ((rows - i) * (cols - j));
            }
        }

        // Compute the directionality according Abbadeni
        double dir_deltaH, dir_deltaV;
        double dir_delta, dir_theta;

        // These two vectors are initialized to 0.
        int[] dir_N = new int[numBins];
        double[] dir_H = new double[numBins];

        // Compute gradient at each pixel and count bins
        suma = 0;
        for (int y = 1; y < rows - 1; y++) {
            for (int x = 1; x < cols - 1; x++) {
                dir_deltaH = PrewittOperatorHd(f, cols, y, x);
                dir_deltaV = PrewittOperatorVd(f, cols, y, x);
                suma += Math.sqrt(Math.pow(dir_deltaH, 2) + Math.pow(dir_deltaV, 2));
            }
        }

        double umbral = suma / (double) ((rows - 1) * (cols - 1));
        for (int y = 1; y < rows - 1; y++) {
            for (int x = 1; x < cols - 1; x++) {
                dir_deltaH = PrewittOperatorHd(f, cols, y, x);
                dir_deltaV = PrewittOperatorVd(f, cols, y, x);
                dir_delta = Math.sqrt(Math.pow(dir_deltaH, 2) + Math.pow(dir_deltaV, 2));
                if (dir_delta < umbral)
                    continue;
                if (dir_deltaH == 0)
                    dir_theta = 0;
                else
                    dir_theta = Math.atan2(dir_deltaV, dir_deltaH) + Math.PI / 2.0;
               
                if (dir_theta >= 0 && dir_theta < Math.PI)
                    dir_N[(int) Math.floor((double)numBins * dir_theta / Math.PI)]++;
            }
        }

        // Compute sum of values in all bins
        int SumofN = 0;
        for (int k = 0; k < numBins; k++)
            SumofN += dir_N[k];

        // Thresholding to obtain the bins in the dominant directions
        double directionality = 0.0;
        umbral = 2 / (double) numBins;
        for (int k = 0; k < numBins; k++) {
            dir_H[k] = (double) dir_N[k] / SumofN;
            if (dir_H[k] > umbral)
                directionality += dir_H[k];
        }
        System.out.println(directionality);
        return directionality;
    }

    private double PrewittOperatorHd(double[] I, int cols, int y, int x) {
        double result = 0;
        result += (I[(y + 1) * cols + (x - 1)] - I[(y - 1) * cols + (x - 1)]);
        result += (I[(y + 1) * cols + (x)] - I[(y - 1) * cols + (x)]);
        result += (I[(y + 1) * cols + (x + 1)] - I[(y - 1) * cols + (x + 1)]);
        return result;
    }

    double PrewittOperatorVd(double[] I, int cols, int y, int x) {
        double result = 0;
        result += (I[(y - 1) * cols + (x - 1)] - I[(y - 1) * cols + (x + 1)]);
        result += (I[(y) * cols + (x - 1)] - I[(y) * cols + (x + 1)]);
        result += (I[(y + 1) * cols + (x - 1)] - I[(y + 1) * cols + (x + 1)]);
        return result;
    }
    
}
