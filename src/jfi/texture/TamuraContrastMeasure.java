package jfi.texture;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import jfi.color.GreyColorSpace;

/**
 *
 * @author Pedro M. Martínez Jiménez
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.)
 */
public class TamuraContrastMeasure implements TextureMeasure<Double> {

    /**
     * Constructs the measure object (this measure has no parameters).
     */
    public TamuraContrastMeasure() {
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
           
        double mean = 0.0;
        double variance = 0.0;
        double fourth_moment = 0.0;

        /*compute mean--average of grey level */   
        for (int y = 0; y < rows; y++){
            for (int x = 0; x < cols; x++)
                mean += img[y*cols + x];
        }   
        mean /= (rows * cols);
               
        /*compute variance*/   
        for (int y = 0; y < rows; y++ ){
            for (int x = 0; x < cols; x++)
                variance += (Math.pow(img[y*cols + x] - mean, 2));
        }   
        variance /= (rows * cols);
        variance  = Math.pow(variance, 1.0/2.0);
                       
        /*compute the 4th moment*/   
        for (int y = 0; y < rows; y++ ) 
            for (int x = 0; x < cols; x++)  
                fourth_moment += (Math.pow(img[y*cols + x] - mean, 4));
 
        fourth_moment /= (cols * rows);   
        fourth_moment = Math.pow(fourth_moment, 1.0/4.0);   
                       
        /*compute contrast*/ 
        double contrast = 0.0; 
        if (fourth_moment != 0){
            double alfa = fourth_moment / Math.pow(variance, 4.0); 
            contrast = variance / Math.pow(alfa, 1.0/4.0);
        }

        return contrast/1000;
    }

}
