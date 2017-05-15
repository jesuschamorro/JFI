package jfi.texture;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.security.InvalidParameterException;

/**
 *
 * @author Pedro M. Martínez Jiménez
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class HaralickContrastMeasure implements TextureMeasure<Double> {

    private int distance;
    private int greyLevels;

    public static final int DEFAULT_DISTANCE = 1;
    public static final int DEFAULT_GREY_LEVELS = 256;

    /**
     * Constructs the measure object using the default parameters.
     */
    public HaralickContrastMeasure() {
        this(DEFAULT_DISTANCE, DEFAULT_GREY_LEVELS);
    }
    
    /**
     * Constructs the measure object using the parameter "distance".
     *
     * @param distance distance parameter of the Haralick Contrast measure.
     */
    public HaralickContrastMeasure(int distance) {
        this(distance, DEFAULT_GREY_LEVELS);
    }

    /**
     * Constructs the measure object using the parameters "distance" and "greyLevels".
     *
     * @param distance distance parameter of the Haralick Contrast measure.
     * @param greyLevels number of grey levels of the image (Only 256 grey levels supported).
     */
    public HaralickContrastMeasure(int distance, int greyLevels) {
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
        if (image.getColorModel().getColorSpace().getType() != ColorSpace.TYPE_GRAY){
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);  
            ColorConvertOp op = new ColorConvertOp(cs, null);  
            grayscaleImage = op.filter(image, null);
        }
        else
            grayscaleImage = image;
        return haralickMeasure(grayscaleImage);
    }
    
    
    private double haralickMeasure(BufferedImage I){
        final Point[] angulos = new Point[4];
        angulos[0] = new Point(0,1);   // Angle 0
        angulos[1] = new Point(-1, 1);  // Angle 45
        angulos[2] = new Point(-1, 0);  // Angle 90
        angulos[3] = new Point(-1, -1); // Angle 135
	ResultGLCM result_glcm;
        double contrast;
        double max_contrast = -java.lang.Double.MAX_VALUE;

        for (Point angulo : angulos) {
            result_glcm = calculateGLCM(I, angulo);
            contrast = calculateContrast(result_glcm);
            if (contrast > max_contrast)
                max_contrast = contrast;
        }
        System.out.println(max_contrast/1000);
        return max_contrast/1000;
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

        
    private double calculateContrast(ResultGLCM result_glcm) {
        double[] glcm = result_glcm.getGLCM();
        int[] indices = result_glcm.getIndices();
        int numind = result_glcm.getNumInd();
        int i, j;
        double suma = 0.0;

        for (int conta = 0; conta < numind; conta++) {
            i = (int) (indices[conta] / greyLevels);
            j = indices[conta] % greyLevels;
            suma += (i-j)*(i-j) * glcm[indices[conta]];
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
