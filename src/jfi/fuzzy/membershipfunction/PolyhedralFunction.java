package jfi.fuzzy.membershipfunction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jfi.geometry.Line3D;
import jfi.geometry.Point3D;
import jfi.geometry.Polyhedron;

/**
 * Class representing a membership function in a polyhedral shape.
 *
 * @author Míriam Mengíbar Rodríguez (mirismr@correo.ugr.es)
 */
public class PolyhedralFunction implements MembershipFunction<Point3D> {

    /**
     * Polyhedron list representing some alpha cuts. The position 0 refers to
     * kernel volume, size/2 position the 0.5 alpha cut and size-1 position
     * refers to support volume.
     */
    private final List<Polyhedron> volumes;

    /**
     * Point representing the polyhedron centroid.
     */
    private final Point3D centroid;
        
    /**
     * Creates a polyhedral-based function.
     * @param centroid the polyhedron centroid.
     * @param kernelVolume the polyhedron associate to the kernel of fuzzy set.
     * @param alphaCut05Volume the polyhedron associate to the 0.5 alpha cut of
     * fuzzy set.
     * @param supportVolume the polyhedron associate to the support of fuzzy set.
     */
    public PolyhedralFunction(Point3D centroid, Polyhedron kernelVolume, Polyhedron alphaCut05Volume, Polyhedron supportVolume) {
        this.centroid = centroid;
        this.volumes = new ArrayList<Polyhedron>(){{ add(kernelVolume); add(alphaCut05Volume); add(supportVolume);}};
    }
    
    /**
     * Applies this membership function to the given point.
     * 
     * @param pointToEvaluate the given point
     * @return the function result.
     */
    @Override
    public Double apply(Point3D pointToEvaluate) {
        Double membershipDegree = 0.0;
        Polyhedron kernelPolytope = volumes.get(0);
        Polyhedron supportPolytope = volumes.get(volumes.size() - 1);

        try {
            // check if point is in the support
            if (supportPolytope.isPointInside(pointToEvaluate) && !supportPolytope.isPointInFace(pointToEvaluate)) {
                // check if is in the kernel
                if (kernelPolytope != null && kernelPolytope.isPointInside(pointToEvaluate)) {
                    membershipDegree = 1.0;
                } else {
                    Line3D linePointToCentroide = new Line3D(this.centroid, pointToEvaluate);
                    // calculate distance to each polytope
                    List<Double> distances = new ArrayList<Double>();
                    for (Polyhedron polytopeI : this.volumes) {
                        Double distToPolytope = -1.0;
                        Point3D polytopePointIntersection = polytopeI.getIntersectionPoint(linePointToCentroide);                       
                        if (polytopePointIntersection != null) {
                            try {
                                distToPolytope = polytopePointIntersection.distance(this.centroid);
                            } catch (Exception ex) {
                                Logger.getLogger(PolyhedralFunction.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        distances.add(distToPolytope);
                    }                   
                    // parameters
                    Double a = distances.get(0);
                    Double b = distances.get(1);
                    Double c = distances.get(2);
                    // calculate the distance between point and centroid
                    Double distPointToCentroide = linePointToCentroide.getDirectorVector().module();
                    if (distPointToCentroide <= a) {
                        membershipDegree = 1.0;
                    }
                    if (distPointToCentroide > c) {
                        membershipDegree = 0.0;
                    }
                    if (a < distPointToCentroide && distPointToCentroide <= b) {
                        membershipDegree = (Double) (((b - distPointToCentroide) + (b - a)) / (2 * (b - a)));
                    } else {
                        membershipDegree = (Double) ((c - distPointToCentroide) / (2 * (c - b)));
                    }
                }
            } else {
                membershipDegree = 0.0;
            }
        } catch (Exception ex) {
            Logger.getLogger(PolyhedralFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return membershipDegree;
    }

    /**
     * Returns the volumes associated to this function which corresponds to the
     * kernel, 0.5 alpha-cut and support of this polyhedral-based fuzzy color.
     * @return the volumes associated to this fuzzy color.
     */
    public List<Polyhedron> getVolumes() {
        return volumes;
    }
    
    /**
     * Returns the centroid associated to this function which corresponds to the 
     * centroid of the volumes.
     * 
     * @return the prototype associated to this fuzzy color
     */
    public Point3D getCentroid() {
        return centroid;
    }
}
