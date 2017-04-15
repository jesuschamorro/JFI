package jfi.geometry;

/**
 * Class representing a point in a three-dimensional space.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class Point3D {
    /**
     * The x-coordinate of this point.
     */
    public double x;
    /**
     * The y-coordinate of this point.
     */
    public double y;
    /**
     * The z-coordinate of this point.
     */
    public double z;

    /**
     * Constructs a three-dimensional point and initializes it to (0,0,0)
     */
    public Point3D() {
    }

    /**
     * Constructs a three-dimensional point and initializes it with the
     * specified coordinates.
     *
     * @param x the new x-coordinate.
     * @param y the new y-coordinate.
     * @param z the new z-coordinate.
     */
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the x-coordinate of this point.
     * @return the x-coordinate of this point.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of this point.
     * @return the y-coordinate of this point.
     */
    public double getY() {
        return y;
    }
    
    /**
     * Returns the z-coordinate of this point.
     * @return the z-coordinate of this point.
     */
    public double getZ() {
        return z;
    }

    /**
     * Set the three coordinates of this point.
     * 
     * @param x the new x-coordinate.
     * @param y the new y-coordinate.
     * @param z the new z-coordinate.
     */
    public void setLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the distance from this point to a given point.
     *
     * @param px the x-coordinate of the given point.
     * @param py the y-coordinate of the given point.
     * @param pz the z-coordinate of the given point.
     * @return the distance between this point and a given point.
     */
    public double distance(double px, double py, double pz) {
        px -= getX();
        py -= getY();
        pz -= getZ();
        return Math.sqrt(px*px + py*py + pz*pz);
    }
    
    /**
     * Returns the distance from this point to a given point.
     * 
     * @param p the given point.
     * @return the distance from this point to a given point.
     */
    public double distance(Point3D p) {
        double px = p.getX() - this.getX();
        double py = p.getY() - this.getY();
        double pz = p.getZ() - this.getZ();
        return Math.sqrt(px*px + py*py + pz*pz);
    }
    
    /**
     * Returns a string that represents the value of this point.
     *
     * @return a string representation of this point.
     */
    @Override
    public String toString() {
        return "Point3D[" + x + ", " + y + ", " + z +"]";
    }

}
