package jfi.geometry;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing a polyhedron in 3D spaces. A polytope is defined by a
 * collection of planes.
 *
 * @author Míriam Mengíbar Rodríguez (mirismr@correo.ugr.es)
 */
public class Polyhedron {
    /**
     * Set of faces
     */
    private List<PlanarPolygon> faces;

    /**
     * needed for checking if a point is inside of polytope
     */
    private Point3D innerPoint;

    /**
     * Creates a new polyhedron throught a faces set.
     * @param faces the face set
     */
    public Polyhedron(List<PlanarPolygon> faces) {
        this.faces = faces;

        // FUTURE WORK
        // calculate point inside just one time as the middle point
        // between the line from two vertices of differents faces
        // this.calculatePointInside();
    }

    /**
     * Returns the faces forming the polyhedron.
     * @return the faces forming the polyhedron.
     */
    public List<PlanarPolygon> getFaces() {
        return faces;
    }
    
    /**
     * Set the faces forming the polyhedron.
     * @param faces the faces forming the polyhedron.
     */
    public void setFaces(List<PlanarPolygon> faces) {
        this.faces = faces;
    }

    /**
     * Returns a polyhedron inner point.
     * @return the polyhedron inner point.
     */
    public Point3D getInnerPoint(){
        return this.innerPoint;
    }
    
    /**
     * Set the polyhedron inner point.
     * @param innerPoint the point inside of the polyhedron.
     */
    public void setInnerPoint(Point3D innerPoint) {
        this.innerPoint = innerPoint;
    }

    /**
     * Calculate one point inside of polytope. Its needed for check is a point
     * is inside.
     */
    private void calculatePointInside() {
        throw new NoSuchMethodError("Not implemented yet.");
    }

    /**
     * Give the minimum intersection point with a line. We check intersection
     * with each polytope's face.
     *
     * @param line to insersect the polytope
     * @return the insersection point, null if no intersection.
     */
    public Point3D getIntersectionPoint(Line3D line) {
        double minDistance = Double.MAX_VALUE;
        Point3D out = null;
        Vector3D directionVector = line.getDirectorVector();

        for (PlanarPolygon hface : this.faces) {
            Point3D pIntersection = hface.getPlane().getIntersectionPoint(line);
            try {
                // distance with line's origin
                double distance = pIntersection.distance(line.getBelongerPoint());
                if (directionVector.isSameDirection(new Vector3D(line.getBelongerPoint(), pIntersection)) && distance < minDistance) {
                    minDistance = distance;
                    out = pIntersection;
                }
            } catch (Exception ex) {
                Logger.getLogger(Polyhedron.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return out;
    }

    /**
     * Check if a point is in the polytope. Polytope's inner point should be not
     * null.
     *
     * @param point point to check
     * @return true if the point is in the polytope, false otherwise.
     */
    public boolean isPointInside(Point3D point) throws Exception {
        if (this.innerPoint == null) {
            throw new Exception("Inner point is null. Inner point should be fill, please use setInnerPoint before call this method.");
        }
        boolean in = true;
        double EPSILON = 0.00001;
        // If the sign of the result is > 0 the point is of the same side as 
        // the orthogonal
        for (int i = 0; i < this.faces.size() && in; i++) {
            try {
                PlanarPolygon hp = this.faces.get(i);
                Double eval = hp.getPlane().evaluatePoint(this.innerPoint) * hp.getPlane().evaluatePoint(point);
                if (eval < EPSILON * -1.0) { // rounding error
                    in = false;

                }
            } catch (Exception ex) {
                Logger.getLogger(Polyhedron.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return in;
    }

    /**
     * Check is a point is in any polytope face.
     *
     * @param point point to check
     * @return true if the point is in the polytope, false otherwise.
     */
    public boolean isPointInFace(Point3D point) {
        boolean in = false;
        double EPSILON = 0.00001;

        // result == 0 point lies in the plane
        for (int i = 0; i < this.faces.size() && !in; i++) {
            PlanarPolygon hp = this.faces.get(i);

            in = hp.getPlane().isInPlane(point);
        }
        return in;
    } 
    
    /**
     * Returns a string that represents this polyhedron.
     *
     * @return a string representation of this polyhedron.
     */
    public String toString() {
        String out = "";
        if(this.innerPoint == null) {
            out += "Not inner point\n";
        }
        else {
            out += this.innerPoint.toString()+"\n";
        }
        for(PlanarPolygon hp : this.faces) {
            out += hp.toString()+"\n";
        }       
        return out;
    }
}
