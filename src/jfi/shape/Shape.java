/*
  Class representing a Shape

  @author Jesús Chamorro Martínez - UGR
*/
package jfi.shape;

import java.awt.image.BufferedImage;
import java.util.Properties;

public class Shape {
    private BufferedImage sourceImage = null;
    private ImageMask mask = null; 
    Properties properties;
    
    /**
     * 
     * @param img
     * @param mask 
     */
    public Shape(BufferedImage img, ImageMask mask){
        this.sourceImage = img;
        this.mask = mask;  
        this.properties = null;
    }
    /**
     * 
     * @return 
     */
    public Contour getContour(){
        return null;
    }
    
}
