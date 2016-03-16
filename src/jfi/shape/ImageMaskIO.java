/**
  Class for reading/writing mask images

  @author Jesús Chamorro Martínez - UGR
*/

package jfi.shape;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageMaskIO {
   
    static public ImageMask read(File f){
        BufferedImage img;
        ImageMask mask = null;
        try{
            img = ImageIO.read(f);  
            mask = new ImageMask(img); //El constructor umbraliza si no es binaria
        }catch(Exception ex){
            System.err.println("ImageMask: Error reading image ("+ex.getMessage()+")");
        }
        return mask;
    }
    
    
}

