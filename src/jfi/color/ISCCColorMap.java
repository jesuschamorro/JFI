package jfi.color;

import java.security.InvalidParameterException;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import jfi.geometry.Point3D;

/**
 * Class for storing the ISCC–NBS color data.
 *
 * <p>
 * The ISCC–NBS System of Color Designation is a system for naming colors based
 * on a set of basic color terms and a small set of adjective modifiers. It was
 * first established in the 1930s by a joint effort of the Inter-Society Color
 * Council, made up of delegates from various American trade organizations, and
 * the National Bureau of Standards, a US government agency. In 1976, "The Color
 * Names Dictionary" and "The Universal Color Language" were combined and
 * updated with the publication of "Color: Universal Language and Dictionary of
 * Names", the definitive source on the ISCC–NBS system.
 * </p>
 *
 * ISCC-NBS provides several color sets:
 *
 * <ul>
 * <li> The basic set: 13 color names corresponding to ten basic color terms
 * (pink, red, orange, yellow, brown, olive, green, blue, violet, purple), and 3
 * achromatic ones (white, gray, and black).
 * <li> The extended Set: 31 color names corresponding to those of the basic set
 * and some combination of them formed by adding the −ish suffix (Brownish
 * Orange, Purplish Blue among others).
 * <li> The complete set: 267 color names obtained from the extended set by
 * adding five tone modifiers for lightness (very light, light, medium, dark and
 * very dark) and four adjectives for saturation (grayish, moderate, strong and
 * vivid). Also, three additional terms substitute certain lightness-saturation
 * combination (pale for light grayish, brilliant for light strong and deep for
 * dark strong).
 * </ul>
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class ISCCColorMap extends LinkedHashMap<String,Point3D>{
    /**
     * Type constant associated to the basic set of 13 colors.
     */
    static public final int TYPE_BASIC = 1;
    /**
     * Type constant associated to the extended set of 31 colors.
     */
    static public final int TYPE_EXTENDED = 2;
    /**
     * Type constant associated to the complete set of 267 colors.
     */
    static public final int TYPE_COMPLETE = 3;
    /**
     * The type of ISCC color set used. 
     */
    private final int type;
    
    
    /**
     * Constructs a new map of ISCC colors. By default, the basic set of
     * thirteen colors is used.
     */
    public ISCCColorMap() {
        this(TYPE_BASIC);
    }
    
    /**
     * Constructs a new map of ISCC colors.
     * 
     * @param type the set of colors used. 
     */
    public ISCCColorMap(int type) {
        Map.Entry set[];
        switch(type){
            case TYPE_BASIC:
                set = ISCC_BASIC;
                break;
            case TYPE_EXTENDED:
                set = ISCC_EXTENDED;
                break;    
            default:    
                throw new InvalidParameterException("Unknown type");
        }
        for (Entry<String, Point3D> e : set) {
            this.put(e.getKey(), e.getValue());
        }
        this.type = type;
    }
    
    /**
     * Returns the type of this ISCC color map.
     * 
     * @return the type of this ISCC color map.
     */
    public int getType(){
        return type;
    }
    
    /**
     * Set of ISCC basic colors.
     */
    static public Map.Entry ISCC_BASIC[] = {
        new AbstractMap.SimpleEntry("Pink", new Point3D(254, 181, 186)),
        new AbstractMap.SimpleEntry("Red", new Point3D(190, 1, 50)),
        new AbstractMap.SimpleEntry("Orange", new Point3D(243, 132, 1)),
        new AbstractMap.SimpleEntry("Brown", new Point3D(128, 70, 27)),
        new AbstractMap.SimpleEntry("Yellow", new Point3D(243, 195, 1)),
        new AbstractMap.SimpleEntry("Olive", new Point3D(102, 93, 30)),
        new AbstractMap.SimpleEntry("Yellow-green", new Point3D(141, 182, 1)),
        new AbstractMap.SimpleEntry("Green", new Point3D(1, 136, 86)),
        new AbstractMap.SimpleEntry("Blue", new Point3D(1, 161, 194)),
        new AbstractMap.SimpleEntry("Purple", new Point3D(154, 78, 174)),
        new AbstractMap.SimpleEntry("White", new Point3D(252, 252, 249)),
        new AbstractMap.SimpleEntry("Gray", new Point3D(135, 134, 134)),
        new AbstractMap.SimpleEntry("Black", new Point3D(7, 7, 7))
    };
    
    /**
     * Set of ISCC extended colors.
     */
    static public Map.Entry ISCC_EXTENDED[] = {
        // Pink family 
        new AbstractMap.SimpleEntry("Pink", new Point3D(254, 181, 186)),
        new AbstractMap.SimpleEntry("YellowishPink", new Point3D(254, 183, 165)),
        new AbstractMap.SimpleEntry("BrownishPink", new Point3D(194, 172, 153)),
        new AbstractMap.SimpleEntry("PurplishPink", new Point3D(230, 143, 172)),
        // Red family
        new AbstractMap.SimpleEntry("Red", new Point3D(190, 1, 50)),
        new AbstractMap.SimpleEntry("PurplishRed", new Point3D(206, 70, 118)),
        // Orange family        
        new AbstractMap.SimpleEntry("Orange", new Point3D(243, 132, 1)),        
        new AbstractMap.SimpleEntry("YellowOrange", new Point3D(246, 166, 1)),
        new AbstractMap.SimpleEntry("ReddishOrange", new Point3D(226, 88, 34)),
        new AbstractMap.SimpleEntry("BrownishOrange", new Point3D(174, 105, 56)),
        // Brown family
        new AbstractMap.SimpleEntry("Brown", new Point3D(128, 70, 27)),
        new AbstractMap.SimpleEntry("ReddishBrown", new Point3D(136, 45, 23)),
        new AbstractMap.SimpleEntry("YellowishBrown", new Point3D(153, 101, 21)),
        new AbstractMap.SimpleEntry("OliveBrown", new Point3D(107, 79, 13)),
        // Yellow family
        new AbstractMap.SimpleEntry("Yellow", new Point3D(243, 195, 1)),        
        new AbstractMap.SimpleEntry("GreenishYellow", new Point3D(220, 211, 1)),
        // Olive family
        new AbstractMap.SimpleEntry("Olive", new Point3D(102, 93, 30)),
        new AbstractMap.SimpleEntry("GreenOlive", new Point3D(64, 79, 1)),
        // Yellow-green family
        new AbstractMap.SimpleEntry("Yellow-green", new Point3D(141, 182, 1)),
        // Green family
        new AbstractMap.SimpleEntry("Green", new Point3D(1, 136, 86)),        
        new AbstractMap.SimpleEntry("YellowishGreen", new Point3D(39, 166, 76)),
        new AbstractMap.SimpleEntry("BluishGreen", new Point3D(1, 136, 130)),
        // Blue family
        new AbstractMap.SimpleEntry("Blue", new Point3D(1, 161, 194)),
        new AbstractMap.SimpleEntry("GreenishBlue", new Point3D(1, 133, 161)),
        new AbstractMap.SimpleEntry("PurplishBlue", new Point3D(48, 38, 122)),
        // Purple family        
        new AbstractMap.SimpleEntry("Purple", new Point3D(154, 78, 174)),
        new AbstractMap.SimpleEntry("Violet", new Point3D(144, 101, 202)),
        new AbstractMap.SimpleEntry("ReddishPurple", new Point3D(135, 1, 116)),
        // Grey level family
        new AbstractMap.SimpleEntry("White", new Point3D(252, 252, 249)),
        new AbstractMap.SimpleEntry("Gray", new Point3D(135, 134, 134)),
        new AbstractMap.SimpleEntry("Black", new Point3D(7, 7, 7))
    };
}
