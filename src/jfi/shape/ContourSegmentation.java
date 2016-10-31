package jfi.shape;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class ContourSegmentation extends ArrayList<ContourSegment>{
    private final Contour contour;
    
    public ContourSegmentation(Contour contour){
        this.contour = contour;
    }
    
    // Sobrecargar método add para garantizar que el segmento añadido pertenezca
    // al contorno
        
    public ContourSegment getSegment(Point2D point){
        for(ContourSegment s: this){
            if(s.contains(point)) return s;              
        }
        return null;
    }
    
    
    /**
     * Returns the contour containing the segments of this segmentation
     * 
     * @return the source contour
     */
    public Contour getContour(){
        return contour;
    }
    
    
    public BufferedImage toImage(Dimension dimension) {
        BufferedImage segmentationImage = null;
        segmentationImage = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = segmentationImage.createGraphics();
        g2d.setColor(Color.blue);
        Point mark;
        for (ContourSegment s : this) {
            mark = new Point((int) s.getStartPoint().getX(), (int) s.getStartPoint().getY());
            g2d.drawLine(mark.x - 3, mark.y, mark.x + 3, mark.y);
            g2d.drawLine(mark.x, mark.y - 3, mark.x, mark.y + 3);

        }
        return segmentationImage;
    }
}
