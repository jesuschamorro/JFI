/*
  Image representing a binary mask

  @author Jesús Chamorro Martínez - UGR
*/

package jfi.shape;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageMask extends BufferedImage{
    
    public ImageMask(int width, int height){
        super(width,height,BufferedImage.TYPE_BYTE_BINARY);
    }
    
    public ImageMask(BufferedImage image){
        this(image.getWidth(),image.getHeight());
        Graphics2D g2d = this.createGraphics();
        g2d.drawImage(image,0,0,null); //Umbraliza la imagen 
    }
   
}

