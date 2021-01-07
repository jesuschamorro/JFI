package jfi.geometry;

import java.util.List;

/**
 *
 * Class representing planar polygon in a 3D space.
 * It is defined by a plane and a vertex set (the polygon bounds).
 * 
 * @author Míriam Mengíbar Rodríguez (mirismr@correo.ugr.es)
 */
public class PlanarPolygon {

    /**
     * The plane where the polygon is defined.
     */
    private Plane plane;

    /**
     * The vertex set which delimites the polygon.
     */
    private List<Point3D> vertexSet;

    /**
     * Whether this polygon is open or not.
     */
    private boolean open;

    /**
     * Construct a new planar polygon throught the plane where it is defined
     * and the vertex set which delimites the polygon.
     *
     * @param plane the plane where the planar polygon is defined.
     * @param vertexSet the vertex set which delimites the planar polygon.
     */
    public PlanarPolygon(Plane plane, List<Point3D> vertexSet, boolean open) {
        this.plane = plane;
        this.vertexSet = vertexSet;
        this.open = open;
    }

    /**
     * Return the plane where the planar polygon is defined.
     *
     * @return the plane where the planar polygon is defined.
     */
    public Plane getPlane() {
        return plane;
    }

    /**
     * Returns the vertex set which delimites the planar polygon.
     *
     * @return the vertex set which delimites the planar polygon.
     */
    public List<Point3D> getVertexSet() {
        return this.vertexSet;
    }
    
    /**
     * Sets the vertex set which delimites the planar polygon.
     * @param vertexSet the vertex set which delimites the planar polygon.
     */
    public void setVertexSet(List<Point3D> vertexSet) {
        this.vertexSet = vertexSet;
    }

    /**
     * Returns if the polygon is open or not.
     * @return true if polygon is open, false otherwise.
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Sets if the polygon is open or not.
     * @param open if the polygon is open or not.
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * Returns a string that represents this planar polygon.
     *
     * @return a string representation of this planar polygon.
     */
    public String toString() {
        String out = plane.toString();
        out += " vertex set: " + vertexSet.toString();
        return out;
    }

}
