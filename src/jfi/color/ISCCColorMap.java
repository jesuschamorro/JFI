package jfi.color;

import java.security.InvalidParameterException;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import jfi.geometry.Point3D;
import jfi.utils.Pair;

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
     * Type constant associated to a custom set of colors.
     */
    static public final int TYPE_CUSTOM = 4;
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
        Map.Entry set[] = {};
        switch(type){
            case TYPE_BASIC:
                set = ISCC_BASIC;
                break;
            case TYPE_EXTENDED:
                set = ISCC_EXTENDED;
                break;  
            case TYPE_COMPLETE:
                set = ISCC_COMPLETE;
                break; 
            case TYPE_CUSTOM:
                //Empty set
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
     * Returns the subset of colors from this ISCC set that matches with the
     * given pattern.
     * 
     * The pattern have to be defined on the basis of a regular expression (see
     * {@link java.util.regex.Pattern}). Patterns for the thirteen basic color
     * families are defined as constants in this class, but any regular
     * expression can be used.
     *
     * @param pattern the regular expression that define the pattern.
     * @return a subset of ISCC colors matching the pattern.
     */
    public ISCCColorMap getSubset(String pattern) {
        ISCCColorMap output = new ISCCColorMap(TYPE_CUSTOM);
        for (Entry<String, Point3D> e : this.entrySet()) {            
            if (e.getKey().matches(pattern)) {
                output.put(e.getKey(), e.getValue());
            }
        }
        return output;
    }
    
        
    /**
     * ISCC basic set of colors.
     */
    static final public Map.Entry ISCC_BASIC[] = {
        new AbstractMap.SimpleEntry("Pink", new Point3D(254, 181, 186)),
        new AbstractMap.SimpleEntry("Red", new Point3D(190, 1, 50)),
        new AbstractMap.SimpleEntry("Orange", new Point3D(243, 132, 1)),
        new AbstractMap.SimpleEntry("Brown", new Point3D(128, 70, 27)),
        new AbstractMap.SimpleEntry("Yellow", new Point3D(243, 195, 1)),
        new AbstractMap.SimpleEntry("Olive", new Point3D(102, 93, 30)),
        new AbstractMap.SimpleEntry("Yellow-Green", new Point3D(141, 182, 1)),
        new AbstractMap.SimpleEntry("Green", new Point3D(1, 136, 86)),
        new AbstractMap.SimpleEntry("Blue", new Point3D(1, 103, 194)),
        new AbstractMap.SimpleEntry("Purple", new Point3D(154, 78, 174)),
        new AbstractMap.SimpleEntry("White", new Point3D(252, 252, 249)),
        new AbstractMap.SimpleEntry("Gray", new Point3D(135, 134, 134)),
        new AbstractMap.SimpleEntry("Black", new Point3D(7, 7, 7))
    };
    
    /**
     * ISCC extended set of colors.
     */
    static final public Map.Entry ISCC_EXTENDED[] = {
        // A. Pink family 
        new AbstractMap.SimpleEntry("Pink", new Point3D(254, 181, 186)),
        new AbstractMap.SimpleEntry("Yellowish-Pink", new Point3D(254, 183, 165)),
        new AbstractMap.SimpleEntry("Brownish-Pink", new Point3D(194, 172, 153)),
        new AbstractMap.SimpleEntry("Purplish-Pink", new Point3D(230, 143, 172)),
        // B. Red family
        new AbstractMap.SimpleEntry("Red", new Point3D(190, 1, 50)),
        new AbstractMap.SimpleEntry("Purplish-Red", new Point3D(206, 70, 118)),
        // C. Orange family        
        new AbstractMap.SimpleEntry("Orange", new Point3D(243, 132, 1)),        
        new AbstractMap.SimpleEntry("Yellow-Orange", new Point3D(246, 166, 1)),
        new AbstractMap.SimpleEntry("Reddish-Orange", new Point3D(226, 88, 34)),
        new AbstractMap.SimpleEntry("Brownish-Orange", new Point3D(174, 105, 56)),
        // D. Brown family
        new AbstractMap.SimpleEntry("Brown", new Point3D(128, 70, 27)),
        new AbstractMap.SimpleEntry("Reddish-Brown", new Point3D(136, 45, 23)),
        new AbstractMap.SimpleEntry("Yellowish-Brown", new Point3D(153, 101, 21)),
        new AbstractMap.SimpleEntry("Olive-Brown", new Point3D(107, 79, 13)),
        // E. Yellow family
        new AbstractMap.SimpleEntry("Yellow", new Point3D(243, 195, 1)),        
        new AbstractMap.SimpleEntry("Greenish-Yellow", new Point3D(220, 211, 1)),
        // F. Olive family
        new AbstractMap.SimpleEntry("Olive", new Point3D(102, 93, 30)),
        new AbstractMap.SimpleEntry("Green-Olive", new Point3D(64, 79, 1)),
        // G. Yellow-green family
        new AbstractMap.SimpleEntry("Yellow-Green", new Point3D(141, 182, 1)),
        // H. Green family
        new AbstractMap.SimpleEntry("Green", new Point3D(1, 136, 86)),        
        new AbstractMap.SimpleEntry("Yellowish-Green", new Point3D(39, 166, 76)),
        new AbstractMap.SimpleEntry("Bluish-Green", new Point3D(1, 136, 130)),
        // I. Blue family
        new AbstractMap.SimpleEntry("Blue", new Point3D(1, 161, 194)),
        new AbstractMap.SimpleEntry("Greenish-Blue", new Point3D(1, 133, 161)),
        new AbstractMap.SimpleEntry("Purplish-Blue", new Point3D(48, 38, 122)),
        // J. Purple family        
        new AbstractMap.SimpleEntry("Purple", new Point3D(154, 78, 174)),
        new AbstractMap.SimpleEntry("Violet", new Point3D(144, 101, 202)),
        new AbstractMap.SimpleEntry("Reddish-Purple", new Point3D(135, 1, 116)),
        // K. Grey level family
        new AbstractMap.SimpleEntry("White", new Point3D(252, 252, 249)),
        new AbstractMap.SimpleEntry("Gray", new Point3D(135, 134, 134)),
        new AbstractMap.SimpleEntry("Black", new Point3D(7, 7, 7))
    };
    
    /**
     * ISCC complete set of colors.
     */
    static final public Map.Entry ISCC_COMPLETE[] = {
        // A. Pink family 
        // A.1 "Pink" brach (=vivid pink)
        new AbstractMap.SimpleEntry("Vivid Pink", new Point3D(254.0,181.0,186.0)),
        new AbstractMap.SimpleEntry("Strong Pink", new Point3D(234.0,147.0,153.0)),
        new AbstractMap.SimpleEntry("Deep Pink", new Point3D(228.0,113.0,122.0)),
        new AbstractMap.SimpleEntry("Light Pink", new Point3D(249.0,204.0,202.0)),
        new AbstractMap.SimpleEntry("Moderate Pink", new Point3D(222.0,165.0,164.0)),
        new AbstractMap.SimpleEntry("Dark Pink", new Point3D(192.0,128.0,129.0)),
        new AbstractMap.SimpleEntry("Pale Pink", new Point3D(234.0,216.0,215.0)),
        new AbstractMap.SimpleEntry("Grayish Pink", new Point3D(196.0,174.0,173.0)),
        // A.2 "YellowishPink" brach (=vivid yellowish-pink)
        new AbstractMap.SimpleEntry("Vivid Yellowish-Pink", new Point3D(254.0,183.0,165.0)),
        new AbstractMap.SimpleEntry("Strong Yellowish-Pink", new Point3D(248.0,131.0,121.0)),
        new AbstractMap.SimpleEntry("Deep Yellowish-Pink", new Point3D(230.0,103.0,97.0)),
        new AbstractMap.SimpleEntry("Light Yellowish-Pink", new Point3D(244.0,194.0,194.0)),
        new AbstractMap.SimpleEntry("Moderate Yellowish-Pink", new Point3D(217.0,166.0,169.0)),
        new AbstractMap.SimpleEntry("Dark Yellowish-Pink", new Point3D(196.0,131.0,121.0)),
        new AbstractMap.SimpleEntry("Pale Yellowish-Pink", new Point3D(236.0,213.0,197.0)),
        new AbstractMap.SimpleEntry("Grayish Yellowish-Pink", new Point3D(199.0,173.0,163.0)),
        // A.3 "BrownishPink" brach (=brownish-pink)
        new AbstractMap.SimpleEntry("Brownish-Pink", new Point3D(194, 172, 153)),
        // A.4 "Purplish-Pink" brach (=strong purplish-pink)
        new AbstractMap.SimpleEntry("Brilliant Purplish-Pink", new Point3D(254.0,200.0,214.0)),
        new AbstractMap.SimpleEntry("Strong Purplish-Pink", new Point3D(230.0,143.0,172.0)),
        new AbstractMap.SimpleEntry("Deep Purplish-Pink", new Point3D(222.0,111.0,161.0)),
        new AbstractMap.SimpleEntry("Light Purplish-Pink", new Point3D(239.0,187.0,204.0)),
        new AbstractMap.SimpleEntry("Moderate Purplish-Pink", new Point3D(213.0,151.0,174.0)),
        new AbstractMap.SimpleEntry("Dark Purplish-Pink", new Point3D(193.0,126.0,145.0)),
        new AbstractMap.SimpleEntry("Pale Purplish-Pink", new Point3D(232.0,204.0,215.0)),
        new AbstractMap.SimpleEntry("Grayish Purplish-Pink", new Point3D(195.0,166.0,177.0)),
        
        // B. Red family
        // B.1 "Red" brach (=vivid red)
        new AbstractMap.SimpleEntry("Vivid Red", new Point3D(190.0,1.0,50.0)),
        new AbstractMap.SimpleEntry("Strong Red", new Point3D(188.0,63.0,74.0)),
        new AbstractMap.SimpleEntry("Deep Red", new Point3D(132.0,27.0,45.0)),
        new AbstractMap.SimpleEntry("Very Deep Red", new Point3D(92.0,9.0,35.0)),
        new AbstractMap.SimpleEntry("Moderate Red", new Point3D(171.0,78.0,82.0)),
        new AbstractMap.SimpleEntry("Dark Red", new Point3D(114.0,47.0,55.0)),
        new AbstractMap.SimpleEntry("Very Dark Red", new Point3D(63.0,23.0,40.0)),
        new AbstractMap.SimpleEntry("Light Grayish Red", new Point3D(173.0,136.0,132.0)),
        new AbstractMap.SimpleEntry("Grayish Red", new Point3D(144.0,93.0,93.0)),
        new AbstractMap.SimpleEntry("Dark Grayish Red", new Point3D(84.0,61.0,63.0)),
        new AbstractMap.SimpleEntry("Blackish Red", new Point3D(46.0,29.0,33.0)),        
        // B.2 "Purplish-Red" brach (=vivid purplish-red)
        new AbstractMap.SimpleEntry("Vivid Purplish-Red", new Point3D(206.0,70.0,118.0)),
        new AbstractMap.SimpleEntry("Strong Purplish-Red", new Point3D(179.0,68.0,108.0)),
        new AbstractMap.SimpleEntry("Deep Purplish-Red", new Point3D(120.0,24.0,74.0)),
        new AbstractMap.SimpleEntry("Very Deep Purplish-Red", new Point3D(84.0,19.0,59.0)),
        new AbstractMap.SimpleEntry("Moderate Purplish-Red", new Point3D(168.0,81.0,110.0)),
        new AbstractMap.SimpleEntry("Dark Purplish-Red", new Point3D(103.0,49.0,71.0)),
        new AbstractMap.SimpleEntry("Very Dark Purplish-Red", new Point3D(56.0,21.0,44.0)),
        new AbstractMap.SimpleEntry("Light Grayish Purplish-Red", new Point3D(175.0,134.0,142.0)),
        new AbstractMap.SimpleEntry("Grayish Purplish-Red", new Point3D(145.0,95.0,109.0)),
        
        // C. Orange family    
        // C.1 "Orange" brach (=vivid orange)
        new AbstractMap.SimpleEntry("Vivid Orange", new Point3D(243.0,132.0,1.0)),   
        new AbstractMap.SimpleEntry("Brilliant Orange", new Point3D(253.0,148.0,63.0)),   
        new AbstractMap.SimpleEntry("Strong Orange", new Point3D(237.0,135.0,45.0)),   
        new AbstractMap.SimpleEntry("Deep Orange", new Point3D(190.0,101.0,22.0)),   
        new AbstractMap.SimpleEntry("Light Orange", new Point3D(250.0,181.0,127.0)),   
        new AbstractMap.SimpleEntry("Moderate Orange", new Point3D(217.0,144.0,88.0)),   
        // C.2 "Yellow-Orange" brach (=vivid yellow-orange)
        new AbstractMap.SimpleEntry("Vivid Yellow-Orange", new Point3D(246.0,166.0,1.0)),
        new AbstractMap.SimpleEntry("Brilliant Yellow-Orange", new Point3D(254.0,193.0,79.0)),
        new AbstractMap.SimpleEntry("Strong Yellow-Orange", new Point3D(234.0,162.0,33.0)),
        new AbstractMap.SimpleEntry("Deep Yellow-Orange", new Point3D(201.0,133.0,1.0)),
        new AbstractMap.SimpleEntry("Light Yellow-Orange", new Point3D(251.0,201.0,127.0)),
        new AbstractMap.SimpleEntry("Moderate Yellow-Orange", new Point3D(227.0,168.0,87.0)),
        new AbstractMap.SimpleEntry("Dark Yellow-Orange", new Point3D(190.0,138.0,61.0)),
        new AbstractMap.SimpleEntry("Pale Yellow-Orange", new Point3D(250.0,214.0,165.0)),
        // C.3 "Reddish-Orange" brach (=vivid reddish-orange)
        new AbstractMap.SimpleEntry("Vivid Reddish-Orange", new Point3D(226.0,88.0,34.0)),
        new AbstractMap.SimpleEntry("Strong Reddish-Orange", new Point3D(217.0,96.0,59.0)),
        new AbstractMap.SimpleEntry("Deep Reddish-Orange", new Point3D(170.0,56.0,30.0)),
        new AbstractMap.SimpleEntry("Moderate Reddish-Orange", new Point3D(203.0,109.0,81.0)),
        new AbstractMap.SimpleEntry("Dark Reddish-Orange", new Point3D(158.0,71.0,50.0)),
        new AbstractMap.SimpleEntry("Grayish Reddish-Orange", new Point3D(180.0,116.0,94.0)),
        // C.4 "Brownish-Orange" brach (=brownish-orange)
        new AbstractMap.SimpleEntry("Brownish-Orange", new Point3D(174, 105, 56)),
                
        // D. Brown family
        // D.1 "Brown" brach (= strong-brown)
        new AbstractMap.SimpleEntry("Strong Brown", new Point3D(128.0,70.0,27.0)),
        new AbstractMap.SimpleEntry("Deep Brown", new Point3D(89.0,51.0,25.0)),
        new AbstractMap.SimpleEntry("Light Brown", new Point3D(166.0,123.0,91.0)),
        new AbstractMap.SimpleEntry("Moderate Brown", new Point3D(111.0,78.0,55.0)),
        new AbstractMap.SimpleEntry("Dark Brown", new Point3D(66.0,37.0,24.0)),
        new AbstractMap.SimpleEntry("Light Grayish Brown", new Point3D(149.0,128.0,112.0)),
        new AbstractMap.SimpleEntry("Grayish Brown", new Point3D(99.0,81.0,71.0)),
        new AbstractMap.SimpleEntry("Dark Grayish Brown", new Point3D(62.0,50.0,44.0)),
        // D.2 "Reddish-Brown" brach (=strong reddish-brown)
        new AbstractMap.SimpleEntry("Strong Reddish-Brown", new Point3D(136.0,45.0,23.0)),
        new AbstractMap.SimpleEntry("Deep Reddish-Brown", new Point3D(86.0,7.0,12.0)),
        new AbstractMap.SimpleEntry("Light Reddish-Brown", new Point3D(168.0,124.0,109.0)),
        new AbstractMap.SimpleEntry("Moderate Reddish-Brown", new Point3D(121.0,68.0,59.0)),
        new AbstractMap.SimpleEntry("Dark Reddish-Brown", new Point3D(62.0,29.0,30.0)),
        new AbstractMap.SimpleEntry("Light Grayish Reddish-Brown", new Point3D(151.0,127.0,115.0)),
        new AbstractMap.SimpleEntry("Grayish Reddish-Brown", new Point3D(103.0,76.0,71.0)),
        new AbstractMap.SimpleEntry("Dark Grayish Reddish-Brown", new Point3D(67.0,48.0,46.0)),        
        // D.3 "Yellowish-Brown" brach (=strong yellowish-brown)       
        new AbstractMap.SimpleEntry("Strong Yellowish-Brown", new Point3D(153.0,101.0,21.0)),
        new AbstractMap.SimpleEntry("Deep Yellowish-Brown", new Point3D(101.0,69.0,34.0)),
        new AbstractMap.SimpleEntry("Light Yellowish-Brown", new Point3D(193.0,154.0,107.0)),
        new AbstractMap.SimpleEntry("Moderate Yellowish-Brown", new Point3D(130.0,102.0,68.0)),
        new AbstractMap.SimpleEntry("Dark Yellowish-Brown", new Point3D(75.0,54.0,33.0)),
        new AbstractMap.SimpleEntry("Light Grayish Yellowish-Brown", new Point3D(174.0,155.0,130.0)),
        new AbstractMap.SimpleEntry("Grayish Yellowish-Brown", new Point3D(126.0,109.0,90.0)),
        new AbstractMap.SimpleEntry("Dark Grayish Yellowish-Brown", new Point3D(72.0,60.0,50.0)),
        // D.4 "Olive-Brown" brach (=no coincidence in iscc-complete)        
        new AbstractMap.SimpleEntry("Light Olive-Brown", new Point3D(150.0,113.0,23.0)),
        new AbstractMap.SimpleEntry("Moderate Olive-Brown", new Point3D(108.0,84.0,30.0)),
        new AbstractMap.SimpleEntry("Dark Olive-Brown", new Point3D(59.0,49.0,33.0)),
        
        // E. Yellow family
        // E.1 "Yellow" brach (=vivid yellow) 
        new AbstractMap.SimpleEntry("Vivid Yellow", new Point3D(243.0,195.0,1.0)),   
        new AbstractMap.SimpleEntry("Brilliant Yellow", new Point3D(250.0,218.0,94.0)),   
        new AbstractMap.SimpleEntry("Strong Yellow", new Point3D(212.0,175.0,55.0)),   
        new AbstractMap.SimpleEntry("Deep Yellow", new Point3D(175.0,141.0,19.0)),   
        new AbstractMap.SimpleEntry("Light Yellow", new Point3D(248.0,222.0,126.0)),   
        new AbstractMap.SimpleEntry("Moderate Yellow", new Point3D(201.0,174.0,93.0)),   
        new AbstractMap.SimpleEntry("Dark Yellow", new Point3D(171.0,145.0,68.0)),   
        new AbstractMap.SimpleEntry("Pale Yellow", new Point3D(243.0,229.0,171.0)),   
        new AbstractMap.SimpleEntry("Grayish Yellow", new Point3D(194.0,178.0,128.0)),   
        new AbstractMap.SimpleEntry("Dark Grayish Yellow", new Point3D(161.0,143.0,96.0)),   
        // E.2 "Greenish-Yellow" brach (=vivid greenish-yellow)
        new AbstractMap.SimpleEntry("Vivid Greenish-Yellow", new Point3D(220.0,211.0,1.0)),
        new AbstractMap.SimpleEntry("Brilliant Greenish-Yellow", new Point3D(233.0,228.0,80.0)),
        new AbstractMap.SimpleEntry("Strong Greenish-Yellow", new Point3D(190.0,183.0,46.0)),
        new AbstractMap.SimpleEntry("Deep Greenish-Yellow", new Point3D(155.0,148.0,1.0)),
        new AbstractMap.SimpleEntry("Light Greenish-Yellow", new Point3D(234.0,230.0,121.0)),
        new AbstractMap.SimpleEntry("Moderate Greenish-Yellow", new Point3D(185.0,180.0,89.0)),
        new AbstractMap.SimpleEntry("Dark Greenish-Yellow", new Point3D(152.0,148.0,62.0)),
        new AbstractMap.SimpleEntry("Pale Greenish-Yellow", new Point3D(235.0,232.0,164.0)),
        new AbstractMap.SimpleEntry("Grayish Greenish-Yellow", new Point3D(185.0,181.0,125.0)),
        
        // F. Olive family
        // F.1 "Olive" brach (=moderate olive) 
        new AbstractMap.SimpleEntry("Light Olive", new Point3D(134.0,126.0,54.0)),
        new AbstractMap.SimpleEntry("Moderate Olive", new Point3D(102.0,93.0,30.0)),
        new AbstractMap.SimpleEntry("Dark Olive", new Point3D(64.0,61.0,33.0)),
        new AbstractMap.SimpleEntry("Light Grayish Olive", new Point3D(140.0,135.0,103.0)),
        new AbstractMap.SimpleEntry("Grayish Olive", new Point3D(91.0,88.0,66.0)),
        new AbstractMap.SimpleEntry("Dark Grayish Olive", new Point3D(54.0,53.0,39.0)),        
        // F.2 "Green-Olive" brach (=strong green-olive)
        new AbstractMap.SimpleEntry("Strong Green-Olive", new Point3D(64.0,79.0,1.0)),
        new AbstractMap.SimpleEntry("Deep Green-Olive", new Point3D(35.0,47.0,1.0)),
        new AbstractMap.SimpleEntry("Moderate Green-Olive", new Point3D(74.0,93.0,35.0)),
        new AbstractMap.SimpleEntry("Dark Green-Olive", new Point3D(43.0,61.0,38.0)),
        new AbstractMap.SimpleEntry("Grayish Green-Olive", new Point3D(81.0,87.0,68.0)),
        new AbstractMap.SimpleEntry("Dark Grayish Green-Olive", new Point3D(49.0,54.0,43.0)),
             
        // G. Yellow-green family
        // G.1 "Yellow-Green" brach (=vivid yellow-green)
        new AbstractMap.SimpleEntry("Vivid Yellow-Green", new Point3D(141.0,182.0,1.0)),
        new AbstractMap.SimpleEntry("Brilliant Yellow-Green", new Point3D(189.0,218.0,87.0)),
        new AbstractMap.SimpleEntry("Strong Yellow-Green", new Point3D(126.0,159.0,46.0)),
        new AbstractMap.SimpleEntry("Deep Yellow-Green", new Point3D(70.0,113.0,41.0)),
        new AbstractMap.SimpleEntry("Light Yellow-Green", new Point3D(201.0,220.0,137.0)),
        new AbstractMap.SimpleEntry("Moderate Yellow-Green", new Point3D(138.0,154.0,91.0)),
        new AbstractMap.SimpleEntry("Pale Yellow-Green", new Point3D(218.0,223.0,183.0)),
        new AbstractMap.SimpleEntry("Grayish Yellow-Green", new Point3D(143.0,151.0,121.0)),
        
        // H. Green family
        // H.1 "Green" brach (=vivid green) 
        new AbstractMap.SimpleEntry("Vivid Green", new Point3D(1.0,136.0,86.0)), 
        new AbstractMap.SimpleEntry("Brilliant Green", new Point3D(62.0,180.0,137.0)), 
        new AbstractMap.SimpleEntry("Strong Green", new Point3D(1.0,121.0,89.0)), 
        new AbstractMap.SimpleEntry("Deep Green", new Point3D(1.0,84.0,61.0)), 
        new AbstractMap.SimpleEntry("Very Light Green", new Point3D(142.0,209.0,178.0)), 
        new AbstractMap.SimpleEntry("Light Green", new Point3D(106.0,171.0,142.0)), 
        new AbstractMap.SimpleEntry("Moderate Green", new Point3D(59.0,120.0,97.0)), 
        new AbstractMap.SimpleEntry("Dark Green", new Point3D(27.0,77.0,62.0)), 
        new AbstractMap.SimpleEntry("Very Dark Green", new Point3D(28.0,53.0,45.0)), 
        new AbstractMap.SimpleEntry("Very Pale Green", new Point3D(199.0,230.0,215.0)), 
        new AbstractMap.SimpleEntry("Pale Green", new Point3D(141.0,163.0,153.0)), 
        new AbstractMap.SimpleEntry("Grayish Green", new Point3D(94.0,113.0,106.0)), 
        new AbstractMap.SimpleEntry("Dark Grayish Green", new Point3D(58.0,75.0,71.0)), 
        new AbstractMap.SimpleEntry("Blackish Green", new Point3D(26.0,36.0,33.0)), 
        // H.2 "Yellowish-Green" brach (=vivid yellowish-green)     
        new AbstractMap.SimpleEntry("Vivid Yellowish-Green", new Point3D(39.0,166.0,76.0)),
        new AbstractMap.SimpleEntry("Brilliant Yellowish-Green", new Point3D(131.0,211.0,125.0)),
        new AbstractMap.SimpleEntry("Strong Yellowish-Green", new Point3D(68.0,148.0,74.0)),
        new AbstractMap.SimpleEntry("Deep Yellowish-Green", new Point3D(1.0,98.0,45.0)),
        new AbstractMap.SimpleEntry("Very Deep Yellowish-Green", new Point3D(1.0,49.0,24.0)),
        new AbstractMap.SimpleEntry("Very Light Yellowish-Green", new Point3D(182.0,229.0,175.0)),
        new AbstractMap.SimpleEntry("Light Yellowish-Green", new Point3D(147.0,197.0,146.0)),
        new AbstractMap.SimpleEntry("Moderate Yellowish-Green", new Point3D(103.0,146.0,103.0)),
        new AbstractMap.SimpleEntry("Dark Yellowish-Green", new Point3D(53.0,94.0,59.0)),
        new AbstractMap.SimpleEntry("Very Dark Yellowish-Green", new Point3D(23.0,54.0,32.0)),
        // H.3 "Bluish-Green" brach (=vivid bluish-green)
        new AbstractMap.SimpleEntry("Vivid Bluish-Green", new Point3D(1.0,136.0,130.0)),
        new AbstractMap.SimpleEntry("Brilliant Bluish-Green", new Point3D(1.0,166.0,147.0)),
        new AbstractMap.SimpleEntry("Strong Bluish-Green", new Point3D(1.0,122.0,116.0)),
        new AbstractMap.SimpleEntry("Deep Bluish-Green", new Point3D(1.0,68.0,63.0)),
        new AbstractMap.SimpleEntry("Very Light Bluish-Green", new Point3D(150.0,222.0,209.0)),
        new AbstractMap.SimpleEntry("Light Bluish-Green", new Point3D(102.0,173.0,164.0)),
        new AbstractMap.SimpleEntry("Moderate Bluish-Green", new Point3D(49.0,120.0,115.0)),
        new AbstractMap.SimpleEntry("Dark Bluish-Green", new Point3D(1.0,75.0,73.0)),
        new AbstractMap.SimpleEntry("Very Dark Bluish-Green", new Point3D(1.0,42.0,41.0)),
        
        // I. Blue family
        // I.1 "Blue" brach (=vivid blue)        
        new AbstractMap.SimpleEntry("Vivid Blue", new Point3D(1.0,161.0,194.0)),
        new AbstractMap.SimpleEntry("Brilliant Blue", new Point3D(73.0,151.0,208.0)),
        new AbstractMap.SimpleEntry("Strong Blue", new Point3D(1.0,103.0,165.0)),
        new AbstractMap.SimpleEntry("Deep Blue", new Point3D(1.0,65.0,106.0)),
        new AbstractMap.SimpleEntry("Very Light Blue", new Point3D(161.0,202.0,241.0)),
        new AbstractMap.SimpleEntry("Light Blue", new Point3D(112.0,163.0,204.0)),
        new AbstractMap.SimpleEntry("Moderate Blue", new Point3D(67.0,107.0,149.0)),
        new AbstractMap.SimpleEntry("Dark Blue", new Point3D(1.0,48.0,78.0)),
        new AbstractMap.SimpleEntry("Very Pale Blue", new Point3D(188.0,212.0,230.0)),
        new AbstractMap.SimpleEntry("Pale Blue", new Point3D(145.0,163.0,176.0)),
        new AbstractMap.SimpleEntry("Grayish Blue", new Point3D(83.0,104.0,120.0)),
        new AbstractMap.SimpleEntry("Dark Grayish Blue", new Point3D(54.0,69.0,79.0)),
        new AbstractMap.SimpleEntry("Blackish Blue", new Point3D(32.0,40.0,48.0)),
        // I.2 "Greenish-Blue" brach (=vivid greenish-blue)        
        new AbstractMap.SimpleEntry("Vivid Greenish-Blue", new Point3D(1.0,133.0,161.0)),
        new AbstractMap.SimpleEntry("Brilliant Greenish-Blue", new Point3D(35.0,158.0,186.0)),
        new AbstractMap.SimpleEntry("Strong Greenish-Blue", new Point3D(1.0,119.0,145.0)),
        new AbstractMap.SimpleEntry("Deep Greenish-Blue", new Point3D(46.0,132.0,149.0)),
        new AbstractMap.SimpleEntry("Very Light Greenish-Blue", new Point3D(156.0,209.0,220.0)),
        new AbstractMap.SimpleEntry("Light Greenish-Blue", new Point3D(102.0,170.0,188.0)),
        new AbstractMap.SimpleEntry("Moderate Greenish-Blue", new Point3D(54.0,117.0,136.0)),
        new AbstractMap.SimpleEntry("Dark Greenish-Blue", new Point3D(1.0,73.0,88.0)),
        new AbstractMap.SimpleEntry("Very Dark Greenish-Blue", new Point3D(1.0,46.0,59.0)),
        // I.3 "Purplish-Blue" brach (=vivid purplish-blue)
        new AbstractMap.SimpleEntry("Vivid Purplish-Blue", new Point3D(48.0,38.0,122.0)),
        new AbstractMap.SimpleEntry("Brilliant Purplish-Blue", new Point3D(108.0,121.0,184.0)),
        new AbstractMap.SimpleEntry("Strong Purplish-Blue", new Point3D(84.0,90.0,167.0)),
        new AbstractMap.SimpleEntry("Deep Purplish-Blue", new Point3D(39.0,36.0,88.0)),
        new AbstractMap.SimpleEntry("Very Light Purplish-Blue", new Point3D(179.0,188.0,226.0)),
        new AbstractMap.SimpleEntry("Light Purplish-Blue", new Point3D(135.0,145.0,191.0)),
        new AbstractMap.SimpleEntry("Moderate Purplish-Blue", new Point3D(78.0,81.0,128.0)),
        new AbstractMap.SimpleEntry("Dark Purplish-Blue", new Point3D(37.0,36.0,64.0)),
        new AbstractMap.SimpleEntry("Very Pale Purplish-Blue", new Point3D(192.0,200.0,225.0)),
        new AbstractMap.SimpleEntry("Pale Purplish-Blue", new Point3D(140.0,146.0,172.0)),
        new AbstractMap.SimpleEntry("Grayish Purplish-Blue", new Point3D(76.0,81.0,109.0)),

        // J. Purple family  
        // J.1 "Purple" brach (=vivid purple)
        new AbstractMap.SimpleEntry("Vivid Purple", new Point3D(154.0,78.0,174.0)),
        new AbstractMap.SimpleEntry("Brilliant Purple", new Point3D(211.0,153.0,230.0)),
        new AbstractMap.SimpleEntry("Strong Purple", new Point3D(135.0,86.0,146.0)),
        new AbstractMap.SimpleEntry("Deep Purple", new Point3D(96.0,47.0,107.0)),
        new AbstractMap.SimpleEntry("Very Deep Purple", new Point3D(64.0,26.0,76.0)),
        new AbstractMap.SimpleEntry("Very Light Purple", new Point3D(213.0,186.0,219.0)),
        new AbstractMap.SimpleEntry("Light Purple", new Point3D(182.0,149.0,192.0)),
        new AbstractMap.SimpleEntry("Moderate Purple", new Point3D(134.0,96.0,142.0)),
        new AbstractMap.SimpleEntry("Dark Purple", new Point3D(86.0,60.0,92.0)),
        new AbstractMap.SimpleEntry("Very Dark Purple", new Point3D(48.0,25.0,52.0)),
        new AbstractMap.SimpleEntry("Very Pale Purple", new Point3D(214.0,202.0,221.0)),
        new AbstractMap.SimpleEntry("Pale Purple", new Point3D(170.0,152.0,169.0)),
        new AbstractMap.SimpleEntry("Grayish Purple", new Point3D(121.0,104.0,120.0)),
        new AbstractMap.SimpleEntry("Dark Grayish Purple", new Point3D(80.0,64.0,77.0)),
        new AbstractMap.SimpleEntry("Blackish Purple", new Point3D(41.0,30.0,41.0)),
        // J.2 "Violet" brach (=vivid violet)
        new AbstractMap.SimpleEntry("Vivid Violet", new Point3D(144.0,101.0,202.0)),
        new AbstractMap.SimpleEntry("Brilliant Violet", new Point3D(126.0,115.0,184.0)),
        new AbstractMap.SimpleEntry("Strong Violet", new Point3D(96.0,78.0,151.0)),
        new AbstractMap.SimpleEntry("Deep Violet", new Point3D(50.0,23.0,77.0)),
        new AbstractMap.SimpleEntry("Very Light Violet", new Point3D(220.0,208.0,254.0)),
        new AbstractMap.SimpleEntry("Light Violet", new Point3D(140.0,130.0,182.0)),
        new AbstractMap.SimpleEntry("Moderate Violet", new Point3D(96.0,78.0,129.0)),
        new AbstractMap.SimpleEntry("Dark Violet", new Point3D(47.0,33.0,64.0)),
        new AbstractMap.SimpleEntry("Very Pale Violet", new Point3D(196.0,195.0,221.0)),
        new AbstractMap.SimpleEntry("Pale Violet", new Point3D(150.0,144.0,171.0)),
        new AbstractMap.SimpleEntry("Grayish Violet", new Point3D(85.0,76.0,105.0)),
        // J.3 "Reddish-Purple" brach (=vivid reddish-purple)
        new AbstractMap.SimpleEntry("Vivid Reddish-Purple", new Point3D(135.0,1.0,116.0)),
        new AbstractMap.SimpleEntry("Strong Reddish-Purple", new Point3D(158.0,79.0,136.0)),
        new AbstractMap.SimpleEntry("Deep Reddish-Purple", new Point3D(112.0,41.0,99.0)),
        new AbstractMap.SimpleEntry("Very Deep Reddish-Purple", new Point3D(84.0,25.0,78.0)),
        new AbstractMap.SimpleEntry("Light Reddish-Purple", new Point3D(183.0,132.0,167.0)),
        new AbstractMap.SimpleEntry("Moderate Reddish-Purple", new Point3D(145.0,92.0,131.0)),
        new AbstractMap.SimpleEntry("Dark Reddish-Purple", new Point3D(93.0,57.0,84.0)),
        new AbstractMap.SimpleEntry("Very Dark Reddish-Purple", new Point3D(52.0,23.0,49.0)),
        new AbstractMap.SimpleEntry("Pale Reddish-Purple", new Point3D(170.0,138.0,158.0)),
        new AbstractMap.SimpleEntry("Grayish Reddish-Purple", new Point3D(131.0,100.0,121.0)),
            
        // K. Grey level family
        // "White" brach (=no coincidence in iscc-complete)
        new AbstractMap.SimpleEntry("White", new Point3D(250.0,250.0,250.0)),
        new AbstractMap.SimpleEntry("Pinkish White", new Point3D(234.0,227.0,225.0)),
        new AbstractMap.SimpleEntry("Yellowish White", new Point3D(240.0,234.0,214.0)),
        new AbstractMap.SimpleEntry("Greenish White", new Point3D(223.0,237.0,232.0)),
        new AbstractMap.SimpleEntry("Bluish White", new Point3D(233.0,233.0,237.0)),
        new AbstractMap.SimpleEntry("Purplish White", new Point3D(232.0,227.0,229.0)),
        // "Gray" brach (=no coincidence in iscc-complete)
        new AbstractMap.SimpleEntry("Light Gray", new Point3D(185.0,184.0,181.0)),
        new AbstractMap.SimpleEntry("Medium Gray", new Point3D(132.0,132.0,130.0)),
        new AbstractMap.SimpleEntry("Dark Gray", new Point3D(85.0,85.0,85.0)),
        new AbstractMap.SimpleEntry("Pinkish Gray", new Point3D(193.0,182.0,179.0)),
        new AbstractMap.SimpleEntry("Reddish Gray", new Point3D(143.0,129.0,127.0)),
        new AbstractMap.SimpleEntry("Dark Reddish Gray", new Point3D(92.0,80.0,79.0)),        
        new AbstractMap.SimpleEntry("Light Brownish Gray", new Point3D(142.0,130.0,121.0)),
        new AbstractMap.SimpleEntry("Brownish Gray", new Point3D(91.0,80.0,79.0)),
        new AbstractMap.SimpleEntry("Yellowish Gray", new Point3D(191.0,184.0,165.0)),
        new AbstractMap.SimpleEntry("Light Olive Gray", new Point3D(138.0,135.0,118.0)),
        new AbstractMap.SimpleEntry("Olive Gray", new Point3D(87.0,85.0,76.0)),
        new AbstractMap.SimpleEntry("Light Greenish Gray", new Point3D(178.0,190.0,181.0)),
        new AbstractMap.SimpleEntry("Greenish Gray", new Point3D(125.0,137.0,132.0)),
        new AbstractMap.SimpleEntry("Dark Greenish Gray", new Point3D(78.0,87.0,85.0)),
        new AbstractMap.SimpleEntry("Light Bluish Gray", new Point3D(180.0,188.0,192.0)),
        new AbstractMap.SimpleEntry("Bluish Gray", new Point3D(129.0,135.0,139.0)),
        new AbstractMap.SimpleEntry("Dark Bluish Gray", new Point3D(81.0,88.0,94.0)),
        new AbstractMap.SimpleEntry("Light Purplish Gray", new Point3D(191.0,185.0,189.0)),
        new AbstractMap.SimpleEntry("Purplish Gray", new Point3D(139.0,133.0,137.0)),
        new AbstractMap.SimpleEntry("Dark Purplish Gray", new Point3D(93.0,85.0,91.0)),
        // "Black" brach (=no coincidence in iscc-complete)       
        new AbstractMap.SimpleEntry("Black", new Point3D(3.0,3.0,3.0)),
        new AbstractMap.SimpleEntry("Reddish Black", new Point3D(40.0,32.0,34.0)),
        new AbstractMap.SimpleEntry("Brownish Black", new Point3D(40.0,32.0,28.0)),
        new AbstractMap.SimpleEntry("Olive Black", new Point3D(37.0,36.0,29.0)),
        new AbstractMap.SimpleEntry("Greenish Black", new Point3D(30.0,35.0,33.0)),
        new AbstractMap.SimpleEntry("Bluish Black", new Point3D(32.0,36.0,40.0)),
        new AbstractMap.SimpleEntry("Purplish Black", new Point3D(36.0,33.0,36.0))
    };
    
    
 
    /**
     * Returns a customized version of the ISCC complete set of colors.
     * 
     * @return a customized version of the ISCC complete set of colors.
     */
    static final public ISCCColorMap POSITIVE_CUSTOMIZED_ISCC_COMPLETE(){
        ISCCColorMap output = new ISCCColorMap(TYPE_COMPLETE);        
        // B. Red family
        output.replace("Vivid Red", new Point3D(230.0,25.0,25.0));        
        // E. Yellow family
        output.replace("Vivid Yellow", new Point3D(230.0,230.0,25.0));                   
        // H. Green family
        output.put("RGB-Pure Green", new Point3D(25.0,230.0,25.0));  
        // I. Blue family
        output.replace("Vivid Blue", new Point3D(25.0,25.0,230.0)); 
        output.put("Sea Blue", new Point3D(37.0,36.0,64.0));
        // K. Grey level family
        output.replace("Light Gray", new Point3D(153.0,153.0,153.0));
        output.replace("Medium Gray", new Point3D(128.0,128.0,128.0));        
        output.replace("Dark Gray", new Point3D(103.0,103.0,103.0)); 
        output.replace("Black", new Point3D(7, 7, 7));
        
        return output;
    }

    /**
     * Returns a customized version of the ISCC complete set of colors.
     * 
     * @return a customized version of the ISCC complete set of colors.
     */
    static final public ISCCColorMap NEGATIVE_CUSTOMIZED_ISCC_COMPLETE(){
        ISCCColorMap output = new ISCCColorMap(TYPE_COMPLETE);        
        output.put("Light Black", new Point3D(55.0,55.0,55.0)); 
        output.put("Very Dark Blue", new Point3D(18.0,18.0,32.0));   
        output.replace("White", new Point3D(220.0,220.0,220.0));   
        return output;
    }
    
    
    
    /*
     * Patterns for the main color families.
     */
    static final public String PINK_PATTERN = ".*Pink$";
    static final public String PINK_CORE_PATTERN = ".*( Pink)$";   
    static final public String PINK_NODARK_PATTERN = "(?!.*Dark|.*Grayish).*Pink$";
    static final public String PINK_CORE_NODARK_PATTERN = "(?!.*Dark|.*Grayish).*( Pink)$";
    
    static final public String RED_PATTERN = ".*Red$";
    static final public String RED_CORE_PATTERN = ".*( Red)$";
    static final public String RED_NODARK_PATTERN = "(?!.*Dark|.*Blackish|.*Grayish).*Red$";  
    static final public String RED_CORE_NODARK_PATTERN = "(?!.*Dark|.*Blackish|.*Grayish).*( Red)$";
    
    static final public String ORANGE_PATTERN = ".*Orange$";
    static final public String ORANGE_CORE_PATTERN = ".*( Orange)$";  
    static final public String ORANGE_NODARK_PATTERN = "(?!.*Dark).*Orange$";
    static final public String ORANGE_CORE_NODARK_PATTERN = "(?!.*Dark).*( Orange)$";
    
    static final public String BROWN_PATTERN = ".*Brown$";
    static final public String BROWN_CORE_PATTERN = ".*( Brown)$";
    static final public String BROWN_NODARK_PATTERN = "(?!.*Dark|.*Grayish).*Brown$";
    static final public String BROWN_CORE_NODARK_PATTERN = "(?!.*Dark|.*Grayish).*( Brown)$";
    
    static final public String YELLOW_PATTERN = ".*Yellow$";
    static final public String YELLOW_CORE_PATTERN = ".*( Yellow)$";    
    static final public String YELLOW_NODARK_PATTERN = "(?!.*Dark|.*Grayish).*Yellow$";
    static final public String YELLOW_CORE_NODARK_PATTERN = "(?!.*Dark|.*Grayish).*( Yellow)$";
    
    static final public String OLIVE_PATTERN = ".*Olive$";
    static final public String OLIVE_CORE_PATTERN = ".*( Olive)$";    
    static final public String OLIVE_NODARK_PATTERN = "(?!.*Dark|.*Grayish).*Olive$";
    static final public String OLIVE_CORE_NODARK_PATTERN = "(?!.*Dark|.*Grayish).*( Olive)$";
    
    static final public String YELLOWGREEN_PATTERN = ".*Yellow-Green$";
    static final public String YELLOWGREEN_CORE_PATTERN = ".*( Yellow-Green)$";
    static final public String YELLOWGREEN_NODARK_PATTERN = "(?!.*Dark|.*Grayish).*( Yellow-Green)$";
    static final public String YELLOWGREEN_CORE_NODARK_PATTERN = "(?!.*Dark|.*Grayish).*( Yellow-Green)$";   
    
    static final public String GREEN_PATTERN = "(?!.*Yellow-).*Green$";
    static final public String GREEN_CORE_PATTERN = "(?!.*Yellow-).*( Green)$";
    static final public String GREEN_NODARK_PATTERN = "(?!.*Dark|.*Blackish|.*Grayish|.*Yellow-).*Green$";
    static final public String GREEN_CORE_NODARK_PATTERN = "(?!.*Dark|.*Blackish|.*Grayish|.*Yellow-).*( Green)$";
    
    static final public String BLUE_PATTERN = ".*Blue";
    static final public String BLUE_CORE_PATTERN = ".*( Blue)";
    static final public String BLUE_NODARK_PATTERN = "(?!.*Dark|.*Grayish|.*Blackish).*Blue$";
    static final public String BLUE_CORE_NODARK_PATTERN = "(?!.*Dark|.*Grayish|.*Blackish).*( Blue)$";
    
    static final public String PURPLE_PATTERN = ".*(Purple|Violet)$";
    static final public String PURPLE_CORE_PATTERN = ".*( Purple| Violet)$";
    static final public String PURPLE_NODARK_PATTERN = "(?!.*Dark|.*Grayish|.*Blackish).*(Purple|Violet)$";
    static final public String PURPLE_CORE_NODARK_PATTERN = "(?!.*Dark|.*Grayish|.*Blackish).*( Purple| Violet)$";
    static final public String PURPLEONLY_PATTERN = ".*Purple$";
    static final public String PURPLEONLY_CORE_PATTERN = ".*( Purple)$";
    static final public String PURPLEONLY_NODARK_PATTERN = "(?!.*Dark|.*Grayish|.*Blackish).*Purple$";
    static final public String PURPLEONLY_CORE_NODARK_PATTERN = "(?!.*Dark|.*Grayish|.*Blackish).*( Purple)$";
    static final public String VIOLET_PATTERN = ".*Violet$";
    static final public String VIOLET_CORE_PATTERN = ".*( Violet)$";
    static final public String VIOLET_NODARK_PATTERN = "(?!.*Dark|.*Grayish|.*Blackish).*Violet$";
    static final public String VIOLET_CORE_NODARK_PATTERN = "(?!.*Dark|.*Grayish|.*Blackish).*( Violet)$";
    
    static final public String WHITE_PATTERN = ".*White$";
    static final public String GRAY_PATTERN = ".*Gray$";
    static final public String GRAY_NODARK_PATTERN = "(?!.*Dark).*Gray$";
    static final public String BLACK_PATTERN = ".*Black$";


    static final public String NOT(String pattern){
        return "(?!"+pattern+").*";
    }
    
    static final public Map.Entry ISCC_BASIC_GRANULAR_PROTOTYPE_PATTERNS_pALLnALL[] = {
        new AbstractMap.SimpleEntry("Pink", new Pair(PINK_PATTERN, NOT(PINK_PATTERN))),
        new AbstractMap.SimpleEntry("Red", new Pair(RED_PATTERN, NOT(RED_PATTERN))),
        new AbstractMap.SimpleEntry("Orange", new Pair(ORANGE_PATTERN, NOT(ORANGE_PATTERN))),
        new AbstractMap.SimpleEntry("Brown", new Pair(BROWN_PATTERN, NOT(BROWN_PATTERN))),
        new AbstractMap.SimpleEntry("Yellow", new Pair(YELLOW_PATTERN, NOT(YELLOW_PATTERN))),
        new AbstractMap.SimpleEntry("Olive", new Pair(OLIVE_PATTERN, NOT(OLIVE_PATTERN))),
        new AbstractMap.SimpleEntry("Yellow-Green", new Pair(YELLOWGREEN_PATTERN, NOT(YELLOWGREEN_PATTERN))),
        new AbstractMap.SimpleEntry("Green", new Pair(GREEN_PATTERN, NOT(GREEN_PATTERN))),
        new AbstractMap.SimpleEntry("Blue", new Pair(BLUE_PATTERN, NOT(BLUE_PATTERN))),
        new AbstractMap.SimpleEntry("Purple", new Pair(PURPLE_PATTERN, NOT(PURPLE_PATTERN))),
        new AbstractMap.SimpleEntry("White", new Pair(WHITE_PATTERN, NOT(WHITE_PATTERN))),
        new AbstractMap.SimpleEntry("Gray", new Pair(GRAY_PATTERN, NOT(GRAY_PATTERN))),
        new AbstractMap.SimpleEntry("Black", new Pair(BLACK_PATTERN, NOT(BLACK_PATTERN)))
    };

    static final public Map.Entry ISCC_BASIC_GRANULAR_PROTOTYPE_PATTERNS_pCOREnALL[] = {
        new AbstractMap.SimpleEntry("Pink", new Pair(PINK_CORE_NODARK_PATTERN, NOT(PINK_PATTERN))),
        new AbstractMap.SimpleEntry("Red", new Pair(RED_CORE_NODARK_PATTERN, NOT(RED_PATTERN))),
        new AbstractMap.SimpleEntry("Orange", new Pair(ORANGE_CORE_NODARK_PATTERN, NOT(ORANGE_PATTERN))),
        new AbstractMap.SimpleEntry("Brown", new Pair(BROWN_CORE_NODARK_PATTERN, NOT(BROWN_PATTERN))),
        new AbstractMap.SimpleEntry("Yellow", new Pair(YELLOW_CORE_NODARK_PATTERN, NOT(YELLOW_PATTERN))),
        new AbstractMap.SimpleEntry("Olive", new Pair(OLIVE_CORE_NODARK_PATTERN, NOT(OLIVE_PATTERN))),
        new AbstractMap.SimpleEntry("Yellow-Green", new Pair(YELLOWGREEN_CORE_NODARK_PATTERN, NOT(YELLOWGREEN_PATTERN))),
        new AbstractMap.SimpleEntry("Green", new Pair(GREEN_CORE_NODARK_PATTERN, NOT(GREEN_PATTERN))),
        new AbstractMap.SimpleEntry("Blue", new Pair(BLUE_CORE_NODARK_PATTERN, NOT(BLUE_PATTERN))),
        new AbstractMap.SimpleEntry("Purple", new Pair(PURPLE_CORE_NODARK_PATTERN, NOT(PURPLE_PATTERN))),
        new AbstractMap.SimpleEntry("White", new Pair(WHITE_PATTERN, NOT(WHITE_PATTERN))),
        new AbstractMap.SimpleEntry("Gray", new Pair(GRAY_NODARK_PATTERN, NOT(GRAY_PATTERN))),
        new AbstractMap.SimpleEntry("Black", new Pair(BLACK_PATTERN, NOT(BLACK_PATTERN)))
    };
    
    static final String PINK_POSITIVES = "Vivid Pink|Strong Pink|Deep Pink|Light Pink|Moderate Pink|Pale Pink";
    static final String PINK_NEGATIVES = "Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black";    
    static final String RED_POSITIVES = "Vivid Red|Strong Red|Deep Red|Very Deep Red|Moderate Red|Vivid Purplish-Red";
    static final String RED_NEGATIVES = "Vivid Pink|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black";
    static final String ORANGE_POSITIVES = "Vivid Orange|Brilliant Orange|Strong Orange|Deep Orange|Moderate Orange";
    static final String ORANGE_NEGATIVES = "Vivid Pink|Vivid Red|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black";
    static final String BROWN_POSITIVES = "Strong Brown|Deep Brown|Light Brown|Moderate Brown|Dark Brown";
    static final String BROWN_NEGATIVES = "Vivid Pink|Vivid Red|Vivid Orange|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black";
    static final String YELLOW_POSITIVES = "Vivid Yellow|Strong Yellow|Deep Yellow|Light Yellow|Moderate Yellow|Pale Yellow";
    static final String YELLOW_NEGATIVES = "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black";
    static final String OLIVE_POSITIVES = "Light Olive|Moderate Olive";
    static final String OLIVE_NEGATIVES = "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black";
    static final String YELLOWGREEN_POSITIVES = "Vivid Yellow-Green|Brilliant Yellow-Green|Moderate Yellow-Green";
    static final String YELLOWGREEN_NEGATIVES = "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black";
    static final String GREEN_POSITIVES = "Vivid Green|Brilliant Green|Light Green|Moderate Green|RGB-Pure Green";
    static final String GREEN_NEGATIVES = "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black";
    static final String BLUE_POSITIVES = "Vivid Blue|Brilliant Blue|Strong Blue|Deep Blue|Very Light Blue|Moderate Blue|Pale Blue|Vivid Greenish-Blue"
                                        +"|Sea Blue";
    static final String BLUE_NEGATIVES = "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Purple|White|Medium Gray|Black"
                                        +"|Very Dark Blue";
    static final String PURPLE_POSITIVES = "Vivid Purple|Brilliant Purple|Strong Purple|Light Purple|Moderate Purple|Vivid Violet|Brilliant Violet|Strong Violet|Light Violet|Moderate Violet";
    static final String PURPLE_NEGATIVES = "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|White|Medium Gray|Black";
    static final String WHITE_POSITIVES = "White";
    static final String WHITE_NEGATIVES = "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|Medium Gray|Black";
    static final String GRAY_POSITIVES = "Light Gray|Medium Gray|Dark Gray";
    static final String GRAY_NEGATIVES = "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Black";
    static final String BLACK_POSITIVES = "Black";
    static final String BLACK_NEGATIVES = "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray"+"|Light Black";
    
    static final public Map.Entry ISCC_BASIC_GRANULAR_PROTOTYPE_PATTERNS_pCUSTOMIZEDnALL[] = {
        new AbstractMap.SimpleEntry("Pink", new Pair(PINK_POSITIVES, NOT(PINK_PATTERN))),
        new AbstractMap.SimpleEntry("Red", new Pair(RED_POSITIVES, NOT(RED_PATTERN))),
        new AbstractMap.SimpleEntry("Orange", new Pair(ORANGE_POSITIVES, NOT(ORANGE_PATTERN))),
        new AbstractMap.SimpleEntry("Brown", new Pair(BROWN_POSITIVES, NOT(BROWN_PATTERN))),
        new AbstractMap.SimpleEntry("Yellow", new Pair(YELLOW_POSITIVES, NOT(YELLOW_PATTERN))),
        new AbstractMap.SimpleEntry("Olive", new Pair(OLIVE_POSITIVES, NOT(OLIVE_PATTERN))),
        new AbstractMap.SimpleEntry("Yellow-Green", new Pair(YELLOWGREEN_POSITIVES, NOT(YELLOWGREEN_PATTERN))),
        new AbstractMap.SimpleEntry("Green", new Pair(GREEN_POSITIVES, NOT(GREEN_PATTERN))),
        new AbstractMap.SimpleEntry("Blue", new Pair(BLUE_POSITIVES, NOT(BLUE_PATTERN))),
        new AbstractMap.SimpleEntry("Purple", new Pair(PURPLE_POSITIVES, NOT(PURPLE_PATTERN))),
        new AbstractMap.SimpleEntry("White", new Pair(WHITE_POSITIVES, NOT(WHITE_PATTERN))),
        new AbstractMap.SimpleEntry("Gray", new Pair(GRAY_POSITIVES, NOT(GRAY_PATTERN))),
        new AbstractMap.SimpleEntry("Black", new Pair(BLACK_POSITIVES, NOT(BLACK_PATTERN)))
    };
    
    static final public Map.Entry ISCC_BASIC_GRANULAR_PROTOTYPE_PATTERNS_pCUSTOMIZEDnCUSTOMIZED[] = {
        //Pair of positives and negatives prototypes, expressed as a pattern over the ISCC complete set of colors
        new AbstractMap.SimpleEntry("Pink", new Pair(PINK_POSITIVES, PINK_NEGATIVES)),
        new AbstractMap.SimpleEntry("Red", new Pair(RED_POSITIVES, RED_NEGATIVES)),
        new AbstractMap.SimpleEntry("Orange", new Pair(ORANGE_POSITIVES,ORANGE_NEGATIVES)),
        new AbstractMap.SimpleEntry("Brown", new Pair(BROWN_POSITIVES,BROWN_NEGATIVES)),
        new AbstractMap.SimpleEntry("Yellow", new Pair(YELLOW_POSITIVES,YELLOW_NEGATIVES)),
        new AbstractMap.SimpleEntry("Olive", new Pair(OLIVE_POSITIVES,OLIVE_NEGATIVES)),
        new AbstractMap.SimpleEntry("Yellow-Green",new Pair(YELLOWGREEN_POSITIVES,YELLOWGREEN_NEGATIVES)),
        new AbstractMap.SimpleEntry("Green", new Pair(GREEN_POSITIVES,GREEN_NEGATIVES)),
        new AbstractMap.SimpleEntry("Blue", new Pair(BLUE_POSITIVES,BLUE_NEGATIVES)),
        new AbstractMap.SimpleEntry("Purple", new Pair(PURPLE_POSITIVES,PURPLE_NEGATIVES)),
        new AbstractMap.SimpleEntry("White", new Pair(WHITE_POSITIVES,WHITE_NEGATIVES)),
        new AbstractMap.SimpleEntry("Gray", new Pair(GRAY_POSITIVES,GRAY_NEGATIVES)),
        new AbstractMap.SimpleEntry("Black", new Pair(BLACK_POSITIVES,BLACK_NEGATIVES))
    };
    
//    static final public Map.Entry ISCC_BASIC_GRANULAR_PROTOTYPE_PATTERNS_pCUSTOMIZEDnCUSTOMIZED[] = {
//        //Pair of positives and negatives prototypes, expressed as a pattern over the ISCC complete set of colors
//        new AbstractMap.SimpleEntry("Pink", new Pair("Vivid Pink|Strong Pink|Deep Pink|Light Pink|Moderate Pink|Pale Pink",
//                "Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black")),
//        new AbstractMap.SimpleEntry("Red", new Pair("Vivid Red|Strong Red|Deep Red|Very Deep Red|Moderate Red|Vivid Purplish-Red", 
//                "Vivid Pink|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black")),
//        new AbstractMap.SimpleEntry("Orange", new Pair("Vivid Orange|Brilliant Orange|Strong Orange|Deep Orange|Moderate Orange", 
//                "Vivid Pink|Vivid Red|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black")),
//        new AbstractMap.SimpleEntry("Brown", new Pair("Strong Brown|Deep Brown|Light Brown|Moderate Brown|Dark Brown", 
//                "Vivid Pink|Vivid Red|Vivid Orange|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black")),
//        new AbstractMap.SimpleEntry("Yellow", new Pair("Vivid Yellow|Strong Yellow|Deep Yellow|Light Yellow|Moderate Yellow|Pale Yellow", 
//                "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black"+
//                "|Vivid Greenish-Yellow")),
//        new AbstractMap.SimpleEntry("Olive", new Pair("Light Olive|Moderate Olive", 
//                "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black")),
//        new AbstractMap.SimpleEntry("Yellow-Green",new Pair("Vivid Yellow-Green|Brilliant Yellow-Green|Moderate Yellow-Green", 
//                "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black")),
//        new AbstractMap.SimpleEntry("Green", new Pair("Vivid Green|Brilliant Green|Light Green|Moderate Green|RGB-Pure Green", 
//                "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Blue|Vivid Purple|White|Medium Gray|Black")),
//        new AbstractMap.SimpleEntry("Blue", new Pair("Vivid Blue|Brilliant Blue|Strong Blue|Deep Blue|Very Light Blue|Moderate Blue|Pale Blue|Vivid Greenish-Blue", 
//                "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Purple|White|Medium Gray|Black")),
//        new AbstractMap.SimpleEntry("Purple", new Pair("Vivid Purple|Brilliant Purple|Strong Purple|Light Purple|Moderate Purple|Vivid Violet|Brilliant Violet|Strong Violet|Light Violet|Moderate Violet", 
//                "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|White|Medium Gray|Black")),
//        new AbstractMap.SimpleEntry("White", new Pair("White", 
//                "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|Medium Gray|Black")),
//        new AbstractMap.SimpleEntry("Gray", new Pair("Light Gray|Medium Gray|Dark Gray", 
//                "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Black")),
//        new AbstractMap.SimpleEntry("Black", new Pair("Black", 
//                "Vivid Pink|Vivid Red|Vivid Orange|Strong Brown|Vivid Yellow|Moderate Olive|Vivid Yellow-Green|Vivid Green|Vivid Blue|Vivid Purple|White|Medium Gray"))
//    };
    
    
}


