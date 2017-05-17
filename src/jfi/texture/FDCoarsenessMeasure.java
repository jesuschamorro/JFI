package jfi.texture;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import jfi.color.GreyColorSpace;


/**
 * 
 * Calculation of the Fractal Dimension measure with the Epsilon-Blanket method
 * (Peleg, S. et al "Multiple resolution texture analysis and classification"
 * in "IEEE Transactions on pattern analysis and machine intelligence" 6, No.4,
 * pp 518-523, 1984)
 * @author Pedro M. Martínez Jiménez
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FDCoarsenessMeasure implements TextureMeasure<Double> {

    private int distance;
    private int epsilon;

    public static final int DEFAULT_DISTANCE = 1;
    public static final int DEFAULT_EPSILON = 8;

    /**
     * Constructs the measure object using the default parameters.
     */
    public FDCoarsenessMeasure() {
        this(DEFAULT_DISTANCE, DEFAULT_EPSILON);
    }
    
    /**
     * Constructs the measure object using the parameter "distance".
     *
     * @param distance distance parameter of the Epsilon-Blanket method.
     */
    public FDCoarsenessMeasure(int distance) {
        this(distance, DEFAULT_EPSILON);
    }

    /**
     * Constructs the measure object using the parameters "distance" and "epsilon".
     *
     * @param distance distance parameter of the Epsilon-Blanket method.
     * @param epsilon epsilon parameter of the Epsilon-Blanket method.
     */
    public FDCoarsenessMeasure(int distance, int epsilon) {
        this.distance = distance;
        this.epsilon = epsilon;
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
        return FDMeasure(grayscaleImage);
    }
    
    
    private double FDMeasure(BufferedImage I){
        int rows = I.getHeight();
        int cols = I.getWidth();       
        int d = distance;
        
        double[] A = new double[epsilon];
        double[] v = new double[epsilon + 1];
        double[] epsilons = new double[epsilon];
        
        int dto, ind, tam = rows * cols;
        int[] u = new int[tam];
        int[] b = new int[tam];
        int[] u_act = new int[tam];
        int[] b_act = new int[tam];

        List<Point> positions = buscaPosiciones(d);
        
        v[0] = 0;
        // u and b are initialized to the same values of the image
        u = I.getRaster().getSamples(0, 0, cols, rows, 0, u);
        b = I.getRaster().getSamples(0, 0, cols, rows, 0, b);

        // Go through all the possible values of epsilon (from 1 to the value of the parameter "epsilon")
        for (int eps = 1; eps <= epsilon; eps++) {
             v[eps] = 0.0;
            // Go through all the pixels in the image
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    ind = i * cols + j;
                     u_act[ind] =  u[ind] + 1;
                     b_act[ind] =  b[ind] - 1;

                    // Search for the extreme values of the blankets by examining the neighbours
                    for (Point pos : positions) {
                        if ((i +  pos.x >= 0) && (i +  pos.x < rows) && (j +  pos.y >= 0) && (j +  pos.y < cols)) {
                            dto = (i +  pos.x) * cols + j +  pos.y;
                            if (u[dto] >  u_act[ind]) 
                                u_act[ind] = u[dto];
                            if (b[dto] <  b_act[ind])
                                b_act[ind] = b[dto];
                        }
                    }
                    v[eps] +=  u_act[ind] -  b_act[ind];
                }
            }

            A[eps - 1] = Math.log((v[eps] -  v[eps - 1]) / 2);
            // Calculate the logarithm in order to perform the "fit"
            epsilons[eps - 1] = Math.log((float) eps);
            
            System.arraycopy(u_act, 0, u, 0, tam);
            System.arraycopy(b_act, 0, b, 0, tam);
        }

        double pendiente = fit(epsilons, A, epsilon);
        return 2 - pendiente;
    }
    
    
    private List<Point> buscaPosiciones(int d){
        List<Point> pos = new ArrayList<>();
        for (int i = -d; i <= d; i++) 
            for (int j = -d; j <= d; j++) 
                if ((Math.sqrt((float) (i*i + j*j)) <= d) && !((i == 0) && (j == 0)))
                    pos.add(new Point(i,j));
        return pos;
    }
    
    
    private double fit(double[] x, double[] y, int ndata){
        double t, sxoss, sx = 0.0, st2 = 0.0;
        double b = 0;
        for (int i = 0; i < ndata; i++)
            sx += x[i];
        sxoss = sx / ndata;
        for (int i = 0; i < ndata; i++) {
            t = x[i] - sxoss;
            st2 += t * t;
            b += t * y[i];
        }
        return b /= st2;
    }

        
}
