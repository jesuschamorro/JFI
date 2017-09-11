package jfi.color;

import java.security.InvalidParameterException;
import java.util.AbstractMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import static jfi.color.ISCCColorMap.ISCC_BASIC;
import static jfi.color.ISCCColorMap.TYPE_BASIC;
import static jfi.color.ISCCColorMap.TYPE_EXTENDED;
import jfi.geometry.Point3D;

/**
 * Class for storing the ISCC–NBS color data as a tree.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class ISCCColorTree extends DefaultTreeModel{
    /**
     * The type of ISCC color set used. 
     */
    private final int type;
    
    /**
     * Constructs a new map of ISCC colors. By default, the extended set of
     * 31 colors is used.
     */
    public ISCCColorTree(){
        this(TYPE_EXTENDED);
    }
    
    /**
     * Constructs a new tree of ISCC colors.
     * 
     * @param type the set of colors used 
     * 
     * @see jfi.color.ISCCColorMap Allowed types for the set of colors used
     */
    public ISCCColorTree(int type){
        super(new DefaultMutableTreeNode("ISCC Color Tree"));         
        switch(type){
            case TYPE_BASIC:
                for (Map.Entry<String, Point3D> e : ISCC_BASIC) {
                    ((DefaultMutableTreeNode)this.root).add(new DefaultMutableTreeNode(e));
                }
                break;
            case TYPE_EXTENDED:
                createISCCExtended();
                break;    
            default:    
                throw new InvalidParameterException("Unknown type");
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
     * Creates a tree with two levels: the first one associated to the basic set 
     * of ISCC (13 colors) and the second one to extended colors.
     */
    private void createISCCExtended(){
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.root;
        DefaultMutableTreeNode color_node; 
        
        // Pink family 
        color_node = new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("Pink", new Point3D(254, 181, 186)));
        root.add(color_node);  
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("YellowishPink", new Point3D(254, 183, 165))) );
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("BrownishPink", new Point3D(194, 172, 153))) );
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("PurplishPink", new Point3D(230, 143, 172))) );
        // Red family
        color_node = new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("Red", new Point3D(190, 1, 50)));
        root.add(color_node);        
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("PurplishRed", new Point3D(206, 70, 118))) );
        // Orange family        
        color_node = new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("Orange", new Point3D(243, 132, 1)));
        root.add(color_node);       
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("YellowOrange", new Point3D(246, 166, 1))) );
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("ReddishOrange", new Point3D(226, 88, 34))) );
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("BrownishOrange", new Point3D(174, 105, 56))) );
        // Brown family
        color_node = new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("Brown", new Point3D(128, 70, 27)));
        root.add(color_node);
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("ReddishBrown", new Point3D(136, 45, 23))) );
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("YellowishBrown", new Point3D(153, 101, 21))) );
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("OliveBrown", new Point3D(107, 79, 13))) );
        // Yellow family
        color_node = new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("Yellow", new Point3D(243, 195, 1)));
        root.add(color_node);
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("GreenishYellow", new Point3D(220, 211, 1))) );
        // Olive family
        color_node = new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("Olive", new Point3D(102, 93, 30)));
        root.add(color_node);
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("GreenOlive", new Point3D(64, 79, 1))) );
        // Yellow-green family
        color_node = new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("Yellow-green", new Point3D(141, 182, 1)));
        root.add(color_node);
        // Green family
        color_node = new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("Green", new Point3D(1, 136, 86)));
        root.add(color_node);
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("YellowishGreen", new Point3D(39, 166, 76))) );
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("BluishGreen", new Point3D(1, 136, 130))) );
        // Blue family
        color_node = new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("Blue", new Point3D(1, 161, 194)));
        root.add(color_node);
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("GreenishBlue", new Point3D(1, 133, 161))) );
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("PurplishBlue", new Point3D(48, 38, 122))) );
        // Purple family        
        color_node = new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("Purple", new Point3D(154, 78, 174)));
        root.add(color_node);
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("Violet", new Point3D(144, 101, 202))) );
        color_node.add( new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("ReddishPurple", new Point3D(135, 1, 116))) );
        // Grey level family
        color_node = new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("White", new Point3D(252, 252, 249)));
        root.add(color_node);
        color_node = new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("Gray", new Point3D(135, 134, 134)));
        root.add(color_node);
        color_node = new DefaultMutableTreeNode(new AbstractMap.SimpleEntry("Black", new Point3D(7, 7, 7)));
        root.add(color_node);
    }
    
}
