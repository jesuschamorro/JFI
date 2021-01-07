package jfi.geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing a line in 3D space.
 * A line can be defined by one director vector and one point that belongs to the line.
 * Parametric equation:
 * | x1 = P1 + V1*lambda
 * | x2 = P2 + V2*lambda
 * | x3 = P3 + V3*lambda
 * where xi is the i coordinate for a point that belongs to the line, 
 * Pi is the belonger point i coordinate and Vi is the director vector i coordinate.
 * 
 * @author Míriam Mengíbar Rodríguez (mirismr@correo.ugr.es)
 */
public class Line3D {
    /**
     * A director vector.
     */
    private Vector3D directorVector;

    /**
     * A point that belongs to the line.
     */
    private Point3D belongerPoint;
    
    /**
     * Construct a line through her director vector and one point.
     * @param directorVector the director vector
     * @param belongerPoint point belonging to the line
     */
    public Line3D(Vector3D directorVector, Point3D belongerPoint) {
        this.directorVector = directorVector;
        this.belongerPoint = belongerPoint;
    }
    
    /**
     * Construct a line through two points
     * @param origin origin point
     * @param destination destination point
     */
    public Line3D(Point3D origin, Point3D destination) {
        this.directorVector = new Vector3D(origin, destination);
        this.belongerPoint = origin;
    }
    
    /**
     * Give the result point substituting lambda in line's parametric equation.
     * @param lambda lambda value to substitute in parametric equation.
     * @return the result point.
     */
    public Point3D getPointSubstitutingLambda(Double lambda) {
        List<Double> coordinatesPoint = new ArrayList<Double>();
        
        for(int i=0; i < this.belongerPoint.getDimension(); i++) {
            Double xi = null;
            try {
                xi = this.belongerPoint.getValueOfCoordinate(i) + this.directorVector.getCoordinates().getValueOfCoordinate(i)*lambda;
            } catch (Exception ex) {
                Logger.getLogger(Line3D.class.getName()).log(Level.SEVERE, null, ex);
            }           
            coordinatesPoint.add(xi);
        }       
        return new Point3D(coordinatesPoint.get(0), coordinatesPoint.get(1), coordinatesPoint.get(2));
    }
    
    /**
     * Returns the line's director vector.
     * @return the line's director vector.
     */
    public Vector3D getDirectorVector() {
        return directorVector;
    }
    
    /**
     * Returns the line's belonger point.
     * @return the line's belonger point.
     */
    public Point3D getBelongerPoint() {
        return belongerPoint;
    }
}
