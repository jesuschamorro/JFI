package jfi.texture;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;
import jfi.color.GreyColorSpace;

/**
 *
 * @author Pedro M. Martínez Jiménez
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class CorrelationCoarsenessMeasure implements TextureMeasure<Double> {

    private int distance;
    private int greyLevels;

    public static final int DEFAULT_DISTANCE = 1;
    public static final int DEFAULT_GREY_LEVELS = 256;

    /**
     * Constructs the measure object using the default parameters.
     */
    public CorrelationCoarsenessMeasure() {
        this(DEFAULT_DISTANCE, DEFAULT_GREY_LEVELS);
    }
    
    /**
     * Constructs the measure object using the parameter "distance".
     *
     * @param distance distance parameter of the Haralick Correlation measure.
     */
    public CorrelationCoarsenessMeasure(int distance) {
        this(distance, DEFAULT_GREY_LEVELS);
    }

    /**
     * Constructs the measure object using the parameters "distance" and "greyLevels".
     *
     * @param distance distance parameter of the Haralick Correlation measure.
     * @param greyLevels number of grey levels of the image (Only 256 grey levels supported).
     */
    public CorrelationCoarsenessMeasure(int distance, int greyLevels) {
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
            jfi.color.ColorConvertOp op = new jfi.color.ColorConvertOp(cs, null);  
            grayscaleImage = op.filter(image, null, false);
        }
        return correlationMeasure(grayscaleImage);
    }
    
    
    private double correlationMeasure(BufferedImage I){
        final Point[] angulos = new Point[4];
        angulos[0] = new Point(0,1);   // Angle 0
        angulos[1] = new Point(-1, 1);  // Angle 45
        angulos[2] = new Point(-1, 0);  // Angle 90
        angulos[3] = new Point(-1, -1); // Angle 135
	ResultGLCM result_glcm;
        double corr;
        double max_corr = java.lang.Double.MAX_VALUE;

        for (Point angulo : angulos) {
            result_glcm = calculateGLCM(I, angulo);
            corr = calculateCorr(result_glcm);
            if (Math.abs(corr) < Math.abs(max_corr))
                max_corr = corr;
        }
        return max_corr;
    }
    
    
    private ResultGLCM calculateGLCM(BufferedImage I, Point angulo) {
        int rows = I.getHeight();
        int cols = I.getWidth();
        int[] img = null;
        img = I.getRaster().getSamples(0, 0, cols, rows, 0, img);
        int df, dc, total = 0, fila, columna;
        df = angulo.x;
        dc = angulo.y;
        
        double[] glcm = new double[greyLevels * greyLevels];
        int[] indices = new int[greyLevels * greyLevels];
        int numind;

        numind = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ((i + df * distance >= 0) && (i + df * distance < rows)
                        && (j + dc * distance >= 0) && (j + dc * distance < cols)) {
                    fila = img[i * cols + j];
                    columna = img[(i + df * distance) * cols + j + dc * distance];
                    if (glcm[fila * greyLevels + columna] == 0.0) {
                        indices[numind] = fila * greyLevels + columna;
                        numind++;
                    }
                    glcm[fila * greyLevels + columna]++;
                    total++;

                    //////////////// AÑADO ESTO
                    columna = img[i * cols + j];
                    fila = img[(i + df * distance) * cols + j + dc * distance];
                    if (glcm[fila * greyLevels + columna] == 0.0) {
                        indices[numind] = fila * greyLevels + columna;
                        numind++;
                    }
                    glcm[fila * greyLevels + columna]++;
                    total++;
                }
            }
        }

        for (int conta = 0; conta < numind; conta++)
            glcm[indices[conta]] /= total;

        return new ResultGLCM(glcm, indices, numind);
    }

        
    private double calculateCorr(ResultGLCM result_glcm) {
        double[] glcm = result_glcm.getGLCM();
        int[] indices = result_glcm.getIndices();
        int numind = result_glcm.getNumInd();
        int i, j;
        double suma = 0.0;
        double mu_i = 0.0;
        double mu_j = 0.0;
        double sigma_i = 0.0;
        double sigma_j = 0.0;

        for (int conta = 0; conta < numind; conta++) {
            i = (int) (indices[conta] / greyLevels);
            j = indices[conta] % greyLevels;
            mu_i += i * glcm[indices[conta]];
            mu_j += j * glcm[indices[conta]];
        }
        for (int conta = 0; conta < numind; conta++) {
            i = (int) (indices[conta] / greyLevels);
            j = indices[conta] % greyLevels;
            sigma_i += (i - mu_i) * (i - mu_i) * glcm[indices[conta]];
            sigma_j += (j - mu_j) * (j - mu_j) * glcm[indices[conta]];
            suma += (i - mu_i) * (j - mu_j) * glcm[indices[conta]];
        }

        sigma_i = Math.sqrt(sigma_i);
        sigma_j = Math.sqrt(sigma_j);

        double div = sigma_i * sigma_j;
        if (div == 0) {
            suma = 0;
        } else {
            suma /= div;
        }
        return suma;
    }
    
    
    private class ResultGLCM{
        private final double[] glcm;
        private final int[] indices;
        private final int numind;
        
        public ResultGLCM(double[] glcm, int[] indices, int numind){
            this.glcm = glcm;
            this.indices = indices;
            this.numind = numind;
        }
        
        public double[] getGLCM(){
            return this.glcm;
        }
        
        public int[] getIndices(){
            return this.indices;
        }
        
        public int getNumInd(){
            return numind;
        }
    }
    
}
