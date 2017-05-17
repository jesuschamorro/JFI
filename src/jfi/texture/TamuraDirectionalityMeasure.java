package jfi.texture;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import jfi.color.GreyColorSpace;

/**
 *
 * @author Pedro M. Martínez Jiménez
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.)
 */
public class TamuraDirectionalityMeasure implements TextureMeasure<Double> {

    private int tamuDirN;
    private int tamuDirT;
    private double  tamuDirR;

    public static final int TAMURA_DIR_N = 16;
    public static final int TAMURA_DIR_T = 12;
    public static final double TAMURA_DIR_R = 0.2;

    /**
     * Constructs the measure object using the default parameters.
     */
    public TamuraDirectionalityMeasure() {
        this(TAMURA_DIR_N, TAMURA_DIR_T, TAMURA_DIR_R);
    }
    
    /**
     * Constructs the measure object using the parameter "tamuDirN".
     *
     * @param tamuDirN parameter N of the Tamura directionality measure.
     */
    public TamuraDirectionalityMeasure(int tamuDirN) {
        this(tamuDirN, TAMURA_DIR_T, TAMURA_DIR_R);
    }
    
    /**
     * Constructs the measure object using the parameter "tamuDirN" and "tamuDirT".
     *
     * @param tamuDirN parameter N of the Tamura directionality measure.
     * @param tamuDirT parameter T of the Tamura directionality measure.
     */
    public TamuraDirectionalityMeasure(int tamuDirN, int tamuDirT) {
        this(tamuDirN, tamuDirT, TAMURA_DIR_R);
    }

    /**
     * Constructs the measure object using the parameters "tamuDirN", "tamuDirT" and "tamuDirR".
     *
     * @param tamuDirN parameter N of the Tamura directionality measure.
     * @param tamuDirT parameter T of the Tamura directionality measure.
     * @param tamuDirR parameter R of the Tamura directionality measure.
     */
    public TamuraDirectionalityMeasure(int tamuDirN, int tamuDirT, double tamuDirR) {
        this.tamuDirN = tamuDirN;
        this.tamuDirT = tamuDirT;
        this.tamuDirR = tamuDirR;
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
        return tamuraMeasure(grayscaleImage);
    }

    
    private double tamuraMeasure(BufferedImage I) {
        int rows = I.getHeight();
        int cols = I.getWidth();
        int[] img = null;
        img = I.getRaster().getSamples(0, 0, cols, rows, 0, img);

        double dir_delta, dir_theta, dir_deltaH, dir_deltaV;

        int vertex1;  // The largest bin's index, and usually the left one
        int vertex2;  // The second large bin's index, and usually  the right one

        // Bin value of corresponding index
        double vertex1_H, vertex2_H;

        // These two vectors are initialized to 0.
        int[] dir_N = new int[tamuDirN];
        double[] dir_H = new double[tamuDirN];
        double directionality = 0.0;

        // Compute gradient at each pixel and count bins
        for (int y = 1; y < rows - 1; y++) {
            for (int x = 1; x < cols - 1; x++) {
                dir_deltaH = (double) PrewittOperatorH(img, cols, y, x);
                dir_deltaV = (double) PrewittOperatorV(img, cols, y, x);
                dir_delta = (double) (Math.abs(dir_deltaH) + Math.abs(dir_deltaV)) / 2.0;
                if (dir_delta < tamuDirT)
                    continue;
                if (dir_deltaH == 0)
                    dir_theta = 0;
                else
                    dir_theta = Math.atan2(dir_deltaV, dir_deltaH) + Math.PI / 2.0;
                
                if (dir_theta >= 0 && dir_theta < Math.PI)
                    dir_N[(int) Math.floor((double)tamuDirN * dir_theta / Math.PI)]++;
            }
        }

        // Compute sum of values in all bins
        int SumofN = 0;
        for (int k = 0; k < tamuDirN; k++) 
            SumofN += dir_N[k];

        if ((double) SumofN / (double) (rows * cols) < 0.05)
            return directionality;
  
        vertex1 = 0;
        vertex2 = 0;
        vertex1_H = 0;
        vertex2_H = 0;
        for (int k = 0; k < tamuDirN; k++) {
            dir_H[k] = (double) dir_N[k] / SumofN;
            if (vertex1_H < dir_H[k]) {
                vertex2 = vertex1;
                vertex2_H = vertex1_H;
                vertex1 = k;
                vertex1_H = dir_H[k];
            } else {
                if (vertex2_H < dir_H[k]) {
                    vertex2 = k;
                    vertex2_H = dir_H[k];
                }
            }
        }

        // If no pixel has directionality return 0.0
        if (vertex1_H == 0)
            return directionality;

        // If no pixel exists with other directionality than that related to vertex1
        if (vertex2_H == 0) {
            for (int k = 0; k < tamuDirN; k++)  
                directionality += (Math.pow((double) circularDist(k, vertex1, tamuDirN) * (Math.PI / (double) tamuDirN), 2) * dir_H[k]);
            directionality = 1 - tamuDirR * directionality;
            return directionality;
        }

        // For simplity of computation, let vertex1 denote the left one of the two largest bins
        if (vertex1 > vertex2) {
            int temp = vertex2;
            double temp_H = vertex2_H;
            vertex2 = vertex1;
            vertex2_H = vertex1_H;
            vertex1 = temp;
            vertex1_H = temp_H;
        }
        if (vertex1 <= 1 && vertex2 >= tamuDirN - 2) {
            double temp;
            double temp_H;
            for (int k = 0; k < tamuDirN / 2; k++) {
                temp = dir_H[k];
                dir_H[k] = dir_H[k + tamuDirN / 2];
                dir_H[k + tamuDirN / 2] = temp;
            }
            temp = vertex2;
            temp_H = vertex2_H;
            vertex2 = vertex1 + tamuDirN / 2;
            vertex2_H = vertex1_H;
            vertex1 = (int) (tamuDirN / 2 - (tamuDirN - temp));
            vertex1_H = temp_H;
        }

        int vertex;
        vertex = vertex1_H > vertex2_H ? vertex1 : vertex2;
        directionality = 0;
        for (int k = 0; k < tamuDirN; k++) {
            directionality += (Math.pow((double) circularDist(k, vertex, tamuDirN) * (Math.PI / (double) tamuDirN), 2) * dir_H[k]);
        }
        directionality = 1 - tamuDirR * directionality;

        return directionality;

    }
    
    int PrewittOperatorH(int[] I , int cols , int y , int x ){   
        int result = 0 ;   
        result += ( I[(y+1)*cols + (x-1)] - I[(y-1)*cols + (x-1)]);
        result += ( I[(y+1)*cols + (x)] - I[(y-1)*cols + (x)]);
        result += ( I[(y+1)*cols + (x+1)] - I[(y-1)*cols + (x+1)]);
        return result;   
    }   
                   
    int PrewittOperatorV(int[] I , int cols , int y , int x ){   
        int result = 0 ;   
        result += ( I[(y-1)*cols + (x-1)] - I[(y-1)*cols + (x+1)]);
        result += ( I[(y)*cols + (x-1)] - I[(y)*cols + (x+1)]);
        result += ( I[(y+1)*cols + (x-1)] - I[(y+1)*cols + (x+1)]);
        return result;   
    }  

    // In order to solve the problem with circularity in the measure of Tamura
    int circularDist(int a, int b, int tamHist){
        int tmp;
        if (a==b)
           return 0;
        else if (a>b){
           tmp=b;
           b=a;
           a=tmp;
        }
        if ((b-a) < (tamHist-b+a))
           return (b-a);
        else      
           return (tamHist-b+a); 
    }

}
