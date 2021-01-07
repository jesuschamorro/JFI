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
     * Returns a new point resulting of adding this point and the given point.
     * 
     * @param p the given point.
     * @return a new added point.
     */
    public Point3D add(Point3D p) {        
        double px = p.getX() + this.getX();
        double py = p.getY() + this.getY();
        double pz = p.getZ() + this.getZ();
        return new Point3D(px,py,pz);
    }
    
    /**
     * Returns a new point resulting of subtracting this point and the given
     * point.
     * 
     * @param p the given point.
     * @return a new subtracted point.
     */
    public Point3D subtract(Point3D p) {        
        double px = this.getX() - p.getX();
        double py = this.getY() - p.getY();
        double pz = this.getZ() - p.getZ();
        return new Point3D(px,py,pz);
    }
    
    /**
     * Applies a scaling transformation to this point.
     * 
     * @param sx the scale factor for the z-coordinate
     * @param sy the scale factor for the z-coordinate
     * @param sz the scale factor for the z-coordinate
     */
    public void scale(double sx, double sy, double sz){
        this.x *= sx;
        this.y *= sy;
        this.z *= sz;
    }
    
    /**
     * Applies a scaling transformation to this point.
     * 
     * @param s the scale factor for the three coordinates
     */
    public void scale(double s){
        this.scale(s,s,s);
    }
    
    /**
     * Applies a translation transformation to this point.
     * 
     * @param dx the distance for the z-coordinate
     * @param dy the distance for the z-coordinate
     * @param dz the distance for the z-coordinate
     */
    public void translate(double dx, double dy, double dz){
        this.x += dx;
        this.y += dy;
        this.z += dz;
    }
    
    /**
     * Applies a translation transformation to this point.
     * 
     * @param offset the distance offset for the three coordinates.
     *
     */
    public void translate(double offset){
        this.translate(offset, offset, offset);
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
       
    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj reference object with which to compare.
     * @return {@code true} if this point is the same as the argument;
     * {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point3D) {
            Point3D p3d = (Point3D) obj;
            double EPSILON = 0.00001;
            return (Math.abs(x - p3d.x) < EPSILON && 
                    Math.abs(y - p3d.y) < EPSILON && 
                    Math.abs(z - p3d.z) < EPSILON);
        }
        return super.equals(obj);
    }

    /**
     * Returns a hash code value for this point. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link java.util.HashMap}.
     * 
     * @return a hash code value for this point.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.x) ^ 
                (Double.doubleToLongBits(this.x) >>> 32));
        return hash;
    }
    
    /**
     * Return the value of coordinate i of this point. Use this method (with a
     * loop) if you want apply a repetitive operation to all coordinates. 0 is
     * the x-coordinate 1 is the y-coordinate 2 is the z-coordinate
     *
     * @param i the index coordinate starting at 0.
     * @return x-coordinate, y-coordinate or z-coordinate
     */
    public Double getValueOfCoordinate(int i) {
        switch (i) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
            default:
                break;
        }

        return null;
    }

    /**
     * Get the point dimension
     *
     * @return always 3, since it is defined in 3D a space.
     */
    public int getDimension() {
        return 3;
    }

}
