package jfi.events;

import java.awt.Color;
import java.awt.Point;
import java.util.EventObject;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class PixelEvent extends EventObject {
    private final Point pixel_location;
    private final Color rgb_value;
    private final Integer alpha_value; // Value in [0..255]
    
    public PixelEvent(Object source, Point pixel_location, Color rgb_value, Integer alpha_value) {
        super(source);
        this.pixel_location = pixel_location;
        this.rgb_value = rgb_value;
        this.alpha_value = alpha_value;
    }
    
    public Point getPixelLocation(){
        return pixel_location;
    }
    
    public Color getRGB(){
        return rgb_value;
    }
    
    public Integer getAlpha(){
        return alpha_value;
    }
}
